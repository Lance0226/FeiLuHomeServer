package com.lance.scrapy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lance.datastructure.BudgetCategoryType;
import com.lance.datastructure.BudgetItemOne;
import com.lance.datastructure.BudgetItemThree;
import com.lance.datastructure.BudgetItemTwo;
import com.lance.datastructure.BudgetList;
import com.lance.datastructure.ItemTag;
import com.lance.server.SpoloSQL;

import net.sf.json.JSONObject;

public class ScrapyBudgetList 
{
  private              Map<String, String> projectInfo;
  private              Document            doc;
  private              List<Elements>      listBudgets;
  private              JSONObject          spoloJson;
  
  private              List<BudgetList>    arrBudgetList;
  
  private              List<String>        listItemTag;
  private              List<BudgetItemOne>    listBudgetItemOne;
  private              List<BudgetItemTwo>    listBudgetItemTwo;
  private              List<BudgetItemThree>  listBudgetItemThree;
  
  private static final String              urlPrefix="http://www.xuanran001.com";
	
  public ScrapyBudgetList(String strURL) throws IOException
  {
	  
	  this.projectInfo=new HashMap<String, String>();
	  
	  doc=Jsoup.connect(strURL).timeout(100000).get();  //设置10秒超时
	  this.listBudgets=new LinkedList<Elements>();
	  this.arrBudgetList=new LinkedList<BudgetList>();
	  
	  this.listItemTag=new LinkedList<String>();
	  this.listBudgetItemOne=new LinkedList<BudgetItemOne>();
	  this.listBudgetItemTwo=new LinkedList<BudgetItemTwo>();
	  this.listBudgetItemThree=new LinkedList<BudgetItemThree>();
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
      int project_id=0;//子项目id
     
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
	  
	  Elements subTitle0s=this.listBudgets.get(i).last().select("a[data-parent=#accordion-"+category_abr+"]");
	  for(Element subTitle0:subTitle0s)
	  {

		  String itemTagTemp=subTitle0.attr("href");
		  
		  
		  if(itemTagTemp!="")
		  {
			 project_id++;
			 String itemTag=itemTagTemp.substring(1, itemTagTemp.length());//去掉＃
			 this.listItemTag.add(itemTag);//添加标签
		     String arr_str[]=subTitle0.text().split(" ");                         //获得工程名称
		     String item_budget=null;
		     String item_name=null;
		  
		     item_name=arr_str[0]; 
		     item_budget=arr_str[1];
		  
		  
		     BudgetList budget_list=new BudgetList();
		     budget_list.plan_id=plan_id;
		     budget_list.project_id=project_id+"";
		     budget_list.item_id=itemTag;
		     budget_list.category=i;
		     budget_list.name=item_name;
		     budget_list.budget=item_budget;
		     this.arrBudgetList.add(budget_list);
		  }
		  else
		  {   String arr_str[]=subTitle0.text().split("：");                         //获得工程名称
		      String item_budget=null;
		      String item_name=null;
		      item_name=arr_str[0]; 
			  item_budget=arr_str[1];
		      
			  BudgetList budget_list=new BudgetList();
			  budget_list.plan_id=plan_id;
			  budget_list.project_id=project_id+"";
			  budget_list.item_id="";
			  budget_list.category=i;
			  budget_list.name=item_name;
			  budget_list.budget=item_budget;
			  this.arrBudgetList.add(budget_list);
		  }
	  }
		  
	
	  }
		  
		  
		 
		
		 
	  return this.arrBudgetList;
	 
	  
	  
  }
  
  public void parseBudgetItem()
  {
	  for(String item:this.listItemTag)
	  {
		  String category=item.substring(0,6);
		  System.out.println(category);
		  Element subItem_temp=this.doc.select("div[id="+item+"]").last().getElementsByTag("tbody").last();
		  if(subItem_temp!=null)
		  {
		  Elements subItems=subItem_temp.getElementsByTag("tr");
		  for(Element subItem:subItems )
		  {
			  
			 
			  Elements sub2Items=subItem.getElementsByTag("td");
			  if(category.equals("sgBill"))
			  {
			  BudgetItemOne budgetItemOne=new BudgetItemOne();
			  budgetItemOne.project_id=item;
			  budgetItemOne.item_name=subItem.getElementsByTag("th").last().text();
			  int i=0;
			  for(Element sub2Item:sub2Items)
			  {
				  i++;
				  switch (i) 
				  {
				    case 1:budgetItemOne.item_unit=sub2Item.text();break;
				    case 2:budgetItemOne.item_amount=sub2Item.text();break;
				    case 3:budgetItemOne.item_price=sub2Item.text();break;
				    case 4:budgetItemOne.item_total=sub2Item.text();break;
				    case 5:budgetItemOne.item_method=sub2Item.text();break;
				    default:System.out.println("Index eror");break;
				  }
			  }
			  this.listBudgetItemOne.add(budgetItemOne);
			  }
			  else if(category.equals("yzBill"))
			  {
				  BudgetItemTwo budgetItemTwo=new BudgetItemTwo();
				  budgetItemTwo.project_id=item;
				  budgetItemTwo.item_name=subItem.getElementsByTag("th").last().text();
				  int i=0;
				  for(Element sub2Item:sub2Items)
				  {
					  i++;
					  switch (i) 
					  {
					    case 1:budgetItemTwo.item_brand=sub2Item.text();break;
					    case 2:budgetItemTwo.item_code=sub2Item.text();break;
					    case 3:budgetItemTwo.item_unit=sub2Item.text();break;
					    case 4:budgetItemTwo.item_amount=sub2Item.text();break;
					    case 5:budgetItemTwo.item_price=sub2Item.text();break;
					    case 6:budgetItemTwo.item_total=sub2Item.text();break;
					    case 7:budgetItemTwo.item_address=sub2Item.text();break;
					    default:System.out.println("Index eror");break;
					  }
				  } 
				  this.listBudgetItemTwo.add(budgetItemTwo);
			  }
			  else if(category.equals("rzBill"))
			  {
				  BudgetItemThree budgetItemThree=new BudgetItemThree();
				  budgetItemThree.project_id=item;
				  budgetItemThree.item_category=subItem.getElementsByTag("th").last().text();
				  int i=0;
				  for(Element sub2Item:sub2Items)
				  {
					  i++;
					  switch (i) 
					  {
					    case 1:budgetItemThree.item_name=sub2Item.text();break;
					    case 2:budgetItemThree.item_brand=sub2Item.text();break;
					    case 3:budgetItemThree.item_code=sub2Item.text();break;
					    case 4:budgetItemThree.item_amount=sub2Item.text();break;
					    case 5:budgetItemThree.item_price=sub2Item.text();break;
					    case 6:budgetItemThree.item_total=sub2Item.text();break;
					    case 7:budgetItemThree.item_address=sub2Item.text();break;
					    default:System.out.println("Index eror");break;
					  }
				  } 
				  this.listBudgetItemThree.add(budgetItemThree);
			  }
			  else
			  {
				  System.out.println("Category error");
			  }
			  
			  
			  
				  
		  }
		  }
		 
	  }
  }
  
  
  public List<BudgetItemOne> getItemListOne()
  {
	  return this.listBudgetItemOne;
  }
  
  public List<BudgetItemTwo> getItemListTwo()
  {
	  return this.listBudgetItemTwo;
  }
  
  public List<BudgetItemThree> getItemListThree()
  {
	  return this.listBudgetItemThree;
  }
  
  
  private String turnSwfToHtmlURL(String pano_swf_url)  //将swf的url转变为html5的
  {
	  String pano_html5_url=pano_swf_url.substring(0, pano_swf_url.length()-13)+"/html5/output/0000.html";
	  return pano_html5_url;
  }
}