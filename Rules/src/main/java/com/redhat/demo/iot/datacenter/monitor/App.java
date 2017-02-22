package com.redhat.demo.iot.datacenter.monitor;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class App 
{
    private static final Logger log = Logger.getLogger(BRMSServer.class.getName());
	
   
   	 
    public static void main( String[] args ) throws InterruptedException, JMSException, JAXBException, MqttPersistenceException, MqttException
    {
    	String	cacheValue=null;
    	String 	messageFromQueue;
    	String 	sourceAMQBroker  = System.getenv("SOURCE_AMQ_BROKER");
    	String 	targetMQTTBroker = System.getenv("TARGET_MQTT_BROKER");
    	String 	sourceQueue 	 = System.getenv("SOURCE_QUEUE");
    	String  brokerUID		 = System.getenv("BROKER_ADMIN_UID");
    	String  brokerPassword   = System.getenv("BROKER_ADMIN_PASSWD");
    	
    	DataGridHttpHelper 	jdgHelper;
            	
    	System.out.println(" Check if remote AMQ-Broker are already available");
    	AMQTester tester = new AMQTester(); 
    	
    	tester.waitForBroker(sourceAMQBroker);
    	
    	System.out.println(" AMQ-Broker " + sourceAMQBroker + " ready to work! ");
    	
    	// Connecting to JBDG
    	jdgHelper = new DataGridHttpHelper();

		Consumer consumer = new Consumer(sourceQueue, sourceAMQBroker);
	
		BRMSServer brmsServer = new BRMSServer();
		
		while ( true ) {
			messageFromQueue = consumer.run(20000);		
			
			if ( messageFromQueue != null ) {
				
	            // Convert TextMessage to DataSet via jaxb unmarshalling
	            JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
	            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
	
	            StringReader reader = new StringReader( messageFromQueue );
	            DataSet event = (DataSet) unmarshaller.unmarshal(reader);
		
	            event.setRequired(0);	
	            event.setErrorCode(0);
	            
	            System.out.println("checking with cache if we know this event already.");
	            
	            // Validate if we already have an open process for this
	            try {
					cacheValue = jdgHelper.getMethod("http://jdg:8080/rest/default/"+event.getDeviceType()+event.getDeviceID());
				} catch (IOException e) {
					cacheValue=null;
				}
	            
	            System.out.println("Cached value for <"+event.getDeviceType()+event.getDeviceID()+"> is <"+cacheValue+">");
	            
	            if ( ( cacheValue == null ) || ( cacheValue.contains("solved")) ) {
	         
	            	event = brmsServer.insert( event);
	      	      	
		            System.out.println("Rules Event-DeviceType <"+event.getDeviceType()+">");
		                     
		            if ( event.getErrorCode() != 0 ) {
		            	
		            	System.out.println("Need to call BPM Process!");
		            	
		            	try {
		            		BPMClient bpmClient = new BPMClient();
			
		            		bpmClient.doCall("http://104.155.37.167:8080/business-central",
        				     "org.jbpm:IoTEvent:1.0",
        				     "IoTProcesses.IoTEvent",
        				     "psteiner", "change12_me",
        				     event);
		            		
		            	} catch (Exception ex) {
		            		System.out.println("Exception when calling BPMSuite");
		                }
		            
		            
		            	System.out.println("Need to turn on alarm light!");
		            	MQTTProducer producer = new MQTTProducer(targetMQTTBroker, brokerUID, brokerPassword, "rules_server");
		            	producer.run("iotdemocommand/light", "an");
		            	
		            	System.out.println("Pushing this event to distributed Cache");
		            	try {
							jdgHelper.putMethod("http://jdg:8080/rest/default/"+event.getDeviceType()+event.getDeviceID(),"known");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	
		            } 

	            } else {
	            	System.out.println("No need to call BRMS, we know this already");
	            }
	            	            
			}
            
		}
    } 
}
