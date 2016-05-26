package de.akquinet.jbosscc.examples.eap.dynamic;

import de.akquinet.jbosscc.examples.eap.jms.JmsService;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Model
public class WebController {
    @EJB
    private DynamicProxyService proxyService;

    @EJB
    private JmsService jmsService;

    private String jobName = "job1";

    public void submitTask() {
        proxyService.submitBatch(getJobName());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Job submitted", null));
    }

    public void startTask() {
        proxyService.startBatch(getJobName());

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "Job started", null));
    }

    public List<String> getTasks() {
        return proxyService.listTasks();
    }

    public List<String> getJobs() {
        return proxyService.listJobs();
    }

    public void submitBatchToJms() {
        jmsService.submitBatchToJms(getJobName());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
            "JMS job submitted", null));
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }
}
