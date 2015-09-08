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
  private              List<Elements>      listBudgetsElements;//子项目分类Elements列表
  private              JSONObject          spoloJson;
  private              String              plan_id;          //查询的最后一条plan数据库id
  
  private              List<BudgetList>    arrBudgetList;
  
  private              List<Elements>           listBudgetLvOneElements; //一级预算信息elements列表
  private              List<String>           listCategoryAbr;  //子项目分类标签前缀字符串列表
  private              List<String>           listItemTag;
  private              List<BudgetItemOne>    listBudgetItemOne;
  private              List<BudgetItemTwo>    listBudgetItemTwo;
  private              List<BudgetItemThree>  listBudgetItemThree;
  
  private static final String              urlPrefix="http://www.xuanran001.com";
	
  public ScrapyBudgetList(String strURL,int plan_id) throws IOException
  {
	  
	  this.projectInfo=new HashMap<String, String>();
	  
	  doc=Jsoup.connect(strURL).timeout(100000).get();  //设置10秒超时
	  this.listBudgetsElements=new LinkedList<Elements>();
	  this.arrBudgetList=new LinkedList<BudgetList>();
	  
	  this.listCategoryAbr=new LinkedList<String>();//初始化子项目分类标签前缀字符串列表
	  this.listBudgetLvOneElements=new LinkedList<Elements>();//初始化一级预算信息elements列表
	  this.listItemTag=new LinkedList<String>();
	  this.listBudgetItemOne=new LinkedList<BudgetItemOne>();
	  this.listBudgetItemTwo=new LinkedList<BudgetItemTwo>();
	  this.listBudgetItemThree=new LinkedList<BudgetItemThree>();
	  InitCategoryAbr();
	  InitBudgetsElements();
	  InitBudgetLvOneElements();
	  InitBudgetList(plan_id);
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
  
  public String getPano()  //获取全景url
  {
	  Elements panos=doc.select("div[data-swf~=(?i)\\.(swf)]");
	  String pano_swf_url=panos.last().attr("data-swf");
      String pano_html5_url=this.urlPrefix+turnSwfToHtmlURL(pano_swf_url);
      
      System.out.println(pano_html5_url);
	  
      return pano_html5_url; 
  }

  public List<BudgetList> getArrayBuddgetList()   //获取第一级数据节点的数据结构列表
  {
	  System.out.println(this.arrBudgetList.get(0).name);
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
  
  private void InitCategoryAbr() //将sgBill,yzBill,rzBill三个字符串放入全局队列，便于之后循环
  {
	  this.listCategoryAbr.add("sgBill");
	  this.listCategoryAbr.add("yzBill");
	  this.listCategoryAbr.add("rzBill");
  }
  
  private void InitBudgetsElements()   //获取子页面sg,yz,rz三大分块的Elements
  {
	  for(int i=0;i<3;i++)
	  {
		  Elements budgets=doc.select("section[id=spolo-"+this.listCategoryAbr.get(i)+"]");
		  this.listBudgetsElements.add(budgets);	  
	  }
	  
  }
  
  private void InitBudgetLvOneElements()  //获取第一级预算信息Elements
  {
	  for (int i=0;i<3;i++)
	  {
		  Elements budgetLvOneElements=this.listBudgetsElements.get(i)
				  .last().select("a[data-parent=#accordion-"+this.listCategoryAbr.get(i)+"]");
		  this.listBudgetLvOneElements.add(budgetLvOneElements);
		  
	  }
  }
  
  private void InitBudgetList(int plan_id) //生成一级子项标签列表，如yzBill阳台
  {   int project_id=0;
      int category=0;
	  for(Elements budgetLvOneElements:this.listBudgetLvOneElements)
      {
		  category++;
		  for(Element budgetLvOneElement:budgetLvOneElements)
		  {
			  String itemTagTemp=budgetLvOneElement.attr("href");
			  if(itemTagTemp!="")
			  {
				  project_id=project_id+1;
				  String item_id=itemTagTemp.substring(1, itemTagTemp.length());//去掉＃
				  //System.out.println(item_id);
				  this.listItemTag.add(item_id);//添加标签
				  
				  String arr_str[]=budgetLvOneElement.text().split(" ");                         //获得工程名称
				  String item_budget=null;
				  String item_name=null;
				  item_name=arr_str[0]; 
				  item_budget=arr_str[1];
				  BudgetList budget_list=InitBudgetLvOneNode(plan_id, project_id+"",item_id, category, item_name, item_budget);
				  this.arrBudgetList.add(budget_list);
			  }
			  else
			  {
				  String arr_str[]=budgetLvOneElement.text().split("：");                         //获得工程名称
			      String item_budget=null;
			      String item_name=null;
			      item_name=arr_str[0]; 
				  item_budget=arr_str[1];
				  BudgetList budget_list=InitBudgetLvOneNode(plan_id, project_id+"","", category, item_name, item_budget);
				  this.arrBudgetList.add(budget_list);
			  }
		  }
      }
	 
  }
  
  private BudgetList InitBudgetLvOneNode(int plan_id,String project_id,String item_id, int category,String name,String budget)
  {
  	     BudgetList budget_list=new BudgetList();
  	     budget_list.plan_id=plan_id;
  	     budget_list.project_id=project_id;
  	     budget_list.item_id=item_id;
  	     budget_list.category=category;
  	     budget_list.name=name;
  	     budget_list.budget=budget;
  	     return budget_list;
  	     
  }
  
  private String turnSwfToHtmlURL(String pano_swf_url)  //将swf的url转变为html5的
  {
	  String pano_html5_url=pano_swf_url.substring(0, pano_swf_url.length()-13)+"/html5/output/0000.html";
	  return pano_html5_url;
  }
}