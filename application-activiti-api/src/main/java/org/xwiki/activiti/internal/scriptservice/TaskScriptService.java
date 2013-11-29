/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.activiti.internal.scriptservice;

import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.stability.Unstable;

/**
 * @author Sorin Burjan
 */
@Unstable
public class TaskScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    private XWikiActivitiBridge bridge;

    TaskScriptService(ActivitiEngine activitiEngine, XWikiActivitiBridge bridge)
    {
        this.activitiEngine = activitiEngine;
        this.bridge = bridge;
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
        if (this.bridge.hasPermissions()) {
            return this.activitiEngine.getAllUsersTasks();
        } else
            return null;
    }

    /**
     * List of global Tasks which are not assigned to any User
     * 
     * @return
     */
    public List<Task> getAllUnassignedUsersTasks()
    {
        if (this.bridge.hasPermissions()) {
            return this.activitiEngine.getAllUnassignedUsersTasks();
        } else
            return null;
    }

    /**
     * Claims a User Task as the logged-in User
     * 
     * @param taskId
     */
    public void claimTask(String taskId)
    {
        List<Task> tasks = activitiEngine.getCurrentUserCandidateTasks();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(taskId)) {
                this.activitiEngine.claimTask(taskId);
            }
        }
    }
}
