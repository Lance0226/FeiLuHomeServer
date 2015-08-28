package com.lance.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lance.datastructure.BudgetCategoryType;
import com.lance.datastructure.PlanList;
import com.mysql.jdbc.Statement;


public class SpoloSQL 
{
  public Connection con;//定义一个mysql链接对象
	
  public SpoloSQL()
  {
	  try 
	  {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/spolo","root","19880226");//链接本地mysql
		
	  } 
	  catch (Exception e) 
	  {
		System.out.println("MYSQL ERROR"+e.getMessage());
	  }
  }
  
  public void insertToPlanList(int id,String preview_url,String detail_url,String pano_url) throws SQLException
  {
	  Statement stmt;
	  ResultSet res;
	  stmt=(Statement) con.createStatement();
	  String sql="INSERT INTO spolo_plan_list(id,name,preview_url,detail_url,pano_url) VALUES('"+id+"','方案"+id+"','"+preview_url+"','"+detail_url+"','"+pano_url+"');";
	  System.out.println(sql);
	  stmt.executeUpdate(sql);
	  res=stmt.executeQuery("select LAST_INSERT_ID()");
	  int ret_id;
	  if(res.next())
	  {
		ret_id=res.getInt(1);
		System.out.println(ret_id);
	  
	  }
  }
  
  
  public int getPlanFinalId() throws SQLException
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
	  
	  return id;
  }
  
  public int getBudgetFinalId() throws SQLException
  {
	  Statement stmt;
	  ResultSet res;
	  stmt=(Statement) con.createStatement();
	  int id=0;
	  res=stmt.executeQuery("select max(id)as id from spolo_budget_list_levelone");
	  if(res.next())
	  {
		  id=res.getInt("id")+1;
	  }
	  else
	  {
		  id=0;
	  }
	  
	  return id;
  }
  
  
  public List<PlanList> query() throws SQLException
  {
     Statement stmt;
     ResultSet res;
     stmt=(Statement) con.createStatement();
     res=stmt.executeQuery("select * from spolo_plan_list limit 0,5;");
     int      id=0;
     String name=null;
     String preview_url=null;
     String detail_url=null;
     String pano_url=null;
     
     List<PlanList> arrayPlanList=new ArrayList<PlanList>();
     while(res.next())
     {
    	 id=res.getInt("id");
    	 name=res.getString("name");
    	 preview_url=res.getString("preview_url");
    	 detail_url=res.getString("detail_url");
    	 pano_url=res.getString("pano_url");
    	 

     PlanList plan_list=new PlanList();
     plan_list.id=id;
     plan_list.name=name;
     plan_list.preveiew_url=preview_url;
     plan_list.detail_url=detail_url;
     plan_list.pano_url=pano_url;
     
     
     arrayPlanList.add(plan_list);
     
     }
     return arrayPlanList;
     
     
  }
  
  
  public void insertToBudgetLevelOneList(int id,int plan_id,int category,String name,String budget ) throws SQLException //插入一级预算表
  {
	  Statement stmt;
	  ResultSet res;
	  stmt=(Statement) con.createStatement();
	  
	  
	  String sql="INSERT INTO spolo_budget_list_levelone(id,plan_id,category,name,budget) VALUES('"+id+"','"+plan_id+"','"+category+"','"+name+"','"+budget+"')";
	  System.out.println(sql);
	  stmt.executeUpdate(sql);
	  res=stmt.executeQuery("select LAST_INSERT_ID()");
	  int ret_id;
	  if(res.next())
	  {
		ret_id=res.getInt(1);
		System.out.println(ret_id);
	  
	  }
  }
}
