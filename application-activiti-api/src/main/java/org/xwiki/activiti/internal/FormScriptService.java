package org.xwiki.activiti.internal;

import java.util.List;
import java.util.Map;

import org.activiti.engine.form.FormProperty;
import org.xwiki.activiti.ActivitiEngine;

public class FormScriptService
{
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

    public void submitTaskFormData(String taskId, Map<String, String> properties)
    {
        this.activitiEngine.submitTaskFormData(taskId, properties);
    }
}
