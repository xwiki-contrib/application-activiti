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

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.stability.Unstable;

/**
 * @author Sorin Burjan
 */
@Unstable
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

    public List<HistoricTaskInstance> getCurrentUserHistoryTasks()
    {
        return this.activitiEngine.getCurrentUserHistoryTasks();
    }

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
