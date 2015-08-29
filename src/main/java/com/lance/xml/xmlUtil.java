package com.lance.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lance.datastructure.BudgetList;


public class xmlUtil 
{
   public static void BuildXML(List<BudgetList> arrBudgetList)
   {
	   String xmlStr=null;
	   DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	   try {
		     DocumentBuilder builder=factory.newDocumentBuilder();
		     Document document=builder.newDocument();
		     document.setXmlVersion("1.0");
		     
		      Element root=document.createElement("root");
		      document.appendChild(root);
		      
		      Element node11=document.createElement("node1");
		      root.appendChild(node11);
		      
		      Element node21=document.createElement("node2");
		      node21.setAttribute("name","家庭基础施工预算");
		      Element node22=document.createElement("node2");
		      node22.setAttribute("name","家装基础建材预算");
		      Element node23=document.createElement("node2");
		      node23.setAttribute("name","家装基础家居预算");
		      node11.appendChild(node21);
		      node11.appendChild(node22);
		      node11.appendChild(node23);
		      
		      String section_name=null;//预算分区的名字
		      
		      for(BudgetList budget:arrBudgetList)
		      {   
		    	  Element node;
		    	  switch (budget.category) 
		    	  {
				    case 0:section_name="sgBill";
				           node=document.createElement("node3");
				           node.setAttribute("name", budget.name);
				           node.setAttribute("budget",budget.budget);
				           node21.appendChild(node);
				           break;
				    case 1:section_name="yzBill";
				           node=document.createElement("node3"); 
				           node.setAttribute("name", budget.name);
				           node.setAttribute("budget",budget.budget);
			               node22.appendChild(node);
				           break;
				    case 2:section_name="rzBill";
				           node=document.createElement("node3");
				           node.setAttribute("name", budget.name);
				           node.setAttribute("budget",budget.budget);
		                   node23.appendChild(node);
				           break;
				    default:System.out.print("budget category error");break;
				    
				
				  }
		      }
		      
		      
		      
	            TransformerFactory transFactory = TransformerFactory.newInstance();
	            Transformer transFormer = transFactory.newTransformer();
	            transFormer.setOutputProperty(OutputKeys.INDENT,"yes");
	            DOMSource domSource = new DOMSource(document);

	            //export string
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            transFormer.transform(domSource, new StreamResult(bos));
	            xmlStr = bos.toString();

	            //-------
	            //save as file
	            File file = new File("TelePhone.xml");
	            if(!file.exists()){
	                file.createNewFile();
	            }
	            FileOutputStream out = new FileOutputStream(file);
	            StreamResult xmlResult = new StreamResult(out);
	            transFormer.transform(domSource, xmlResult);
		     
	       } 
	   catch (Exception e) 
	       {
		   
	       }
	   
   }
}
