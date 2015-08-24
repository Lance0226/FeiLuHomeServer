package com.lance.app;

import com.lance.Json.JsonUtil;
import com.lance.parse.*;
import com.lance.scrapy.*;
import com.lance.server.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
 
    
    

	//private static Map<String, String> info;

	public static void main( String[] args ) throws IOException, SQLException
    {
		//Parse parse=new Parse("http://www.xuanran001.com/rwrule/zxfazzjfb3/409a55a7-86ae-4db4-99f8-fdbe94f30bef.html");
		//parse.GetImg();
		//info = new HashMap<String, String>();
		//info=parse.GetProInfo();
		//parse.GetBudget(0,"sgBill");
		//parse.GetBudget(1,"yzBill");
		//parse.GetBudget(2, "rzBill");
        	
		//Scrapy scrapy=new Scrapy();
		//scrapy.Parse();
		
		Server server=new Server();
		PlanList planlist=server.query(1);
		System.out.println(planlist.url);
		String strPlanlist=JsonUtil.PlanListToJson(planlist);
        
    }
}
