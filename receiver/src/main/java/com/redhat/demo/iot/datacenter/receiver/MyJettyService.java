package com.redhat.demo.iot.datacenter.receiver;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MyJettyService implements Processor {
	
	 public void process(Exchange exchange) throws Exception {
	        // just get the body as a string
	        String body = exchange.getIn().getBody(String.class);
	        DataSet data = new DataSet();
	        
	        data.setDeviceType( (String)exchange.getIn().getHeader("deviceType") );
	        data.setDeviceID(   (String)exchange.getIn().getHeader("deviceID") );
	        
	        System.out.println("DeviceType received by Camel = " + exchange.getIn().getHeader("deviceType") );
	 
	        // we have access to the HttpServletRequest here and we can grab it if we need it
	        // HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
	     
	        // send a html response
	        exchange.getOut().setBody(data);
	    }

}
