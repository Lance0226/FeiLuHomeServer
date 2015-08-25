package com.lance.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class io 
{
  public static void writeToFile(String fileName,String extension,String filePath,String content) throws IOException
  {
	  FileWriter writer=null;
	  try {
		   File file=new File(filePath+File.separator+fileName+"."+extension);
		   writer=new FileWriter(file);
		   writer.write(content);
	      } 
	  catch (Exception e) 
	     {
		    e.printStackTrace();
	     }
	  finally
	    {
		     writer.close();
	    }
  }
}
