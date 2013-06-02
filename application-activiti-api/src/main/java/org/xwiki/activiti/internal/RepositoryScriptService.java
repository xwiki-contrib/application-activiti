package org.xwiki.activiti.internal;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceSerializer;

public class RepositoryScriptService
{
    private ActivitiEngine activitiEngine;

    private XWikiActivitiBridge bridge;

    private DocumentAccessBridge documentAccessBridge;

    private EntityReferenceSerializer<String> entityReferenceSerializer;

    RepositoryScriptService(ActivitiEngine activitiEngine, XWikiActivitiBridge bridge,
        DocumentAccessBridge documentAccessBridge, EntityReferenceSerializer<String> entityReferenceSerializer)
    {
        this.activitiEngine = activitiEngine;
        this.bridge = bridge;
        this.documentAccessBridge = documentAccessBridge;
        this.entityReferenceSerializer = entityReferenceSerializer;
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

    public List<ProcessDefinition> getProcessesStartablyByCurrentUser()
    {

        DocumentReference docRef = documentAccessBridge.getCurrentUserReference();
        String userId = this.entityReferenceSerializer.serialize(docRef);
        return this.activitiEngine.getProcessesStartablyByCurrentUser(userId);
    }

    public ProcessDefinition getProcessDefinitionById(String processDefinitionId)
    {
        return this.activitiEngine.getProcessDefinitionById(processDefinitionId);
    }
}
