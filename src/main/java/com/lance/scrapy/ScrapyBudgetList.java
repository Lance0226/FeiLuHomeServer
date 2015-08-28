package com.lance.scrapy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lance.datastructure.BudgetCategoryType;
import com.lance.datastructure.BudgetList;
import com.lance.server.SpoloSQL;

import net.sf.json.JSONObject;

public class ScrapyBudgetList 
{
  private              Map<String, String> projectInfo;
  private              Document            doc;
  private              List<Elements>      listBudgets;
  private              JSONObject          spoloJson;
  
  private              List<BudgetList>    arrBudgetList;
  
  private static final String              urlPrefix="http://www.xuanran001.com";
	
  public ScrapyBudgetList(String strURL) throws IOException
  {
	  
	  this.projectInfo=new HashMap<String, String>();
	  
	  doc=Jsoup.connect(strURL).timeout(100000).get();  //设置10秒超时
	  this.listBudgets=new LinkedList<Elements>();
	  this.arrBudgetList=new LinkedList<BudgetList>();
	  GetBudgets();
  }
  
  public Map<String,String> GetProInfo()
  {
	  //分区名
	  //String sectionName=doc.select("section[id=spolo-proInfo]").last().select("h3[class=spolo-title]").last().text();
      Elements str1=doc.getElementsByClass("col-md-6");
      for(Element str : str1)
      {
      	Elements nodes1=str.getElementsByTag("dt");
      	Elements nodes2=str.getElementsByTag("dd");
      
      	
      	for(int i=0;i<nodes1.size();i++)
      	{
      		//去掉冒号
      		this.projectInfo.put(nodes1.get(i).text().substring(0, nodes1.get(i).text().length()-1), nodes2.get(i).text());
      		
      	}
      	
      }
     
        this.spoloJson=JSONObject.fromObject(this.projectInfo);
		System.out.println(this.spoloJson.toString());
      	return this.projectInfo;
  }
  
  public void GetImg()
  {
	  Elements imgs=doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
	  int i=0;
	  for (Element image : imgs) 
	  {
          
		  if(image.attr("src").contains("preview"))
		  {
		  i++;
		  String image_url=this.urlPrefix+image.attr("src");
		  this.projectInfo.put("preview"+i,image_url);
		  }

	  }
	  
	  
	  int t=0;
	  for (Element image : imgs) 
	  {
         
		  if(image.attr("src").contains("renderinfo"))
		  {
			t++;
		    //System.out.println("http://www.xuanran001.com" + image.attr("src")+"\n");
		    String render_info_url=this.urlPrefix+image.attr("src");
		    this.projectInfo.put("renderinfo"+t, render_info_url);
		  }

	  }
	  
	  this.spoloJson=JSONObject.fromObject(this.projectInfo);
	  System.out.println(this.spoloJson.toString());
	  
  }
  
  public String getPano()
  {
	  Elements panos=doc.select("div[data-swf~=(?i)\\.(swf)]");
	  String pano_swf_url=panos.last().attr("data-swf");
      String pano_html5_url=this.urlPrefix+turnSwfToHtmlURL(pano_swf_url);
	  
      return pano_html5_url; 
  }
  
  private void GetBudgets()
  {
	  Elements budgets1=doc.select("section[id=spolo-sgBill]");
	  Elements budgets2=doc.select("section[id=spolo-yzBill]");
	  Elements budgets3=doc.select("section[id=spolo-rzBill]");
	  
	  this.listBudgets.add(budgets1);
	  this.listBudgets.add(budgets2);
	  this.listBudgets.add(budgets3);
	  
  }

  public List<BudgetList> GetBudget(int plan_id) throws SQLException
  {   String category_abr=null;
      SpoloSQL spoloSQL=new SpoloSQL();
      int final_id=spoloSQL.getBudgetFinalId();    //查询最后一个id
      int item_id=final_id;                     //设置起始id
     
	  for(int i=0;i<3;i++)  //三部分循环抓取
	  {
		  

		  switch (i) 
		  {
		    case 0:category_abr="sgBill";break;
		    case 1:category_abr="yzBill";break;
		    case 2:category_abr="rzBill";break;
		    default:System.out.println("Index error");
			break;
		  }
	  
	  Elements subTitle0s=this.listBudgets.get(i).last().select("a[href^=#"+category_abr+"]");
	  for(Element subTitle0:subTitle0s)
	  {

			  
		  item_id++;	  
		  String arr_str[]=subTitle0.text().split(" ");                         //获得工程名称
		  String item_budget=null;
		  String item_name=null;
		  
		  item_name=arr_str[0]; //去除工程两个字
		  item_budget=arr_str[1];
		  
		  
		  System.out.println(item_name);
		  System.out.println(item_budget);
		  BudgetList budget_list=new BudgetList();
		  budget_list.id=item_id;
		  budget_list.plan_id=plan_id;
		  budget_list.category=i;
		  budget_list.name=item_name;
		  budget_list.budget=item_budget;
		  
		  this.arrBudgetList.add(budget_list);
	     }
	  }
		  
		  
		  //server.insertToBudgetLevelOneList(plan_id, category, project_name, project_budget);
		
		  /*
		  Element temp=this.listBudgets.get(index).last().select("div[id="+abr+"-"+project_name+"]").last();
		  Elements node2s=null;
		  if(temp!=null)
		  {
		    node2s=temp.getElementsByTag("tr");//装修项目
		  }
		  
		  if(node2s!=null)
		  {
		  for(Element node2:node2s)
		  {
			  Elements node3_1s=node2.select("th"); //项目的具体选项
			  if(node3_1s!=null)
			  {
		         for (Element node3:node3_1s)
		         {
		    	   System.out.println(node3.text()+"\n");
		         }
			  }
		      
		      Elements node3_2s=node2.select("td");
		      if(node3_2s!=null)
		      {
		    	  for(Element node3:node3_2s)
		    	  {
		    		System.out.println(node3.text()+"\n");  
		    	  }
		      }
		    
		  }
		  }
		  */
	  return this.arrBudgetList;
	 
	  
	  
  }
  
  private String turnSwfToHtmlURL(String pano_swf_url)  //将swf的url转变为html5的
  {
	  String pano_html5_url=pano_swf_url.substring(0, pano_swf_url.length()-13)+"/html5/output/0000.html";
	  return pano_html5_url;
  }
}