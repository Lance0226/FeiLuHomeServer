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
		//Parse parse=new Parse("http://www.xuanran001.com/rwrule/zxfazzjfb3/b1b7b92e-1e40-4936-bd1c-2fd0423fe3a5.html");
		//parse.GetPano();
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
		String strPanoURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.PANO_URL);
		
        io.writeToFile("plan_name", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strNamePlanlist);
        io.writeToFile("plan_preview_url", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strPreviewURLPlanlist);
        io.writeToFile("plan_detail_url", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strDetailURLPlanlist);
		io.writeToFile("plan_pano_url", "json","/usr/local/Cellar/tomcat/8.0.23/libexec/webapps/ROOT",strPanoURLPlanlist);
    }
}
