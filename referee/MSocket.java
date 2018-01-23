package com.example.anfj.refereescore;
import java.io.*;
import java.net.*;
/**
 * Created by AnFJ on 2017/12/1.
 */

public class MSocket extends Socket
{
   // private static final String host="10.0.2.2";
    //private static final int port=7100;
    private static final String host="192.168.43.130";
    private static final int port=8001;
    private static BufferedReader br = null;
    private static PrintWriter pw = null;
    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
    private static MSocket socket=null;

    private MSocket(String host, int port) throws UnknownHostException, IOException {
        super(host, port);
    }

    public static MSocket getsocket() throws IOException {
        if(socket==null){
            socket= new MSocket(host,port);
        }
        return  socket;
    }

    public static BufferedReader getbr() throws IOException {
        if(br==null){
            br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
        }
        return br;
    }

    public static PrintWriter getpw() throws IOException {
        if(pw==null){
            pw=new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true
            );
        }
        return pw;
    }
}
