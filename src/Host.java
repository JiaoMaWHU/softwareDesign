import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

public class Host{
    private int socketListNum=1;
    private static boolean isPrint = false;// 是否输出消息标志
    private static List<Server> thread_list = new ArrayList<Server>(); //服务器已启用线程集合
    private volatile static Map<Integer, String> message_list = new HashMap<Integer,String>();
    private int mainRefereeNum;
    private int refereeSumNum;
    private Map<Integer,Integer> sumScoreMap = new HashMap<Integer,Integer>();//用来发汇总信息给主裁的数据结构
    private boolean isTriger=false;//
    private boolean display=true;
    private boolean writeTimes=true;
    //=======================================
    Host(ServerSocket _server){
        System.out.println("创建服务器端成功！");
        conToDb _conToDb1=new conToDb();
        //
        refereeSumNum=_conToDb1.askForRefereeNum();
        Random rand = new Random();
        mainRefereeNum = rand.nextInt(refereeSumNum)+1;
         //mainRefereeNum=1;
        //
        try{
            ServerSocket server;
            server=_server;
            new PrintOutThread();
            new waitForTriger();
            while(true){
                Socket connection=server.accept();
                System.out.println("新客户端连接到线程！");
                Server s=new Server(connection,socketListNum);
                socketListNum++;
            }
        }catch(IOException e){
            System.out.println("链接意外结束");
        }
    }

    class PrintOutThread extends Thread {
        public PrintOutThread() {
            start();
        }
        @Override
        public void run() {
            System.out.println("send thread create succ");
            while (true) {
                if (message_list.size() > 0) {
                    // 将缓存在队列中的消息按顺序发送到各客户端，并从队列中清除。
                    Set set = message_list.keySet();
                    Iterator iter = set.iterator();
                    Integer socket_num;
                    socket_num= (Integer)iter.next();//返回key，同时指向下一个元素
                    for (Server thread : thread_list){
                        if (socket_num==thread.SocketNum){
                            thread.sendMessage(message_list.get(socket_num));
                            System.out.println("send "+message_list.get(socket_num));
                            break;
                        }
                    }
                    message_list.remove(socket_num);
                }
            }
        }
    }

    class waitForTriger extends Thread{
        class MyHandler implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
                System.out.println("Http have read");
                BufferedReader r = new BufferedReader(new InputStreamReader(t.getRequestBody(), "UTF-8"));
                String line = null;
                final StringBuilder b = new StringBuilder();
                while ((line = r.readLine()) != null) {
                    b.append(line);
                    b.append("\n");
                }
                try {
                    JSONObject jsonObject=new JSONObject(b.toString());
                    String jsonId=jsonObject.getString("jsonId");
                    if (jsonId.equals("display")){
                        conToDb _conToDb2=new conToDb();
                        _conToDb2.displayDb(display);
                        display=!display;
                    }else{
                        for(Server server : thread_list) {
                        server.pushMessage(jsonObject.toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String response = "Message send!";
                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
        //实现HTTP SERVER
        waitForTriger(){
            try {
                HttpServer hs = HttpServer.create(new InetSocketAddress(16000),10);
                hs.createContext("/refereeServer", new MyHandler());
                hs.setExecutor(null); // creates a default executor
                System.out.println("Http创建成功");
                hs.start();
            }catch (IOException e){
                e.printStackTrace();
            }
            start();
        }
    }

    class Server extends Thread{
        //每个对话都会新建一个server
        private Socket connection;
        private String userName=new String("");
        private String userPw=new String("");
        private conToDb _conToDb=new conToDb();
        private int SocketNum;
        private String jsonid;
        private String jsonString;
        DataInputStream in;
        DataOutputStream out;

        Server(Socket _connection,int _SocketNum){
            connection= _connection;
            SocketNum=_SocketNum;
            start();
        }
        public void run(){
            try{
                thread_list.add(this);
                // 先判断账户和密码
                in=new DataInputStream(connection.getInputStream());
                out=new DataOutputStream(connection.getOutputStream());
                boolean runnning=true;
                while(runnning){
                    jsonString=in.readUTF();
                    System.out.println("============");
                    JSONObject jObject = new JSONObject(jsonString);
                    jsonid=jObject.getString("jsonId");
                    switch (jsonid){
                        //登录
                        case "tryLogin":if(!checkUserIdentity(jObject)); break;
                        //大家都把分数发过来
                        case "refereesScore": receiveScore(jObject); break;
                        //总裁发的最后分数
                        case "isRefereesOk": isRefereesOk(jObject); break;
                        default: break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //线程关闭
                System.out.println("线程"+SocketNum+" 关闭");
                try {
                    _conToDb.finalize();
                    thread_list.remove(this);
                    connection.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        //验证登录身份
        private boolean checkUserIdentity(JSONObject jObject){
            try{
                System.out.println("开始验证用户名密码");
                userName=jObject.getString("refereeName");
                userPw=jObject.getString("refereePw");
                int isUserAtResult;
                isUserAtResult=_conToDb.askForRefereeLogin(userName,userPw);
                switch (isUserAtResult){
                    case 0: System.out.println("查询存在语句错误"); break;
                    case 1: System.out.println("没有该用户");break;
                    case 2: System.out.println("用户密码错误");break;
                    case 3: System.out.println("用户检验成功");break;
                }
                JSONObject json = new JSONObject();
                if (isUserAtResult!=3) {
                    System.out.println("验证账号密码失败！");
                    json.put("jsonId", "respondLogin");
                    json.put("login_result", new Boolean(false));
                    json.put("isMainReferee", new Boolean(false));
                    this.pushMessage(json.toString());
                    return false;
                }else{
                    System.out.println("验证账号密码成功！");
                    json.put("jsonId", "respondLogin");
                    json.put("login_result", new Boolean(true));
                    if (this.SocketNum==mainRefereeNum){
                        json.put("isMainReferee", new Boolean(true));
                    }else {
                        json.put("isMainReferee", new Boolean(false));
                    }
                    json.put("refereeId",new Integer(SocketNum));
                    this.pushMessage(json.toString());
                    try{
                        Thread.sleep(2000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
            return true;
        }

        //接受评分
        private void receiveScore(JSONObject jObject){
            try{
                    for(Server server : thread_list) {
                        if (server.SocketNum==mainRefereeNum){
                            server.pushMessage(jObject.toString());
                            System.out.println("!-=-=-=-===");
                        }
                    }
                }catch(Exception e){
                e.printStackTrace();
            }
        }

        //把消息发回数据库,再告知发令枪这一运动员已经评判成功，最后的显示应该由主裁显示
        private void isRefereesOk(JSONObject jObject){
            System.out.println(jObject);
            System.out.println("now sending ok");
            boolean isOk=false;
            try {
                isOk=jObject.getBoolean("isScoreOk");
                System.out.println(isOk);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Server server : thread_list) {
                server.pushMessage(jObject.toString());
            }
            if (isOk){
                if (display){
                    _conToDb.wirteBackToDb(jObject);
                }else{
                    _conToDb.wirteBackToDb1(jObject);
                }

            }
        }

        // 放入消息队列末尾，准备发送给客户端
        private void pushMessage(String msg) {
            message_list.put(SocketNum,msg);
        }

        // 向客户端发送一条消息
        private void sendMessage(String msg) {
            try{
                out.writeUTF(msg);
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }
}