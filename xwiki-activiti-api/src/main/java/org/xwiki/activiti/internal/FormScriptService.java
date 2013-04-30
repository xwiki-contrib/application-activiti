package org.xwiki.activiti.internal;

import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.form.FormProperty;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;

public class FormScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    FormScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }

    /**
     * @param taskId
     * @return List of FormProperties specified in the Process Diagram for the Task
     */
    public List<FormProperty> getTaskFormProperties(String taskId)
    {
        return this.activitiEngine.getTaskFormProperties(taskId);
    }
}
