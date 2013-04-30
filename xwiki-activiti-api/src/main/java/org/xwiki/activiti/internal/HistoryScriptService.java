package org.xwiki.activiti.internal;

import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;

public class HistoryScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    HistoryScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }

    /**
     * List of completed Tasks for the current User
     * 
     * @return
     */
    public List<HistoricTaskInstance> getCurrentUserHistoryTasks()
    {
        return this.activitiEngine.getCurrentUserHistoryTasks();
    }

    /**
     * List of completed Tasks for all Users
     * 
     * @return
     */
    public List<HistoricTaskInstance> getAllUsersHistoryTasks()
    {
        return this.activitiEngine.getAllUsersHistoryTasks();
    }

    public List<HistoricProcessInstance> getCompletedProcessInstances()
    {
        return this.activitiEngine.getCompletedProcessInstances();
    }

    public List<HistoricVariableInstance> getHistoricaInstanceVariables(String instanceId)
    {
        return this.activitiEngine.getHistoricaInstanceVariables(instanceId);
    }
}
