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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.script.service.ScriptService;
import org.xwiki.stability.Unstable;

/**
 * Make the ActivitiEngine API available to scripting.
 * 
 * @author Sorin Burjan
 */
@Unstable
@Component
@Named("activiti")
@Singleton
public class ActivitiScriptService implements ScriptService
{
    @Inject
    private ActivitiEngine activiti;

    @Inject
    private XWikiActivitiBridge bridge;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    @Inject
    private EntityReferenceSerializer<String> entityReferenceSerializer;

    public RepositoryScriptService getRepository()
    {
        return new RepositoryScriptService(activiti, bridge, documentAccessBridge, entityReferenceSerializer);
    }

    public RuntimeScriptService getRuntime()
    {
        return new RuntimeScriptService(activiti);
    }

    public TaskScriptService getTask()
    {
        return new TaskScriptService(activiti, bridge);
    }

    public HistoryScriptService getHistory()
    {
        return new HistoryScriptService(activiti);
    }

    public FormScriptService getForm()
    {
        return new FormScriptService(activiti);
    }

    public IdentityScriptService getIdentity()
    {
        return new IdentityScriptService(activiti);
    }

    /**
     * @return The vanilla Activiti Engine API
     */
    public ProcessEngine getProcessEngine()
    {
        return this.activiti.getProcessEngine();
    }

    /**
     * @return The vanilla Activiti Runtime Service API
     */
    public RuntimeService getRuntimeService()
    {
        return this.activiti.getRuntimeService();
    }

    /**
     * @return The custom XWiki - Activiti Identity Service API
     */
    public IdentityService getIdentityService()
    {
        return this.activiti.getIdentityService();
    }

    /**
     * @return The vanilla Activiti Task Service API
     */
    public TaskService getTaskService()
    {
        return this.activiti.getTaskService();
    }

    /**
     * @return The vanilla Activiti Form Service API
     */
    public FormService getFormService()
    {
        return this.activiti.getFormService();
    }

    /**
     * @return The vanilla Activiti History Service API
     */
    public HistoryService getHistoryService()
    {
        return this.activiti.getHistoryService();
    }

}
