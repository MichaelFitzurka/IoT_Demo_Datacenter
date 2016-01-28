package com.redhat.demo.iot.datacenter.monitor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.ActiveMQConnectionFactory;

public class App 
{
    private static final Logger log = Logger.getLogger(BRMSServer.class.getName());
	
	public static String sourceBrokerURL = "tcp://iotdatacenterreceiver:61616";
	public static String sourceQueueName = "message.to.monitor";
	 
    public static void main( String[] args ) throws Exception
    {
    	String	messageFromQueue;
    		
    	System.out.println(" Check if remote AMQ-Broker are already available");
    	AMQTester tester = new AMQTester(); 
    	
    
    	
    	while( tester.testAvailability( sourceBrokerURL ) == false ) {
    		System.out.println(" AMQ-Broker " + sourceBrokerURL + " not yet available ");
    		Thread.sleep(10000);
    	}
    	
		Consumer consumer = new Consumer(sourceQueueName, sourceBrokerURL);
	
		BRMSServer brmsServer = new BRMSServer();
		
		while ( 1 ==1 ) {
			messageFromQueue = consumer.run(20000);
			
			if ( messageFromQueue != null ) {
				
	            // Convert TextMessage to DataSet via jaxb unmarshalling
	            JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
	            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	
	            StringReader reader = new StringReader( messageFromQueue );
	            DataSet event = (DataSet) unmarshaller.unmarshal(reader);
		
		    event.setRequired(0);	            
	            event = brmsServer.insert( event);
	                     
	            if ( event.getRequired() == 1 ) {
	            	
	            	System.out.println("Need to call BPM Process!");
	            	
	            	BPMClient bpmClient = new BPMClient();
	            	bpmClient.doCall("http://iotdatacenterbpm:8080/business-central", 
	            				     "com.redhat.demo.iot.datacenter:HumanTask:1.0", 
	            				     "psteiner", "change12_me",
	            				     event);
	

	            } 
	            	            
			}
            
		}
		
    }
}
