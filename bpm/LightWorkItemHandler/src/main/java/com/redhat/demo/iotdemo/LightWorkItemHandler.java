package com.redhat.demo.iotdemo;

import java.io.IOException;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;


public class LightWorkItemHandler implements WorkItemHandler {
	
	public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
		System.out.println("Oh no, my item aborted..");

	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
		
		HttpProducer 		producer  = new HttpProducer();
		DataGridHttpHelper 	jdgHelper = new DataGridHttpHelper();
		
		String			deviceType;
		String			deviceID;
		
		
		deviceType = (String)wi.getParameter("deviceType");
		deviceID = (String)wi.getParameter("deviceID");
		
		try {
			producer.sendGet("http://receiver:4711/lightsOff?deviceType="+deviceType+"&deviceID="+deviceID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("V0.1 Message transmitted via mqtt");
		
		try {
			jdgHelper.putMethod("http://jdg:8080/rest/default/"+deviceType+deviceID, "solved");
			System.out.println("After put via REST value = <"+jdgHelper.getMethod("http://jdg:8080/rest/default/"+deviceType+deviceID)+">");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		wim.completeWorkItem(wi.getId(), null);
	}

}
