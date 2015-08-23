package com.lance.server;
import java.sql.Connection;
import java.sql.DriverManager;


public class Server 
{
  public Server()
  {
	  try 
	  {
		Connection con=null;//定义一个mysql链接对象
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/spolo","root","19880226");//链接本地mysql
		System.out.println("yes");
		
	  } 
	  catch (Exception e) 
	  {
		System.out.println("MYSQL ERROR"+e.getMessage());
	  }
  }
}
