import java.sql.*;
import java.util.*;
import java.lang.*;

import org.json.JSONException;
import org.json.JSONObject;

public class conToDb {
    //====================================
    Connection con;
    String driver = "com.mysql.jdbc.Driver";
    String mysql_url = "jdbc:mysql://xxxxxxxxxx";
    String mysql_user = "xxxx";
    String mysql_password = "xxxxxxxx";
    Statement statement;
    ResultSet rs;
    String executeSql=null;
    //====================================

    conToDb(){
        try
        {
            Class.forName(driver);
            con = DriverManager.getConnection(mysql_url,mysql_user,mysql_password);
            if(!con.isClosed()) System.out.println("Succeeded connecting to the Database!");
            statement = con.createStatement();
        }

        catch(ClassNotFoundException e)
        {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            //数据库连接失败异常处理
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    int askForRefereeLogin(String userName,String userPd){
        int flag=0;
        try {
            //要执行的SQL语句
            executeSql = "SELECT referee_password FROM learn_team where referee_name = '"+userName+"'";    //从建立的login数据库的login——message表单读取数据
            rs = statement.executeQuery(executeSql);
            String name = null;
            String login_password = null;
            if (rs.next()){
                login_password = rs.getString("referee_password");
                if (userPd.equals(login_password)){
                    flag=3;
                }else{
                    flag=2;
                }
                //输出结果
                System.out.println(login_password);
            }else flag = 1;//没有用户
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    int askForRefereeNum(){
        int num=0;
        try {
            //要执行的SQL语句
            executeSql = "SELECT * FROM learn_team";//从表中查看裁判人数
            rs = statement.executeQuery(executeSql);
            while (rs.next()){
                num++;
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }

    void wirteBackToDb(JSONObject jsonObject){
        try {
            //要执行的SQL语句
            System.out.println("now write ok");
            executeSql = "UPDATE learn_person_first SET total_score="+jsonObject.getInt("totalScore")+" WHERE athletes_id= "+jsonObject.getString("athletesId");
            statement.executeUpdate(executeSql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void wirteBackToDb1(JSONObject jsonObject){
        try {
            //要执行的SQL语句
            System.out.println("now write ok");
            executeSql = "UPDATE learn_person_final SET total_score="+jsonObject.getInt("totalScore")+" WHERE athletes_id= "+jsonObject.getString("athletesId");
            statement.executeUpdate(executeSql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void  displayDb(boolean times){
        try {
            //要执行的SQL语句
            executeSql = "SELECT * FROM learn_person_first";//从表中输出结果
            if (times){
                rs = statement.executeQuery(executeSql);
                System.out.println("初赛表结果如下：");
                System.out.println("athletes_id | event_type | total_score | athletes_sex | team_type");
                while (rs.next()){
                    System.out.println(rs.getString("athletes_id")+" | "+rs.getString("event_type")+" | "+
                            rs.getInt("total_score")+" | "+rs.getInt("athletes_sex")+" | "+rs.getInt("team_type"));
                }
                executeSql="SELECT * FROM learn_person_final";
                rs = statement.executeQuery(executeSql);
                System.out.println("复赛名单如下：");
                System.out.println("athletes_id | event_type | total_score | athletes_sex | team_type");
                while (rs.next()){
                    System.out.println(rs.getString("athletes_id")+" | "+rs.getString("event_type")+" | "+
                            rs.getInt("total_score")+" | "+rs.getInt("athletes_sex")+" | "+rs.getInt("team_type"));
                }
            }else {
                executeSql="SELECT * FROM learn_person_final";
                rs = statement.executeQuery(executeSql);
                System.out.println("复赛结果如下：");
                System.out.println("athletes_id | event_type | total_score | athletes_sex | team_type");
                while (rs.next()){
                    System.out.println(rs.getString("athletes_id")+" | "+rs.getString("event_type")+" | "+
                            rs.getInt("total_score")+" | "+rs.getInt("athletes_sex")+" | "+rs.getInt("team_type"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String args[]){
        conToDb _con=new conToDb();
        _con.displayDb(true);

    }

    protected void destroy() {
        try{
            statement.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void finalize() throws java.lang.Throwable {
        destroy();
        // 递归调用超类中的finalize方法
        super.finalize();
    }
}
