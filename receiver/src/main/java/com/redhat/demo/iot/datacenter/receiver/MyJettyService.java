package com.redhat.demo.iot.datacenter.receiver;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyJettyService implements Processor {
	
	 public void process(Exchange exchange) throws Exception {
	        
//	        DataSet data = new DataSet(	"0", 
//	        							(String)exchange.getIn().getHeader("deviceType"), 
//	        							(String)exchange.getIn().getHeader("deviceID"), 
//	        							0, 
//	        							0, 
//	        							0, 
//        							0  );
		 String data = "aus";
		 
	        System.out.println("DeviceType received by Camel ( fullset ) = " + exchange.getIn().getHeader("deviceType") );
	 
	        exchange.getOut().setBody(data);
	    }

}
