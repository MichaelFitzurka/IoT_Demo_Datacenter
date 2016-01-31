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
	
    private static final String DEFAULT_RECEIVER	= "localhost";
	
    
	public static String sourceBrokerURL = "tcp://receiver:61616";
	public static String sourceQueueName = "message.to.rules"; 
	 
    public static void main( String[] args ) throws Exception
    {
    	String messageFromQueue;
    	String brokerURLMQTT = "tcp://" + System.getProperty("receiverURL",DEFAULT_RECEIVER) +  ":1883";
        
    		
    	System.out.println(" Check if remote AMQ-Broker are already available");
    	AMQTester tester = new AMQTester(); 
    	
    
    	
    	while( tester.testAvailability( sourceBrokerURL ) == false ) {
    		System.out.println(" AMQ-Broker " + sourceBrokerURL + " not yet available ");
    		Thread.sleep(10000);
    	}
    	
    	System.out.println(" AMQ-Broker " + sourceBrokerURL + " ready to work! ");
    	
    	
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
	            
	            System.out.println("Rules Event-DeviceType <"+event.getDeviceType()+">");
	                     
	            if ( event.getRequired() == 1 ) {
	            	
	            	System.out.println("Need to call BPM Process!");
	            	
	            	BPMClient bpmClient = new BPMClient();
	            	bpmClient.doCall("http://bpm:8080/business-central", 
	            				     "com.redhat.demo.iot.datacenter:HumanTask:1.0",
	            				     "IoT_Human_Task.low_voltage",
	            				     "psteiner", "change12_me",
	            				     event);
	
	            	System.out.println("Need to turn on alarm light!");
	            	MQTTProducer producer = new MQTTProducer(brokerURLMQTT, "admin", "change12_me", "mqtt.receiver");
	            	producer.run("iotdemocommand/light", "an");

	            } 
	            	            
			}
            
		}
		
    }
}
