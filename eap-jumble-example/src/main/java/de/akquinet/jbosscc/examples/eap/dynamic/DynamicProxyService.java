package de.akquinet.jbosscc.examples.eap.dynamic;

import de.akquinet.jbosscc.examples.eap.bean.BatchJob;
import de.akquinet.jbosscc.examples.eap.bean.BatchJobImpl;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ContextService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class DynamicProxyService {
    @Inject
    private Logger log;

    @Resource
    private ContextService contextService;

    @Resource
    private ManagedExecutorService executorService;

    private final Map<BatchJob, Future<String>> tasks = new LinkedHashMap<>();
    private final Map<String, BatchJob> jobs = new LinkedHashMap<>();


    public void submitBatch(final String name) {
        final BatchJob contextualProxy = contextService.createContextualProxy(new BatchJobImpl(name), BatchJob.class);
        jobs.put(name, contextualProxy);
    }

    public void startBatch(final String name) {
        assert jobs.containsKey(name);

        final BatchJob contextualProxy = jobs.get(name);
        final Future<String> future = executorService.submit(contextualProxy);

        tasks.put(contextualProxy, future);
    }

    public List<String> listTasks() {
        return tasks.entrySet().stream().map(entry -> toString(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public List<String> listJobs() {
        return jobs.entrySet().stream().map(entry -> toString(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    private String toString(final String name, final BatchJob job) {
        return name + " => " + job.toString();
    }

    private String toString(final BatchJob job, final Future<String> future) {
        return job.toString() + " -> " + toString(future);
    }

    private String toString(final Future<String> future) {
        String status = future.isDone() ? "FINISHED" : "RUNNING";
        String result = "<?>";

        if (future.isDone()) {

            try {
                result = future.get();
            } catch (InterruptedException e) {
                status = "INTERRUPTED";
                result = e.toString();
            } catch (ExecutionException e) {
                status = "ABORTED";
                Throwable exception = e.getCause();
                for (; exception.getCause() != null; exception = exception.getCause()) {

                }

                log.log(Level.SEVERE, "Running job", e.getCause());
                result = exception.toString() + ": See log file for details";
            }
        }

        return status + "=" + result;
    }
}
