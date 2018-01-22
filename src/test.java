import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class test {
    public  static void main(String args[]){
        try {
            Socket socket=new Socket("192.168.43.130", 8001);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonId","tryLogin");
            jsonObject.put("refereeName","Bob");
            jsonObject.put("refereePw","123");
            DataInputStream in=new DataInputStream(socket.getInputStream());
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            out.writeUTF(jsonObject.toString());
            //登录结果+运动员信息
            JSONObject jsonObject_r = new JSONObject(in.readUTF());
            jsonObject_r = new JSONObject(in.readUTF());
            //
            jsonObject = new JSONObject();
            jsonObject.put("jsonId","display");
            out.writeUTF(jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
