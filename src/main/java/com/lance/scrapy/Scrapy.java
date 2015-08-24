package com.lance.scrapy;

import java.io.IOException;
import java.sql.SQLException;

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
	
   public Scrapy() throws IOException
   {
	  this.doc=Jsoup.connect(this.urlPrefix+"/rwtarget/zhuangxiuapptubiao.html").timeout(50000).get();
	  
   }
   
   public void Parse() throws SQLException
   {
	   Elements elms1=doc.select("a[class=project-meta]");

	    for(Element elm1:elms1)
	   {
		   String str=elm1.attr("href");
		   Server server=new Server();
		   server.insert(this._urlPrefix+str);
		   System.out.println(this.urlPrefix+str);
	   }
	   
	   
   }
}
