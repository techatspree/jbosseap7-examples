package de.akquinet.jbosscc.examples.eap.bean;

import java.util.logging.Logger;

public class JmsBatchJob implements BatchJob {
    private static final Logger LOGGER = Logger.getLogger(JmsBatchJob.class.getName());
    private final String name;

    public JmsBatchJob(final String name) {
        this.name = name;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public void work() throws Exception {
        LOGGER.info("Work. Work. Work.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String call() throws Exception {
        return "called";
    }
}
