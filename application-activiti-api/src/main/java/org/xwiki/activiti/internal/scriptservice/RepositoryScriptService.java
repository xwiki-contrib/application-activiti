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

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceSerializer;

/**
 * @author Sorin Burjan
 */
public class RepositoryScriptService
{
    private ActivitiEngine activitiEngine;

    private XWikiActivitiBridge bridge;

    private DocumentAccessBridge documentAccessBridge;

    private EntityReferenceSerializer<String> entityReferenceSerializer;

    RepositoryScriptService(ActivitiEngine activitiEngine, XWikiActivitiBridge bridge,
        DocumentAccessBridge documentAccessBridge, EntityReferenceSerializer<String> entityReferenceSerializer)
    {
        this.activitiEngine = activitiEngine;
        this.bridge = bridge;
        this.documentAccessBridge = documentAccessBridge;
        this.entityReferenceSerializer = entityReferenceSerializer;
    }

    public List<ProcessDefinition> getDeployedProcesses()
    {
        return this.activitiEngine.getDeployedProcesses();
    }

    public void deleteProcessDefinition(String processDefinitionId)
    {
        if (this.bridge.hasPermissions()) {
            this.activitiEngine.deleteDeploymentByProcessDefinitionKey(processDefinitionId);
        }
    }

    public String getDeploymentName(String deploymentId)
    {
        return this.activitiEngine.getDeploymentName(deploymentId);
    }

    public void uploadProcessDefinition(String resourceName, InputStream inputStream)
    {
        if (this.bridge.hasPermissions()) {
            this.activitiEngine.uploadProcessDefinition(resourceName, inputStream);
        }
    }

    public List<ProcessDefinition> getProcessesStartablyByCurrentUser()
    {
        DocumentReference docRef = documentAccessBridge.getCurrentUserReference();
        String userId = this.entityReferenceSerializer.serialize(docRef);
        return this.activitiEngine.getProcessesStartablyByCurrentUser(userId);
    }

    public ProcessDefinition getProcessDefinitionById(String processDefinitionId)
    {
        return this.activitiEngine.getProcessDefinitionById(processDefinitionId);
    }
}
