package com.example.anfj.refereescore;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.*;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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


public class LoginActivity extends AppCompatActivity  {
    private Socket socket = null;
    //private BufferedReader in = null;
    //private PrintWriter out = null;
    private DataInputStream in=null;
    private DataOutputStream out=null;
    private String readInfo = "";
    private StringBuilder sb = null;
    private int refereeId=0;
    private boolean login_result=false;
    private boolean isMainReferee=false;
    //定义一个handler对象,用来刷新界面
    public static final int LOGIN_SUCSSESS=1;
    public static final int LOGIN_FAILED=0;
    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            try{
                JSONObject readJson=new JSONObject(readInfo);
                switch (msg.what){
                    case LOGIN_FAILED:
                        Toast.makeText(LoginActivity.this,"登录失败，请重新登录",Toast.LENGTH_SHORT).show();
                        break;
                    case LOGIN_SUCSSESS:
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        if(readJson.getBoolean("isMainReferee")){
                            Intent intent=new Intent(LoginActivity.this,MainReferee.class);
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(LoginActivity.this,SecondReferee.class);
                            intent.putExtra("referee_id",refereeId);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();//隐藏默认标题栏
        if(actionBar!=null){
            actionBar.hide();
        }
        final EditText loginAccount=(EditText)findViewById(R.id.login_account);
        final EditText loginPassword=(EditText)findViewById(R.id.login_password);
        Button loginButton=(Button)findViewById(R.id.login_button);
        Log.d("LoginActivity","开始");
        final JSONObject tryLogin=new JSONObject();
        new Thread() {

            public void run() {
                try {
                    Log.d("LoginActivity","开始连接");
                    socket = MSocket.getsocket();
                    Log.d("LoginActivity","连接成功");
//                    in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                    out = new PrintWriter(new OutputStreamWriter(
//                            socket.getOutputStream()));
//                    readInfo=in.readLine();
//                    Message message=new Message();
//                    message.what=LOGIN_SUCSSESS;
//                    handler.sendMessage(message);
//                    out.write("wocaonima");
//                    out.flush();
//                    out.close();
                    //socket.shutdownOutput();//关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    tryLogin.put("refereeName",loginAccount.getText().toString());
                    tryLogin.put("refereePw",loginPassword.getText().toString());
                    tryLogin.put("jsonId","tryLogin");
                    final String jsonString=tryLogin.toString();
                    Toast.makeText(LoginActivity.this,jsonString,Toast.LENGTH_SHORT).show();
                    //发送socket数据
                    new Thread() {

                        public void run() {
                            try {
                                socket = MSocket.getsocket();
                                Log.d("LoginActivity","连接成功");
                                in=new DataInputStream(socket.getInputStream());
                                out=new DataOutputStream(socket.getOutputStream());
                                //in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
//                                out = new PrintWriter(new OutputStreamWriter(
//                                        socket.getOutputStream()));
                                if(socket.isConnected()){
                                    if (!socket.isOutputShutdown()) {
                                        Log.d("LoginActivity","开始发送");
                                        //out.write(jsonString+"\n");
//                                        out.flush();
                                        //out.close();
                                        out.writeUTF(jsonString);
                                        Log.d("LoginActivity","发送完毕");
                                        try{
                                            Message message=new Message();
                                            //readInfo=in.readLine(f);
                                            readInfo=in.readUTF();
                                            Log.d("LoginActivity","登录界面接收数据成功"+readInfo);
                                            JSONObject readJson=new JSONObject(readInfo);
                                            refereeId=readJson.getInt("refereeId");
                                            Log.d("LoginActivity","refereeId:"+refereeId);
                                            if(readJson.getBoolean("login_result")==true){
                                                message.what=LOGIN_SUCSSESS;
                                            }else {
                                                message.what=LOGIN_FAILED;
                                            }
                                            handler.sendMessage(message);
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
