package de.akquinet.jbosscc.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class HaSingletonBean {
	
	@PostConstruct
	public void init(){
		System.out.println("start ha singleton");
	}

}
