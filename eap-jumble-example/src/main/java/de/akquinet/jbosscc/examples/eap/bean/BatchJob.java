package de.akquinet.jbosscc.examples.eap.bean;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface BatchJob extends Callable<String>, Serializable {
    boolean isRunning();

    void work() throws Exception;

    String getName();
}
