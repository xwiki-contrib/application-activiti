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
package org.xwiki.activiti.internal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.script.ScriptContext;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.SessionFactory;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.script.ScriptContextManager;

/**
 * Default implementation of the ActivitiEngine component.
 * 
 * @author Sorin Burjan
 */

@Component
@Singleton
public class DefaultActivitiEngine implements ActivitiEngine, Initializable
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    @Inject
    private ComponentManager componentManager;

    // @Inject
    // private ScriptServiceManager scriptServiceManager;

    @Inject
    private org.xwiki.context.Execution execution;

    @Inject
    private ScriptContextManager scriptContextManager;

    @Inject
    @Named("user")
    private SessionFactory userEntityManagerFactory;

    @Inject
    @Named("group")
    private SessionFactory groupEntityManagerFactory;

    private ProcessEngine processEngine;

    private void deployProcess()
    {
        this.processEngine.getRepositoryService().createDeployment().name("DemoDeployment")
            .addClasspathResource("DemoProcess1.bpmn").addClasspathResource("DemoProcess2.bpmn")
            .addClasspathResource("DemoProcess3.bpmn").addClasspathResource("DemoProcess4.bpmn")
            .addClasspathResource("DemoProcess5.bpmn").deploy();
    }

    @Override
    public void initialize() throws InitializationException
    {
        logger.info("Initializing Activiti Engine");
        ProcessEngineConfiguration configuration =
            ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        processEngine = configuration.buildProcessEngine();

        // Override the default User and Group Factories This is done after the process engine is built because
        // otherwise the getSessionFactories() method will return null
        Map<Class< ? >, org.activiti.engine.impl.interceptor.SessionFactory> sessionFactories =
            ((ProcessEngineConfigurationImpl) configuration).getSessionFactories();
        sessionFactories.put(UserEntityManager.class, userEntityManagerFactory);
        sessionFactories.put(GroupEntityManager.class, groupEntityManagerFactory);

        // We inject Script Service Manager so we allow the usage of services from within Activiti
        Map<Object, Object> beans = new HashMap<Object, Object>();
        // beans.put("services", this.scriptServiceManager);
        beans.putAll(this.scriptContextManager.getScriptContext().getBindings(ScriptContext.ENGINE_SCOPE));

        ((ProcessEngineConfigurationImpl) configuration).setBeans(beans);

        logger.info("Injecting into Activiti the following XWiki ScriptServices: "
            + this.scriptContextManager.getScriptContext().getBindings(ScriptContext.ENGINE_SCOPE).keySet());

        logger.info("Deploying Default Process Definitions");
        deployProcess();
    }

    @Override
    public ProcessEngine getProcessEngine()
    {
        return this.processEngine;
    }

    @Override
    public RepositoryService getRepositoryService()
    {
        return this.processEngine.getRepositoryService();
    }

    @Override
    public RuntimeService getRuntimeService()
    {
        return this.processEngine.getRuntimeService();
    }

    @Override
    public IdentityService getIdentityService()
    {
        return this.processEngine.getIdentityService();
    }

    @Override
    public TaskService getTaskService()
    {
        return this.processEngine.getTaskService();
    }

    @Override
    public FormService getFormService()
    {
        return this.processEngine.getFormService();
    }

    @Override
    public HistoryService getHistoryService()
    {
        return this.processEngine.getHistoryService();
    }

    @Override
    public List<ProcessDefinition> getDeployedProcesses()
    {
        return this.processEngine.getRepositoryService().createProcessDefinitionQuery().orderByDeploymentId()
            .orderByProcessDefinitionKey().desc().list();
    }

    public List<Execution> getProcessInstances()
    {
        return this.processEngine.getRuntimeService().createExecutionQuery().list();
    }

    @Override
    public void deleteDeploymentByProcessDefinitionKey(String processDefinitionId)
    {
        ProcessDefinition pd =
            this.processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        this.processEngine.getRepositoryService().deleteDeployment(pd.getDeploymentId(), true);
    }

    @Override
    public List<FormProperty> getTaskFormProperties(String taskId)
    {
        return this.processEngine.getFormService().getTaskFormData(taskId).getFormProperties();
    }

    @Override
    public Task getTaskById(String taskId)
    {
        return this.processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public List<Task> getCurrentUserTasks()
    {
        return this.processEngine.getTaskService().createTaskQuery()
            .taskAssignee(documentAccessBridge.getCurrentUserReference().toString()).list();
    }

    @Override
    public List<HistoricTaskInstance> getCurrentUserHistoryTasks()
    {
        // Order the list descending by the end time of the tasks, so the latest finished tasks will be on top of the
        // list
        return this.processEngine.getHistoryService().createHistoricTaskInstanceQuery()
            .taskAssignee(documentAccessBridge.getCurrentUserReference().toString()).finished()
            .orderByHistoricTaskInstanceEndTime().desc().list();
    }

    @Override
    public List<Task> getAllUsersTasks()
    {
        return this.processEngine.getTaskService().createTaskQuery().list();
    }

    @Override
    public List<Task> getAllUnassignedUsersTasks()
    {
        return this.processEngine.getTaskService().createTaskQuery().taskUnassigned().list();
    }

    @Override
    public List<HistoricTaskInstance> getAllUsersHistoryTasks()
    {
        return this.processEngine.getHistoryService().createHistoricTaskInstanceQuery().finished().list();
    }

    @Override
    public List<Task> getCurrentUserCandidateTasks()
    {
        return this.processEngine.getTaskService().createTaskQuery()
            .taskCandidateUser(documentAccessBridge.getCurrentUserReference().toString()).taskUnassigned().list();
    }

    @Override
    public void claimTask(String taskId)
    {
        this.processEngine.getTaskService().claim(taskId, documentAccessBridge.getCurrentUserReference().toString());
    }

    @Override
    public void startProcessInstanceById(String processId)
    {
        this.processEngine.getRuntimeService().startProcessInstanceById(processId);
    }

    @Override
    public List<HistoricProcessInstance> getCompletedProcessInstances()
    {
        return this.getHistoryService().createHistoricProcessInstanceQuery().finished().orderByProcessInstanceEndTime()
            .desc().list();
    }

    @Override
    public List<HistoricVariableInstance> getHistoricaInstanceVariables(String instanceId)
    {
        return this.processEngine.getHistoryService().createHistoricVariableInstanceQuery()
            .processInstanceId(instanceId).list();
    }

    @Override
    public String getDeploymentName(String deploymentId)
    {
        return this.processEngine.getRepositoryService().createDeploymentQuery().deploymentId(deploymentId)
            .singleResult().getName();
    }

    @Override
    public void uploadProcessDefinition(String resourceName, InputStream inputStream)
    {
        this.processEngine.getRepositoryService().createDeployment().addInputStream(resourceName, inputStream).deploy();
    }
}
