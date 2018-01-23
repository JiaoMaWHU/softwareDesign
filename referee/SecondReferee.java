package com.example.anfj.refereescore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class SecondReferee extends AppCompatActivity implements Runnable {
    private Socket socket = null;
//    private BufferedReader in = null;
//    private PrintWriter out = null;
    private DataInputStream in=null;
    private DataOutputStream out=null;
    private String readInfo = "";
    private String sendInfo="";
    private String readInfoCopy = "";
    private int refereeIdGoble=0;
    private boolean isTrueOK=true;
    private boolean isSocketOk=true;
    public static final int BUTTON_SEND=2;
    public static final int SCORE_SUCESS=1;
    public static final int SCORE_FAILED=0;
    private TextView athleteID;
    private TextView athleteName;
    private TextView eventTeam;
    private TextView eventType;
    private String getAtheletName=null;
    private String getAtheletID=null;
    private String getEventTeam=null;
    private String getEventType=null;
    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private int socketType=0;
//    private Handler handler=new Handler(){
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case ATHLETE_INFO:
//                    TextView tv=(TextView)findViewById(R.id.athleteName2);
//                    tv.setText("CAONIMAFJEIOIJFEJFEIOJ");
//            }
//        }
//    };
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SCORE_SUCESS:
                    Toast.makeText(SecondReferee.this,"打分成功",Toast.LENGTH_SHORT).show();
                    break;
                case SCORE_FAILED:
                    Toast.makeText(SecondReferee.this,"打分失败",Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_SEND:
                    Toast.makeText(SecondReferee.this,"数据发送中。。。",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
//            try{
//                JSONObject readJson=new JSONObject(readInfo);
//                Toast.makeText(SecondReferee.this,readJson.getString("athletesName"),Toast.LENGTH_SHORT).show();
//                switch (msg.what){
//                    case ATHLETE_INFO:
//                        Log.d("SecondReferee",readInfo);
//                        Log.d("SecondReferee",readJson.getString("athletesName"));
//                        Log.d("SecondReferee",readJson.getString("isMainReferee"));
//                        Toast.makeText(SecondReferee.this,"获取运动员信息",Toast.LENGTH_SHORT).show();
//                        Log.d("SecondReferee",athleteName.getText().toString());
//                        athleteName.setText("anfangjun");
//                        athleteID.setText(readJson.getString("athletesId"));
//                        break;
//                    default:
//                        break;
//                }
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_referee);
        ActionBar actionBar=getSupportActionBar();//隐藏默认标题栏
        if(actionBar!=null){
            actionBar.hide();
        }
        Intent initIntent=getIntent();
        //refereeIdGoble=initIntent.getStringExtra("referee_id");
        refereeIdGoble=initIntent.getIntExtra("referee_id",0);
        Log.d("LoginActivity",refereeIdGoble+"接收到了");
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        athleteName=(TextView)findViewById(R.id.athleteName2);
        athleteID=(TextView)findViewById(R.id.athleteID2);
        eventTeam=(TextView)findViewById(R.id.eventTeam2);
        eventType=(TextView)findViewById(R.id.eventType2);
        athleteName.setText("{");
        EditText scoreText=(EditText)findViewById(R.id.scoreText);
        Button scoreButton=(Button)findViewById(R.id.scoreButton);
//        new Thread(){
//            public void run(){
//                Message message=new Message();
//                message.what=ATHLETE_INFO;
//                handler.sendMessage(message);
//            }
//        }.start();
//        Intent intent=new Intent("com.example.anfj.refereescore.LOCAL_BROADCAST");
//        intent.putExtra("data","看见俺吗");
//        localBroadcastManager.sendBroadcast(intent);
        new Thread(){
            public void run() {
                try {
                    Log.d("LoginActivity","开始连接");
                    socket = MSocket.getsocket();
                    Log.d("LoginActivity","连接成功");
                    //in=new DataInputStream(socket.getInputStream());
                    out=new DataOutputStream(socket.getOutputStream());
                    while(true)
                    {
                        if(socketType==1)
                        {
                            if(socket.isConnected()){
                                if (!socket.isOutputShutdown()) {
//                                    out.write(sendInfo+"\n");
//                                    out.flush();
                                    out.writeUTF(sendInfo);
                                    Log.d("LoginActivity","副裁打分结果发送了");
                                }
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            socketType=0;
                            try{
                                Message message=new Message();
                                message.what=BUTTON_SEND;
                                handler.sendMessage(message);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();




        new Thread(){
            public void run() {
                try {
                    Log.d("LoginActivity","开始连接");
                    socket = MSocket.getsocket();
                    Log.d("LoginActivity","连接成功");
                    in=new DataInputStream(socket.getInputStream());
                    //out=new DataOutputStream(socket.getOutputStream());
                    while(true)
                    {
                            Log.d("LoginActivity","副裁接收信息中");
                            readInfo=in.readUTF();
                            Log.d("LoginActivity","副裁接收信息完成");
                            try{
                                readInfoCopy=readInfo;
                                JSONObject readJson=new JSONObject(readInfo);
                                if(readJson.getString("jsonId").equals("eventStart")){
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
                                    localBroadcastManager.sendBroadcast(intent);
                                    Log.d("LoginActivity","接收完毕");
                                }else if(readJson.getString("jsonId").equals("isRefereesOk")){
                                    Log.d("LoginActivity","接收isRefereesOk");
                                    if(readJson.getInt("refereeId")==refereeIdGoble){
                                        try{
                                            Message message=new Message();
                                            if(readJson.getBoolean("isScoreOk")==true){
                                                message.what=SCORE_SUCESS;
                                            }else {
                                                message.what=SCORE_FAILED;
                                            }
                                            handler.sendMessage(message);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
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
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText=(EditText)findViewById(R.id.scoreText);
                try{
                    JSONObject refereeScore=new JSONObject();
                    refereeScore.put("score",Integer.parseInt(editText.getText().toString()));
                    refereeScore.put("refereeId",refereeIdGoble);
                    refereeScore.put("jsonId","refereesScore");
                    sendInfo=refereeScore.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
                socketType=1;

//                new Thread(){
//                    public void run(){
//                        try{
//                            socket = MSocket.getsocket();
//
////                            out = new PrintWriter(new OutputStreamWriter(
////                                    socket.getOutputStream()));
//                            out=new DataOutputStream(socket.getOutputStream());
//                            if(socket.isConnected()){
//                                if (!socket.isOutputShutdown()) {
////                                    out.write(sendInfo+"\n");
////                                    out.flush();
//                                    out.writeUTF(sendInfo);
//                                }
//                            }
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
////                            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                            in=new DataInputStream(socket.getInputStream());
//                            readInfo="";
////                            readInfo=in.readLine();
//                            readInfo=in.readUTF();
//                            try{
//                                JSONObject readJson=new JSONObject(readInfo);
//                                Message message=new Message();
//                                if(readJson.getBoolean("isScoreOK")==true){
//                                    message.what=SCORE_SUCESS;
//                                }else {
//                                    message.what=SCORE_FAILED;
//                                }
//                                handler.sendMessage(message);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            socketType=0;
//                        }catch (IOException e)
//                        {
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

    @Override
    public void run() {
        try {
            while (true) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((readInfo = in.readUTF()) != null) {
                            handler.sendEmptyMessage(0x123);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class  LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
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
            //try解析jscon后的数据，setText()失效ORZ
//            try{
//                readInfoCopy=intent.getStringExtra("data");
//                JSONObject readJson=new JSONObject(readInfo);
//                getAtheletName=readJson.getString("athletesName");
//                getAtheletID=readJson.getString("athletesId");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            athleteName.setText(getAtheletName);
//            athleteID.setText(getAtheletID);
        }
    }
}
