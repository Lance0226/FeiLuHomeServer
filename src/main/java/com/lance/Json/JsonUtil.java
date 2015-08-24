package com.lance.Json;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  com.lance.server.*;

import net.sf.json.JSONObject;

public  class JsonUtil 
{
   public static String PlanListToJson(PlanList planList)
   {
	   
	   JSONObject jsonPlanList;
	   Map<String, String> mapPLanList=new HashMap<String, String>();
	   mapPLanList.put(""+planList.id, "{"+planList.name+","+planList.url+"}");
	   jsonPlanList=JSONObject.fromObject(mapPLanList);
	   System.out.println(jsonPlanList.toString());
	   return jsonPlanList.toString();
	   
   }
}
