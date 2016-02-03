package com.redhat.demo.iotdemo;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;


public class LightWorkItemHandler implements WorkItemHandler {
	
	 private static final String DEFAULT_MQTT_RECEIVER	= "192.168.42.1";
	
	
	public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
		System.out.println("Oh no, my item aborted..");

	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
		
		MqttProducer producer;
		String 		 brokerURLMQTT = "tcp://" + System.getProperty("receiverURL",DEFAULT_MQTT_RECEIVER) +  ":1883";
		String		 mqttTopic = "iotdemocommand/light";
		String		 mqttMessage = "aus";
		
		producer = new MqttProducer(brokerURLMQTT, "admin", "change12_me", "mqtt.receiver" );
        
 		try {
			producer.run( mqttTopic, mqttMessage );
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("V0.1 Message transmitted via mqtt");
		
		wim.completeWorkItem(wi.getId(), null);
	}

}
