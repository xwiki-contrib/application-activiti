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

package org.xwiki.activiti;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.xwiki.component.annotation.Role;

/**
 * Interface implementing the main entry point of the Activiti component
 * 
 * @author Sorin Burjan
 */
@Role
public interface ActivitiEngine
{

    ProcessEngine getProcessEngine();

    RepositoryService getRepositoryService();

    IdentityService getIdentityService();

    TaskService getTaskService();

    RuntimeService getRuntimeService();

    FormService getFormService();

    HistoryService getHistoryService();

    /**
     * List of Deployed Processes from the Engine
     * 
     * @return List of Deployed Processes from the Engine
     */
    List<ProcessDefinition> getDeployedProcesses();

    /**
     * A deployed process definition
     * 
     * @return A deployed process definition
     */

    ProcessDefinition getProcessDefinitionById(String processDefinitionId);

    /**
     * List of running Process Instances
     * 
     * @return List of running Process Instances
     */
    List<Execution> getProcessInstances();

    /**
     * For deleting a deployed Process Definition, the Deployment corresponding to that process must also be deleted.
     * This method will find out the deploymentId of the Process Definition to be deleted, and then deletes the whole
     * deployment with all its active tasks and its history
     * 
     * @param process definition key which was deployed with the process deployment
     */
    void deleteDeploymentByProcessDefinitionKey(String processInstanceKey);

    /**
     * List of Form Properties of the given Task
     * 
     * @param taskId
     * @return List of Form Properties of the given Task
     */
    List<FormProperty> getTaskFormProperties(String taskId);

    /**
     * Task with the given Id
     * 
     * @param taskId
     * @return Task with the given Id
     */
    Task getTaskById(String taskId);

    /**
     * List of Tasks assigned to the current User
     * 
     * @return List of Tasks assigned to the current User
     */
    List<Task> getCurrentUserTasks();

    /**
     * List of Tasks of all Users
     * 
     * @return List of Tasks of all Users
     */
    List<Task> getAllUsersTasks();

    /**
     * List of completed tasks for the current User
     * 
     * @return List of completed tasks for the current User
     */
    List<HistoricTaskInstance> getCurrentUserHistoryTasks();

    /**
     * List of completed tasks for all Users
     * 
     * @return List of completed tasks by all Users
     */
    List<HistoricTaskInstance> getAllUsersHistoryTasks();

    /**
     * List of Tasks where the current User is a candidate
     * 
     * @return List of Tasks where the current User is a candidate
     */
    List<Task> getCurrentUserCandidateTasks();

    /**
     * List of Tasks which are not assigned to any User
     * 
     * @return List of Tasks which are not assigned to any User
     */
    List<Task> getAllUnassignedUsersTasks();

    /**
     * Claim the task with the given ID to the current User
     * 
     * @param taskId
     */
    void claimTask(String taskId);

    /**
     * Starts a process instance based on the given processKey
     * 
     * @param processKey
     */
    void startProcessInstanceById(String processId);

    /**
     * List of completed Process Instances
     * 
     * @return
     */
    List<HistoricProcessInstance> getCompletedProcessInstances();

    /**
     * List of variables of the given Instance Id
     * 
     * @param instanceId
     * @return
     */
    List<HistoricVariableInstance> getHistoricaInstanceVariables(String instanceId);

    /**
     * @param deploymentId
     * @return
     */
    String getDeploymentName(String deploymentId);

    /**
     * @param resourceName
     * @param processDefinition
     */
    void uploadProcessDefinition(String resourceName, InputStream processDefinition);

    /**
     * @param userId
     * @return
     */
    List<ProcessDefinition> getProcessesStartablyByCurrentUser(String userId);

    /**
     * @param taskId
     * @param properties
     */
    void submitTaskFormData(String taskId, Map<String, String> properties);
}
