package com.lance.xml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class xmlUtil 
{
   public static void BuildXML()
   {
	   String xmlStr=null;
	   DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	   try {
		     DocumentBuilder builder=factory.newDocumentBuilder();
		     Document document=builder.newDocument();
		     document.setXmlVersion("1.0");
		     
		     Element root=document.createElement("root");
		     document.appendChild(root);
		     
		     Element telephone=document.createElement("TelePhone");
		     
		     Element nokia=document.createElement("type");
		     nokia.setAttribute("name", "nokia");
		     
		     Element priceNokia = document.createElement("price");
	            priceNokia.setTextContent("599");
	            nokia.appendChild(priceNokia);

	            Element operatorNokia = document.createElement("operator");
	            operatorNokia.setTextContent("CMCC");
	            nokia.appendChild(operatorNokia);

	            telephone.appendChild(nokia);

	            Element xiaomi = document.createElement("type");
	            xiaomi.setAttribute("name", "xiaomi");

	            Element priceXiaoMi = document.createElement("price");
	            priceXiaoMi.setTextContent("699");
	            xiaomi.appendChild(priceXiaoMi);

	            Element operatorXiaoMi = document.createElement("operator");
	            operatorXiaoMi.setTextContent("ChinaNet");
	            xiaomi.appendChild(operatorXiaoMi);

	            telephone.appendChild(xiaomi);

	            root.appendChild(telephone);

	            TransformerFactory transFactory = TransformerFactory.newInstance();
	            Transformer transFormer = transFactory.newTransformer();
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
