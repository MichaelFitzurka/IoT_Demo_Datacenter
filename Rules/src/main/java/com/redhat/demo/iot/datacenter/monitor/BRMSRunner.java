package com.redhat.demo.iot.datacenter.monitor;

import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

public class BRMSRunner {
	
	//The releaseId uses maven GAV (groupId, artifactId and version) to specify a kjar
	public static final String GROUPID = "RedHat";
	public static final String ARTIFACTID = "BusinessRules";
	public static final String VERSION = "LATEST";
	
	private KieBase kieBase;

    public BRMSRunner() {
    	initKieSession();
    }
    
    private void initKieSession() {
		KieBaseProvider kbp = new KieBaseProvider(GROUPID,ARTIFACTID,VERSION);
		kieBase = kbp.getKieBase();
    }
    
    public DataSet fireRules( DataSet event ) {	
    	
    	redhat.businessrules.DataSet brmsDataSet = new redhat.businessrules.DataSet();
    	
    	// copy data to DataSet defined in BRMS
    	brmsDataSet.setDeviceType(event.getDeviceType());
    	brmsDataSet.setDeviceID(event.getDeviceID());
    	brmsDataSet.setPayload(event.getPayload());
    	
		KieSession kieSession = kieBase.newKieSession();
		kieSession.insert(brmsDataSet);
		kieSession.fireAllRules();
		kieSession.dispose();    
		
		event.setErrorCode(brmsDataSet.getErrorCode());
		event.setErrorMessage(brmsDataSet.getErrorMessage());
		
		return event;
	}
}
