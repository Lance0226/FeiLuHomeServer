package com.lance.Json;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  com.lance.server.*;

import net.sf.json.JSONObject;

public  class JsonUtil 
{
   public static String PlanListToJson(List<PlanList> arrayPlanList)
   {
	   
	   JSONObject jsonPlanList;
	   Map<String, String> mapPLanList=new HashMap<String, String>();
	   
	   for(int i=0;i<arrayPlanList.size();i++)
	   {
	     
	     mapPLanList.put(""+arrayPlanList.get(i).id, "{"+arrayPlanList.get(i).name+","+arrayPlanList.get(i).url+"}");
	     
	   }
	   jsonPlanList=JSONObject.fromObject(mapPLanList);
	   System.out.println(jsonPlanList.toString());
	   return jsonPlanList.toString();
   }
}
