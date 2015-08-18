package com.lance.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parse 
{
  private static Map<String, String> projectInfo;
  private        Document            doc;
  private        List<Elements>      listBudgets;
	
  public Parse(String strURL) throws IOException
  {
	  this.projectInfo=new HashMap<String, String>();
	  doc=Jsoup.connect(strURL).get();
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
  
  private List<Elements> GetBudgets()
  {
	  Elements budgets1=doc.select("section[id=spolo-sgBill]");
	  Elements budgets2=doc.select("section[id=spolo-yzBill]");
	  Elements budgets3=doc.select("section[id=spolo-rzBill]");
	  
	  this.listBudgets.add(budgets1);
	  this.listBudgets.add(budgets2);
	  this.listBudgets.add(budgets3);
	  
	  return this.listBudgets;
  }

  public void GetBudget()
  {
	  Elements budgets1=doc.select("section[id=spolo-sgBill]");
	  System.out.println(budgets1.last().getElementsByClass("spolo-title").text());
	  
	  Elements subTitle1s=budgets1.last().select("a[data-parent=#accordion-sgBill]");
	  for(Element subTitle1:subTitle1s)
	  {
		  System.out.println(subTitle1.text()+"\n");
	  }
	  
  }
}