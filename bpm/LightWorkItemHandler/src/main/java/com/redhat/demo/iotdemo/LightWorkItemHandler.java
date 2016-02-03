package com.redhat.demo.iotdemo;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;


public class LightWorkItemHandler implements WorkItemHandler {
	
	public void abortWorkItem(WorkItem wi, WorkItemManager wim) {
		System.out.println("Oh no, my item aborted..");

	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wim) {
		
		HttpProducer producer = new HttpProducer();
		
		try {
			producer.sendGet("http://receiver:4711/lightsOff");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("V0.1 Message transmitted via mqtt");
		
		wim.completeWorkItem(wi.getId(), null);
	}

}
