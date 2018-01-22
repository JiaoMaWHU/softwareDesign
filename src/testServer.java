import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class testServer {
    static class waitForTriger extends Thread{
        class MyHandler implements HttpHandler {
            public void handle(HttpExchange t) throws IOException {
                BufferedReader r = new BufferedReader(new InputStreamReader(t.getRequestBody(), "UTF-8"));
                String line = null;
                final StringBuilder b = new StringBuilder();
                while ((line = r.readLine()) != null) {
                    b.append(line);
                    b.append("\n");
                }
                try {
                    JSONObject jsonObject=new JSONObject(b.toString());
                    System.out.println(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String response = "Message received!";
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
                hs.createContext("/refereeServer", new waitForTriger.MyHandler());
                hs.setExecutor(null); // creates a default executor
                hs.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){
        new AppAddTest();
        new AppAddTest();
    }

    static class AppAddTest extends Thread {
        public static final String ADD_URL = "http://localhost:16000/refereeServers";
        AppAddTest() {
            try {
                //创建连接
                URL url = new URL(ADD_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                connection.connect();

                //POST请求
                DataOutputStream out = new DataOutputStream(
                        connection.getOutputStream());
                JSONObject obj = new JSONObject();
                try {
                    obj.put("eventType", 1);
                    obj.put("athletesId", 1);
                    obj.put("athletesName", "wtf");
                    obj.put("jsonId", "eventStart");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                out.writeBytes(obj.toString());
                out.flush();
                out.close();
                //读取响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                System.out.println(sb);
                reader.close();
                // 断开连接
                connection.disconnect();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}