package com.lance.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parse 
{
  private        Map<String, String> projectInfo;
  private        Document            doc;
  private        List<Elements>      listBudgets;
	
  public Parse(String strURL) throws IOException
  {
	  this.projectInfo=new HashMap<String, String>();
	  doc=Jsoup.connect(strURL).timeout(100000).get();  //设置10秒超时
	  this.listBudgets=new LinkedList<Elements>();
	  GetBudgets();
  }
  
  public Map<String,String> GetInfo()
  {
	 
      Elements str1=doc.getElementsByClass("col-md-6");
      for(Element str : str1)
      {
      	Elements nodes1=str.getElementsByTag("dt");
      	Elements nodes2=str.getElementsByTag("dd");
      
      	
      	for(int i=0;i<nodes1.size();i++)
      	{
      		projectInfo.put(nodes1.get(i).text(), nodes2.get(i).text());
      		System.out.println(nodes1.get(i).text());
      		System.out.println(nodes2.get(i).text());
      	}
      }
      	return this.projectInfo;
  }
  
  public void GetImg()
  {
	  Elements imgs=doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
	  for (Element image : imgs) 
	  {

		  if(image.attr("src").contains("preview"))
		  {
		  System.out.println("http://www.xuanran001.com" + image.attr("src")+"\n");
		  }

	  }
	  
	  for (Element image : imgs) 
	  {

		  if(image.attr("src").contains("renderinfo"))
		  {
		  System.out.println("http://www.xuanran001.com" + image.attr("src")+"\n");
		  }

	  }
	  
  }
  
  public void GetPano()
  {
	  Elements panos=doc.select("div[data-swf~=(?i)\\.(swf)]");
	  System.out.println("http://www.xuanran001.com"+panos.last().attr("data-swf")+"\n");
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

  public void GetBudget(int index,String abr)
  {

	  //家装基础施工预算
	  System.out.println(this.listBudgets.get(index).last().getElementsByClass("spolo-title").text()+"\n");
	  
	  Elements subTitle0s=this.listBudgets.get(index).last().select("a[data-parent=#accordion-"+abr+"]");
	  for(Element subTitle0:subTitle0s)
	  {
		  String arr_str[]=subTitle0.text().split(" ");
		  System.out.println(arr_str[0]+"\n");                                 //获得工程名称
		  String project_name=arr_str[0].substring(0, arr_str[0].length()-2);  //去除工程两个字
		  System.out.println(project_name);    
		  System.out.println(arr_str[1]+"\n");
		  
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
	  }
	  
	  
  }
}