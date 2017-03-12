package com.redhat.demo.iot.datacenter.monitor;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.scanner.MavenRepository;

import static org.kie.scanner.MavenRepository.getMavenRepository;

import org.eclipse.aether.artifact.Artifact;


public class KieBaseProvider {
	private static final KieServices KIE_SERVICES = KieServices.Factory.get();
	private static final long POLLING_INTERVAL = 1000L;
	private final KieContainer KIE_CONTAINER;
	private final KieScanner KIE_SCANNER;
	
	
	public KieBaseProvider(String groupId,String artifactId,String version ) {
		
		MavenRepository repository = getMavenRepository();
		

		
		ReleaseId releaseId = KIE_SERVICES.newReleaseId(groupId, artifactId, version);
		
		Artifact art = repository.resolveArtifact(releaseId);
		
		System.out.println("Artifact (Kjar) pulled from : "+art.getFile().getPath());
		
		//This KieContainer will be built using the kjar specified by the provided
		//releaseId. KieServices will use maven to fetch the kjar.
		KIE_CONTAINER = KIE_SERVICES.newKieContainer(releaseId);
		
		//This call will create a scanner and associate it to our kie container. The scanner will poll
		//the maven repo for updates and automatically update the kiecontainer.
		KIE_SCANNER =  KIE_SERVICES.newKieScanner(KIE_CONTAINER);
		
		//Start the polling every POLLING_INTERVAL milliseconds
		KIE_SCANNER.start(POLLING_INTERVAL);
	}
	
	public KieBase getKieBase() {
		return KIE_CONTAINER.getKieBase();
	}
}
