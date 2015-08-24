package com.lance.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;


public class Server 
{
  public Connection con;//定义一个mysql链接对象
	
  public Server()
  {
	  try 
	  {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/spolo","root","19880226");//链接本地mysql
		System.out.println("yes");
		
	  } 
	  catch (Exception e) 
	  {
		System.out.println("MYSQL ERROR"+e.getMessage());
	  }
  }
  
  public void insert(String url) throws SQLException
  {
	  Statement stmt;
	  ResultSet res;
	  stmt=(Statement) con.createStatement();
	  int id=0;
	  res=stmt.executeQuery("select max(id)as id from spolo_plan_list");
	  if(res.next())
	  {
		  id=res.getInt("id")+1;
	  }
	  else
	  {
		  id=0;
	  }
	  stmt.executeUpdate("INSERT INTO spolo_plan_list(id,name,url) VALUES('"+id+"','方案','"+url+"')");
	  res=stmt.executeQuery("select LAST_INSERT_ID()");
	  int ret_id;
	  if(res.next())
	  {
		ret_id=res.getInt(1);
		System.out.println(ret_id);
	  
	  }
  }
  
  public PlanList query(int id) throws SQLException
  {
     Statement stmt;
     ResultSet res;
     stmt=(Statement) con.createStatement();
     res=stmt.executeQuery("select * from spolo_plan_list where id='"+id+"';");
     String name=null;
     String url=null;
     if(res.next())
     {
    	 name=res.getString("name");
    	 url=res.getString("url");
     }
     PlanList plan_list=new PlanList();
     plan_list.id=id;
     plan_list.name=name;
     plan_list.url=url;
     
     return plan_list;
     
     
  }
}
