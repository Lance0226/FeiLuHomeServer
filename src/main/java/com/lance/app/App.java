package com.lance.app;

import com.lance.datastructure.BudgetCategoryType;
import com.lance.datastructure.BudgetList;
import com.lance.datastructure.PlanList;
import com.lance.datastructure.PlanListType;
import com.lance.io.io;
import com.lance.json.*;
import com.lance.scrapy.*;
import com.lance.server.*;
import com.lance.xml.xmlUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class App 
{
 
    
    

	//private static Map<String, String> info;

	public static void main( String[] args ) throws IOException, SQLException
    {
		ScrapyPlanList planList=new ScrapyPlanList();
		planList.parse();
		
		/*
		SpoloSQL spoloSql=new SpoloSQL();
		List<PlanList> arrayPlanList=spoloSql.queryPlanList();
		String strNamePlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.NAME);
		String strPreviewURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.PREVIEW_URL);
		String strDetailURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.DETAIL_URL);
		String strPanoURLPlanlist=JsonUtil.PlanListToJson(arrayPlanList,PlanListType.PANO_URL);
		
        io.writeToFile("plan_name", "json","/usr/local/Cellar/tomcat/8.0.22/libexec/webapps/ROOT",strNamePlanlist);
        io.writeToFile("plan_preview_url", "json","/usr/local/Cellar/tomcat/8.0.22/libexec/webapps/ROOT",strPreviewURLPlanlist);
        io.writeToFile("plan_detail_url", "json","/usr/local/Cellar/tomcat/8.0.22/libexec/webapps/ROOT",strDetailURLPlanlist);
		io.writeToFile("plan_pano_url", "json","/usr/local/Cellar/tomcat/8.0.22/libexec/webapps/ROOT",strPanoURLPlanlist);
	    */
    }
}
