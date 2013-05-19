package org.xwiki.activiti.internal;

import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;

public class RuntimeScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    RuntimeScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }

    /**
     * @return The list of running process instances
     */
    public List<Execution> getProcessInstances()
    {
        return this.activitiEngine.getProcessInstances();
    }

    /**
     * Starts a process identified by the given Process Key
     * 
     * @param processKey
     */
    public void startProcessInstanceById(String processId)
    {
        this.activitiEngine.startProcessInstanceById(processId);
    }
}
