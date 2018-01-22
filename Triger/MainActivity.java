package com.example.wangshuai.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class MainActivity extends AppCompatActivity {
    public static final String ADD_URL = "http://192.168.2.1:16000/refereeServers";
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    int num1=0,num2=0;

    //public static DBConnector c=new DBConnector();
    //public static PreparedStatement preparedStatement=null;
    //public static Connection connection=null;
    //public static ResultSet r
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        num1++;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://xxxxxxxxx", "xxxxxx", "xxxxxx");
                            Log.e("MainActivity", "true1");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            final String athletes_name, id, event_type;
                            final int team_type, athletes_sex;
                            preparedStatement = connection.prepareStatement("SELECT * FROM learn_Athlete WHERE athletes_name IS NOT null");
                            rs = preparedStatement.executeQuery();
                            Log.e("MainActivity", "false");
                            for(int i=1;i<num1;i++)
                                rs.next();
                            if (rs.next()) {
                                athletes_name = rs.getString(2);
                                id = rs.getString(8);
                                team_type = rs.getInt(5);
                                event_type = rs.getString(6);
                                athletes_sex = rs.getInt(7);
                                rs.next();
                                Start S=new Start(athletes_name, id,event_type, team_type, athletes_sex);
                            } else System.out.println("error");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int n=0;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://120.77.35.18:3306/ComSysDesign", "root", "jiaoma");
                            Log.e("MainActivity", "true1");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            Log.d("MainActivity", "true");
                            preparedStatement = connection.prepareStatement("SELECT * FROM learn_person_first ORDER BY event_type,athletes_sex,team_type,total_score DESC");
                            rs = preparedStatement.executeQuery();
                            preparedStatement=connection.prepareStatement("DELETE FROM learn_person_first");
                            preparedStatement.execute();
                            while(rs.next()) {
                                n++;
                                preparedStatement = connection.prepareStatement("INSERT INTO learn_person_first VALUES(?,?,?,?,?,?,?)");
                                preparedStatement.setInt(1,n);
                                String s1 = rs.getString(2);
                                preparedStatement.setString(2, rs.getString(2));
                                String s2 = rs.getString(3);
                                preparedStatement.setString(3, rs.getString(3));
                                preparedStatement.setInt(4, rs.getInt(4));
                                int n1 = rs.getInt(5);
                                preparedStatement.setInt(5, rs.getInt(5));
                                int n2 = rs.getInt(6);
                                preparedStatement.setInt(6, rs.getInt(6));
                                String s3=rs.getString(7);
                                preparedStatement.setString(7,rs.getString(7));
                                preparedStatement.execute();
                            }
                            rs.first();
                            rs.previous();
                            while (rs.next()) {
                                n++;
                                if(n%3!=0) {
                                    preparedStatement = connection.prepareStatement("INSERT INTO learn_person_final VALUES(?,?,?,?,?,?,?)");
                                    preparedStatement.setInt(1,n);
                                    String s1 = rs.getString(2);
                                    preparedStatement.setString(2, rs.getString(2));
                                    String s2 = rs.getString(3);
                                    preparedStatement.setString(3, rs.getString(3));
                                    preparedStatement.setInt(4, 0);
                                    int n1 = rs.getInt(5);
                                    preparedStatement.setInt(5, rs.getInt(5));
                                    int n2 = rs.getInt(6);
                                    preparedStatement.setInt(6, rs.getInt(6));
                                    preparedStatement.setString(7,rs.getString(7));
                                    preparedStatement.execute();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        /*查数据；
                        断开数据库；
                        发JSON;*/
                    }
                }).start();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        num2++;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://120.77.35.18:3306/ComSysDesign", "root", "jiaoma");
                            Log.e("MainActivity", "true1");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        Log.e("MainActivity", "true2");
                        try {
                            String athletes_name, id, event_type;
                            int team_type, athletes_sex;
                            preparedStatement = connection.prepareStatement(" SELECT * FROM learn_person_final");
                            rs = preparedStatement.executeQuery();
                            for(int i=1;i<num2;i++)
                                rs.next();
                            if (rs.next()) {
                                athletes_name = rs.getString(7);
                                id = rs.getString(2);
                                team_type = rs.getInt(6);
                                event_type = rs.getString(3);
                                athletes_sex = rs.getInt(5);
                                rs.next();
                                if(num2%3!=0) {
                                    Start S = new Start(athletes_name, id, event_type, team_type, athletes_sex);
                                }
                                System.out.println("true");
                            } else {
                                System.out.println("error");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            connection = DriverManager.getConnection("jdbc:mysql://120.77.35.18:3306/ComSysDesign", "root", "jiaoma");
                            Log.e("MainActivity", "true1");
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                            Display D = new Display();
                    }
                }).start();
            }
        });



    }
}

