package com.redhat.demo.iot.datacenter.monitor;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	
	 private ConnectionFactory factory;
	 private Connection connection;
	 private Session session;
	 private MessageProducer producer;
	 private String brokerURL	="";
	 private String queueName	="";
	 private String uid			= "admin";
	 private String passwd		= "admin";
 
	 
 
	 public Producer(String queueName, String brokerURL) throws JMSException
	 {
		this.brokerURL = brokerURL;
		this.queueName = queueName;
			  
		connect(this.queueName, this.brokerURL, this.uid, this.passwd);
	 }
 
	 public void connect(String queueName, String brokerURL, String uid, String passwd) 
	 {
		// setup the connection to ActiveMQ
	    factory = new ActiveMQConnectionFactory(this.uid, this.passwd, this.brokerURL);
	    	
	    try {
		    connection = factory.createConnection();
		    connection.start();
		    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		    Destination destination = session.createQueue(queueName);
		    producer = session.createProducer(destination);
	    } catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public void run(String data)
	 {
    
        Message message;
		try {
			message = session.createTextMessage( data );
		
			producer.send(message);
		} catch (JMSException e) {
			System.out.println("Session to broker "+brokerURL+" closed due to no traffic, need to reconnect");
			
			connect(queueName, brokerURL, uid, passwd);
			
			run(data);
			
			e.printStackTrace();
		}
	 }
 
	    public void close() throws JMSException
	    {
	        if (connection != null)
	        {
	            connection.close();
	        }
	    }
}
