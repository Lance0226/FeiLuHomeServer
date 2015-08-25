package com.lance.scrapy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lance.server.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Scrapy 
{
   private Document doc;
   private static final String urlPrefix="http://www.xuanran001.com";
   private static final String _urlPrefix="www.xuanran001.com";
   
   private List<String> detail_urlList;
   private List<String> preview_urlList;
	
   public Scrapy() throws IOException
   {
	  this.doc=Jsoup.connect(this.urlPrefix+"/rwtarget/zhuangxiuapptubiao.html").timeout(50000).get();
	  this.detail_urlList=new ArrayList<String>();
	  this.preview_urlList=new ArrayList<String>();
	  
   }
   
   public void Parse() throws SQLException
   {
	   Elements elms1=doc.select("a[class=project-meta]");

	    for(Element elm1:elms1)
	   {
		   String strDetailURL=elm1.attr("href");
		   this.detail_urlList.add(this.urlPrefix+strDetailURL);
		   
		   //System.out.println(this.urlPrefix+strDetailURL);
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
			   Server server=new Server();
			   server.insert(this.preview_urlList.get(i),this.detail_urlList.get(i));
		   }
	   }
   }
}
