package com.lance.scrapy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lance.datastructure.BudgetCategoryType;
import com.lance.datastructure.BudgetItemOne;
import com.lance.datastructure.BudgetItemThree;
import com.lance.datastructure.BudgetItemTwo;
import com.lance.datastructure.BudgetList;
import com.lance.server.*;
import com.lance.xml.xmlUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class ScrapyPlanList 
{
   private Document doc;
   private static final String urlPrefix="http://www.xuanran001.com";
   private static final String _urlPrefix="www.xuanran001.com";
   
   private List<String> detail_urlList;    //详情url
   private List<String> preview_urlList;   //预览图url
   private List<String> pano_urlList;      //全景url
	
   public ScrapyPlanList() throws IOException
   {
	  this.doc=Jsoup.connect(this.urlPrefix+"/rwtarget/zhuangxiuapptubiao.html").timeout(50000).get();
	  this.detail_urlList=new ArrayList<String>();
	  this.preview_urlList=new ArrayList<String>();
	  this.pano_urlList=new ArrayList<String>();
	  
   }
   
   public void parse() throws SQLException, IOException
   {
	   Elements elms1=doc.select("a[class=project-meta]");

	    for(Element elm1:elms1)
	   {
		   String strDetailURL=elm1.attr("href");
		   this.detail_urlList.add(this.urlPrefix+turnDetailUrlToNormal(strDetailURL));
		 
	   }
	    
	   Elements elms2=doc.select("img[class=BWfade fadein]");
	   
	   for(Element elm2:elms2)
	   {
		   String strPreviewURL=elm2.attr("src");
		   this.preview_urlList.add(this.urlPrefix+strPreviewURL);
		   //System.out.println(this.urlPrefix+strPreviewURL);
	   }
	   
	   if(this.preview_urlList.size()==this.detail_urlList.size())
	   {
		   for(int i=0;i<this.detail_urlList.size();i++)
		   {
			   ScrapyBudgetList budgetList=new ScrapyBudgetList(this.detail_urlList.get(i));
			   this.pano_urlList.add(budgetList.getPano());
			   SpoloSQL server=new SpoloSQL();
			   int id=server.getPlanFinalId();
			   server.insertToPlanList(id,this.preview_urlList.get(i),this.detail_urlList.get(i),this.pano_urlList.get(i));
			   List<BudgetList> arrBudgetList=budgetList.GetBudget(id);
			   budgetList.parseBudgetItem();
			   List<BudgetItemOne> arrBudgetItemOneList=budgetList.getItemListOne();
			   List<BudgetItemTwo> arrBudgetItemTwoList=budgetList.getItemListTwo();
			   List<BudgetItemThree> arrBudgetItemThreeList=budgetList.getItemListThree();
			   xmlUtil.BuildXML("xml"+i,arrBudgetList,arrBudgetItemOneList,arrBudgetItemTwoList,arrBudgetItemThreeList);
		   }
	   }
   }

  private String turnDetailUrlToNormal(String detail_url)  //将跳转url转为直接跳转后，
  {
	String new_detail_url="/rwrule/zxfazzjfb3"+detail_url.substring(10,detail_url.length());
	return new_detail_url;
	
  }
}
