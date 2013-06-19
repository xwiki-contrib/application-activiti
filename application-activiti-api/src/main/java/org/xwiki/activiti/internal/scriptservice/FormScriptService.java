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
import java.util.Map;

import org.activiti.engine.form.FormProperty;
import org.xwiki.activiti.ActivitiEngine;

/**
 * @author Sorin Burjan
 */
public class FormScriptService
{
    private ActivitiEngine activitiEngine;

    FormScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }

    public List<FormProperty> getTaskFormProperties(String taskId)
    {
        return this.activitiEngine.getTaskFormProperties(taskId);
    }

    public void submitTaskFormData(String taskId, Map<String, String> properties)
    {
        this.activitiEngine.submitTaskFormData(taskId, properties);
    }
}
