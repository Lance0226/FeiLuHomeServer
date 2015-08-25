package com.lance.app;

import com.lance.io.io;
import com.lance.json.*;
import com.lance.parse.*;
import com.lance.scrapy.*;
import com.lance.server.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
		List<PlanList> arrayPlanList=server.query();
		String strNamePlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.NAME);
		String strPreviewURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.PREVIEW_URL);
		String strDetailURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.DETAIL_URL);
		
		
        io.writeToFile("plan_name", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strNamePlanlist);
        io.writeToFile("plan_preview_url", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strPreviewURLPlanlist);
        io.writeToFile("plan_detail_url", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strDetailURLPlanlist);
        
        
    }
}
