package org.xwiki.activiti.internal;

import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;

/**
 * @author sorinello
 */
public class TaskScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    TaskScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }

    /**
     * @param taskId
     * @return
     */
    public Task getTaskById(String taskId)
    {
        return this.activitiEngine.getTaskById(taskId);
    }

    /**
     * @return the list of tasks assigned to the current User
     */
    public List<Task> getCurrentUserTasks()
    {
        return this.activitiEngine.getCurrentUserTasks();
    }

    /**
     * @return the list of tasks where the current User is a potential candidate
     */
    public List<Task> getCurrentUserCandidateTasks()
    {
        return this.activitiEngine.getCurrentUserCandidateTasks();
    }

    /**
     * @return the list of global Tasks currently active
     */
    public List<Task> getUsersTasks()
    {
        return this.activitiEngine.getAllUsersTasks();
    }

    /**
     * List of global Tasks which are not assigned to any User
     * 
     * @return
     */
    public List<Task> getAllUnassignedUsersTasks()
    {
        return this.activitiEngine.getAllUnassignedUsersTasks();
    }

    /**
     * Claims a User Task as the logged-in User
     * 
     * @param taskId
     */
    public void claimTask(String taskId)
    {
        this.activitiEngine.claimTask(taskId);
    }
}
