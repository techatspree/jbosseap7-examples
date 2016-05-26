package de.akquinet.jbosscc.cluster.client;

import de.akquinet.jbosscc.cluster.RemoteStateful;
import de.akquinet.jbosscc.cluster.ClusteredStatefulBean;
import de.akquinet.jbosscc.cluster.RemoteStateless;
import de.akquinet.jbosscc.cluster.ClusteredStatelessBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteEJBClient {

	private final Context context;

	public RemoteEJBClient() throws NamingException {
		context = initializeJNDIContext();

	}

	private Context initializeJNDIContext() throws NamingException {

//		Properties jndiProperties = new Properties();
//		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//		context = new InitialContext(jndiProperties);

		Context context = new InitialContext();
		return context;
	}

	public RemoteStateless lookupRemoteStatelessBean() throws NamingException {
		final String appName = "";
		final String moduleName = "cluster"; // module-name in ejb-jar.xml
		final String distinctName = ""; // jboss:distinct-name in jboss-ejb3.xml

		// ejb:/cluster//ClusteredStatelessBean!de.akquinet.jbosscc.cluster.RemoteStateless
		final String jndiName = "ejb:" + appName + '/' + moduleName + '/' + distinctName + '/'
				+ ClusteredStatelessBean.class.getSimpleName() + '!' + RemoteStateless.class.getName();

		return (RemoteStateless) context.lookup(jndiName);
	}

	public RemoteStateful lookupRemoteStatefulBean() throws NamingException {
		final String appName = "";
		final String moduleName = "cluster"; // module-name in ejb-jar.xml
		final String distinctName = ""; // jboss:distinct-name in jboss-ejb3.xml

		// ejb:/cluster//ClusteredStatefulBean!de.akquinet.jbosscc.cluster.RemoteStateful?stateful
		final String jndiName = "ejb:" + appName + '/' + moduleName + '/' + distinctName + '/'
				+ ClusteredStatefulBean.class.getSimpleName() + '!' + RemoteStateful.class.getName() + "?stateful";

		return (RemoteStateful) context.lookup(jndiName);
	}
}
