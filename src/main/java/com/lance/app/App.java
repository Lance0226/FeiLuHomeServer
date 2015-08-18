package com.lance.app;

import com.lance.parse.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Hello world!
 *
 */
public class App 
{
 
    
    

	private static Map<String, String> info;

	public static void main( String[] args ) throws IOException
    {
		Parse parse=new Parse("http://www.xuanran001.com/rwrule/zxfazzjfb3/409a55a7-86ae-4db4-99f8-fdbe94f30bef.html");
		//info = new HashMap<String, String>();
		//info=parse.GetInfo();
		parse.GetBudget();
        	
        
    }
}
