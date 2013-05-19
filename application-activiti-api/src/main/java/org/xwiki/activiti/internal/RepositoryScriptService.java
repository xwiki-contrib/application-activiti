package org.xwiki.activiti.internal;

import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;

public class RepositoryScriptService
{
    @Inject
    private Logger logger;

    // XWikiActivitiBridge bridge = Utils.getComponent(XWikiActivitiBridge.class);

    private ActivitiEngine activitiEngine;

    private XWikiActivitiBridge bridge;

    RepositoryScriptService(ActivitiEngine activitiEngine, XWikiActivitiBridge bridge)
    {
        this.activitiEngine = activitiEngine;
        this.bridge = bridge;
    }

    /**
     * @return The list of deployed process definitions
     */
    public List<ProcessDefinition> getDeployedProcesses()
    {
        return this.activitiEngine.getDeployedProcesses();
    }

    /**
     * Deletes a process definition with all its active tasks and its history
     * 
     * @param process definition key which was deployed with the process deployment
     */
    public void deleteProcessDefinition(String processDefinitionId)
    {
        System.out.println("Deleting process definition: " + this.bridge.hasPermissions());
        if (this.bridge.hasPermissions()) {
            this.activitiEngine.deleteDeploymentByProcessDefinitionKey(processDefinitionId);
        }
    }

    public String getDeploymentName(String deploymentId)
    {
        return this.activitiEngine.getDeploymentName(deploymentId);
    }

    public void uploadProcessDefinition(String resourceName, InputStream inputStream)
    {
        System.out.println("Uploading process definition is allowed: " + this.bridge.hasPermissions());
        if (this.bridge.hasPermissions()) {
            this.activitiEngine.uploadProcessDefinition(resourceName, inputStream);
        }
    }

}
