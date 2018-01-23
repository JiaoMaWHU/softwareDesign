package com.example.anfj.refereescore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
public class MainReferee extends AppCompatActivity {
    private static final int LENGTH=5;
    private Socket socket = null;
//    private BufferedReader in = null;
//    private PrintWriter out = null;
    private DataInputStream in=null;
    private DataOutputStream out=null;
    private String readInfo = "";
    private String sendInfo="";
    private boolean isSocketOk=true;
    private TextView athleteID;
    private TextView athleteName;
    private TextView eventTeam;
    private TextView eventType;
    private String getAtheletName=null;
    private String getAtheletID=null;
    private String getEventTeam=null;
    private String getEventType=null;
    private String a="a";
    private String b="b";
    private String c="c";
    private int socketType=-1;//1,判断打分，2.提交总分。
    private int refereesNumber=0;
    private String[] judgeRefereeId=new String[LENGTH];
    private boolean[] judgeScore=new boolean[LENGTH];
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private Button judgeScoreButton=null;
    private Button  submitScoreButton=null;
    private List<RefereeScore> refereesScore=new ArrayList<RefereeScore>();
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){


            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_referee);
        ActionBar actionBar=getSupportActionBar();//隐藏默认标题栏
        if(actionBar!=null){
            actionBar.hide();
        }
        judgeScoreButton=(Button)findViewById(R.id.judge_score_button);
        submitScoreButton=(Button)findViewById(R.id.submit_score_button);
        athleteName=(TextView)findViewById(R.id.athleteName1);
        athleteID=(TextView)findViewById(R.id.athleteID1);
        eventTeam=(TextView)findViewById(R.id.eventTeam1);
        eventType=(TextView)findViewById(R.id.eventType1);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        refereesScore.add(new RefereeScore());
        refereesScore.add(new RefereeScore());
        refereesScore.add(new RefereeScore());
        refereesScore.add(new RefereeScore());
        refereesScore.add(new RefereeScore());
        int i=0;
        for(RefereeScore refereeScore : refereesScore){
            i++;
            switch (i){
                case 1:
                    refereeScore.setCheckBox((CheckBox)findViewById(R.id.checkbox1));
                    refereeScore.setTextView((TextView)findViewById(R.id.referee_info1));
                    break;
                case 2:
                    refereeScore.setCheckBox((CheckBox)findViewById(R.id.checkbox2));
                    refereeScore.setTextView((TextView)findViewById(R.id.referee_info2));
                    break;
                case 3:
                    refereeScore.setCheckBox((CheckBox)findViewById(R.id.checkbox3));
                    refereeScore.setTextView((TextView)findViewById(R.id.referee_info3));
                    break;
                case 4:
                    refereeScore.setCheckBox((CheckBox)findViewById(R.id.checkbox4));
                    refereeScore.setTextView((TextView)findViewById(R.id.referee_info4));
                    break;
                case 5:
                    refereeScore.setCheckBox((CheckBox)findViewById(R.id.checkbox5));
                    refereeScore.setTextView((TextView)findViewById(R.id.referee_info5));
                    break;
                default:
                    break;
            }
        }
        new Thread(){
            public void run(){
                try {
                    Log.d("LoginActivity","开始连接");
                    socket = MSocket.getsocket();
                    Log.d("LoginActivity","连接成功");
//                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                    out = new PrintWriter(new OutputStreamWriter(
//                            socket.getOutputStream()));

//                    out.write("主裁判"+"\n");
//                    out.flush();
//                    readInfo=in.readLine();
                    //out.writeUTF("主裁判");
                    in=new DataInputStream(socket.getInputStream());
                    //out=new DataOutputStream(socket.getOutputStream());
                    while(true){
                        isSocketOk=false;
                        readInfo=in.readUTF();
                        Log.d("LoginActivity","主裁读取成功");
                        JSONObject readJson;
                        try{
                            readJson=new JSONObject(readInfo);
                            String jsonId=readJson.getString("jsonId");
                            Log.d("LoginActivity","准备进入eventStart："+jsonId);
                            if(jsonId.equals("eventStart")){
                                Log.d("LoginActivity","进入eventStart");
                                getAtheletName=readJson.getString("athletesName");
                                getAtheletID=readJson.getString("athletesId");
                                getEventTeam=readJson.getInt("eventTeam")+"";
                                getEventType=readJson.getString("eventType");
                                Intent intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
                                intent.putExtra("getname",getAtheletName.toString());
                                intent.putExtra("getid",getAtheletID.toString());
                                intent.putExtra("getteam",getEventTeam.toString());
                                intent.putExtra("gettype",getEventType.toString());
                                intent.putExtra("getsex",readJson.getInt("athleteSex")+"");
                                intent.putExtra("type","1");
                                localBroadcastManager.sendBroadcast(intent);
                            }else if(jsonId.equals("refereesScore")){
                                Intent intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
                                intent.putExtra("refereesInfo",readInfo.toString());
                                intent.putExtra("type","2");
                                localBroadcastManager.sendBroadcast(intent);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();



        new Thread(){
            public void run(){
                try {
                    Log.d("LoginActivity","开始连接");
                    socket = MSocket.getsocket();
                    Log.d("LoginActivity","连接成功");
//                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                    out = new PrintWriter(new OutputStreamWriter(
//                            socket.getOutputStream()));

//                    out.write("主裁判"+"\n");
//                    out.flush();
//                    readInfo=in.readLine();
                    //out.writeUTF("主裁判");
                    //in=new DataInputStream(socket.getInputStream());
                    out=new DataOutputStream(socket.getOutputStream());
                    while(true){
                        if(socketType==1)
                        {
                            JSONObject refereeJson=new JSONObject();
                            int i=0;
                            for(RefereeScore refereeScore : refereesScore)
                            {
                                judgeRefereeId[i]=refereeScore.getRefereeId();
                                if(refereeScore.getCheckBox().isChecked()){
                                    refereeScore.setCover(false);
                                    try{
                                        refereeJson.put("refereeId",Integer.parseInt(refereeScore.getRefereeId()));
                                        refereeJson.put("isScoreOk",false);
                                        refereeJson.put("jsonId","isRefereesOk");
                                        sendInfo=refereeJson.toString();
                                        out.writeUTF(sendInfo);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                            }
                                i++;
                            }
                            socketType=0;
                        }else if(socketType==2)
                        {
                            EditText pScoreEditText=(EditText)findViewById(R.id.p_score);
                            EditText dScoreEditText=(EditText)findViewById(R.id.d_score);
                            int pScore=Integer.parseInt(pScoreEditText.getText().toString());
                            int dScore=Integer.parseInt(dScoreEditText.getText().toString());
                            int[] scoreList={0,0,0,0,0};
                            int i=0;
                            for(RefereeScore refereeScore : refereesScore){
                                scoreList[i]=refereeScore.getScore();
                                i++;
                            }
                            Arrays.sort(scoreList);
                            //暂时实现5个副裁打分情况
                            int totalScore=((scoreList[1]+scoreList[2]+scoreList[3])/3)*5+dScore-pScore;
                            try{
                                JSONObject sendScoreJson=new JSONObject();
                                a=totalScore+"";
                                b=pScore+"";
                                c=dScore+"";
                                sendScoreJson.put("totalScore",a);
                                //sendScoreJson.put("pScore",b);
                                //sendScoreJson.put("dScore",c);
                                sendScoreJson.put("isScoreOk",true);
                                sendScoreJson.put("athletesId",getAtheletID.toString());
                                sendScoreJson.put("jsonId","isRefereesOk");
                                sendInfo=sendScoreJson.toString();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            out.writeUTF(sendInfo);
                            Intent intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
                            intent.putExtra("type","3");
                            intent.putExtra("pScore",pScore);
                            intent.putExtra("dScore",dScore);
                            intent.putExtra("pScore",totalScore);
                            Log.d("LoginActivity",totalScore+"|"+pScore+"|"+dScore);
                            localBroadcastManager.sendBroadcast(intent);
                            socketType=0;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        judgeScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isScoreOK=true;
                int i=0;
                for(RefereeScore refereeScore : refereesScore)
                {
                    judgeRefereeId[i]=refereeScore.getRefereeId();
                    if(refereeScore.getCheckBox().isChecked()){
                        isScoreOK=false;
                        judgeScore[i]=false;
                    }else {
                        judgeScore[i]=true;
                    }
                    i++;
                }
                if(!isScoreOK){
                    socketType=1;
                    isSocketOk=true;
//                    new Thread() {
//
//                        public void run() {
//                            try {
//                                socket = MSocket.getsocket();
//                                Log.d("LoginActivity","连接成功");
////                                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
////                                out = new PrintWriter(new OutputStreamWriter(
////                                        socket.getOutputStream()));
//                                in=new DataInputStream(socket.getInputStream());
//                                out=new DataOutputStream(socket.getOutputStream());
//                                if(socket.isConnected()){
//                                    if (!socket.isOutputShutdown()) {
//                                        try{
//                                            JSONArray sendJSONArray=new JSONArray();
//                                            for(int i=0;i<LENGTH;i++){
//                                                JSONObject sendJson=new JSONObject();
//                                                sendJson.put("refereeId",judgeRefereeId[i]);
//                                                sendJson.put("isScoreOk",judgeScore[i]);
//                                                sendJSONArray.put(sendJson);
//                                            }
//                                            String sendJSONArrayString=sendJSONArray.toString();
////                                            out.write(sendJSONArrayString+"\n");
////                                            out.flush();
//                                            out.writeUTF(sendJSONArrayString);
//                                            Intent intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
//                                            intent.putExtra("type","2");
//                                            //intent.putExtra("type","3");
//                                            localBroadcastManager.sendBroadcast(intent);
////                                            readInfo=in.readLine();
//                                            readInfo=in.readUTF();
//                                            intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
//                                            intent.putExtra("refereesInfo",readInfo.toString());
//                                            intent.putExtra("type","1");
//                                            intent.putExtra("ex_data","1");
//                                            localBroadcastManager.sendBroadcast(intent);
//                                            Log.d("MainActivity","接收数据成功");
//
//                                        }catch (Exception e)
//                                        {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
                }
            }
        });
        submitScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketType=2;
                isSocketOk=true;
//                EditText pScoreEditText=(EditText)findViewById(R.id.p_score);
//                EditText dScoreEditText=(EditText)findViewById(R.id.d_score);
//                int pScore=Integer.parseInt(pScoreEditText.getText().toString());
//                int dScore=Integer.parseInt(dScoreEditText.getText().toString());
//                int[] scoreList={0,0,0,0,0};
//                int i=0;
//                for(RefereeScore refereeScore : refereesScore){
//                    scoreList[i]=refereeScore.getScore();
//                    i++;
//                }
//                Arrays.sort(scoreList);
//                //暂时实现5个副裁打分情况
//                int totalScore=((scoreList[1]+scoreList[2]+scoreList[3])/3)*5+dScore-pScore;
//                try{
//                    JSONObject sendScoreJson=new JSONObject();
//                    sendScoreJson.put("totalScore",totalScore);
//                    sendScoreJson.put("pScore",pScore);
//                    sendScoreJson.put("dScore",dScore);
//                    sendInfo=sendScoreJson.toString();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                new Thread(){
//                    public void run(){
//                        try {
//                            socket = MSocket.getsocket();
////                            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
////                            out = new PrintWriter(new OutputStreamWriter(
////                                    socket.getOutputStream()));
//                            in=new DataInputStream(socket.getInputStream());
//                            out=new DataOutputStream(socket.getOutputStream());
////                            out.write(sendInfo+"\n");
////                            out.flush();
//                            out.writeUTF(sendInfo);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();

            }
        });
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.anfj.refereescore.LOCAL_BROADCAST");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
    class  LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                if(intent.getStringExtra("type").equals("1")){
                    Log.d("LoginActivity","进入type=1,设置头部信息");
                    athleteName.setText(intent.getStringExtra("getname"));
                    athleteID.setText(intent.getStringExtra("getid"));
                    eventType.setText(intent.getStringExtra("gettype"));
                    String teamtype=intent.getStringExtra("getteam");
                    String sexString="";
                    String athletSex=intent.getStringExtra("getsex").toString();
                    Log.d("LoginActivity",athletSex);
                    if(athletSex.equals("1")){
                        sexString="男子组";
                    }else{
                        sexString="女子组";
                    }
                    if(Integer.parseInt(teamtype)==1)
                    {
                        eventTeam.setText("(7-8岁"+sexString+")");
                    }
                    if(Integer.parseInt(teamtype)==2)
                    {
                        eventTeam.setText("(9-10岁"+sexString+")");
                    }
                    if(Integer.parseInt(teamtype)==3)
                    {
                        eventTeam.setText("(11-12岁"+sexString+")");
                    }
                    for(RefereeScore refereeScore : refereesScore){
                        refereeScore.setCover(false);
                        refereeScore.getTextView().setText("");
                    }
                }else if(intent.getStringExtra("type").equals("2")){
                    JSONObject refereeJson=new JSONObject(intent.getStringExtra("refereesInfo"));
                    boolean isFind=false;
                    String refereeId=refereeJson.getString("refereeId");
                    int i=0;
                    for(RefereeScore refereeScore : refereesScore){
                        if(!isFind)
                        {
                            if(refereeScore.getRefereeId()==refereeId){
                                isFind=true;
                                int score=refereeJson.getInt("score");
                                Log.d("MainReferee","裁判id"+refereeId+"分数"+score);
                                refereeScore.setRefereeId(refereeId);
                                refereeScore.setScore(score);
                                refereeScore.setCover(true);
                                int j=i+1;
                                //refereeScore.getTextView().setText("我们是副裁判");
                                refereeScore.getTextView().setText("第"+j+"位副裁判（ID"+refereeId+")打分："+score+"分");
                            }else if(!refereeScore.getCover()){
                                isFind=true;
                                int score=refereeJson.getInt("score");
                                Log.d("MainReferee","裁判id"+refereeId+"分数"+score);
                                refereeScore.setRefereeId(refereeId);
                                refereeScore.setScore(score);
                                refereeScore.setCover(true);
                                int j=i+1;
                                refereeScore.getTextView().setText("第"+j+"位副裁判（ID"+refereeId+")打分："+score+"分");
                            }
                        }
                        i++;

                    }
                }else if(intent.getStringExtra("type").equals("3")){
                    Toast.makeText(MainReferee.this,"评分反馈已经发送，请等待。。。",Toast.LENGTH_SHORT).show();
                    AlertDialog dialog=new AlertDialog.Builder(MainReferee.this)
                            .setTitle("公告栏")
                            .setMessage("总分："+a
                                    +" 奖励分："+c
                                    +" 惩罚分："+b).create();
                    dialog.show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class RefereeScore{
        private String refereeId="";
        private int score=-1;
        private boolean cover=false;
        private CheckBox checkBox=null;
        private TextView textView=null;
        public void setRefereeId(String refereeId){
            this.refereeId=refereeId;
        }
        public void setScore(int score){
            this.score=score;
        }
        public void setCheckBox(CheckBox checkBox){
            this.checkBox=checkBox;
        }
        public void setTextView(TextView textView){
            this.textView=textView;
        }
        public String getRefereeId(){
            return refereeId;
        }
        public int getScore(){
            return score;
        }
        public CheckBox getCheckBox(){
            return checkBox;
        }
        public  TextView getTextView(){
            return textView;
        }
        public void setCover(boolean cover){this.cover=cover;}
        public boolean getCover(){return  cover;}
    }
}
