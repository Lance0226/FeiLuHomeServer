package com.lance.json;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  com.lance.server.*;

import net.sf.json.JSONObject;

public  class JsonUtil 
{
   public static String PlanListToJson(List<PlanList> arrayPlanList,PlanListType type)
   {
	   
	   JSONObject jsonPlanList;
	   Map<String, String> mapPLanList=new HashMap<String, String>();
	   
	   for(int i=0;i<arrayPlanList.size();i++)
	   {
	     switch (type) 
	     {
		case NAME:        mapPLanList.put(""+arrayPlanList.get(i).id,arrayPlanList.get(i).name);         break;
		case PREVIEW_URL: mapPLanList.put(""+arrayPlanList.get(i).id,arrayPlanList.get(i).preveiew_url); break;
		case DETAIL_URL:  mapPLanList.put(""+arrayPlanList.get(i).id,arrayPlanList.get(i).detail_url);   break;
		case PANO_URL:    mapPLanList.put(""+arrayPlanList.get(i).id,arrayPlanList.get(i).pano_url);     break;

		default:          System.out.println("Enum type error");                                         break;
		}
	     
		
	     
	   }
	   jsonPlanList=JSONObject.fromObject(mapPLanList);
	   System.out.println(jsonPlanList.toString());
	   return jsonPlanList.toString();
   }
}
