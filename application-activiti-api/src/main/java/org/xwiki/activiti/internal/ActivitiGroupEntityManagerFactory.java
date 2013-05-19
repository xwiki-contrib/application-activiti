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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.activiti.Session;
import org.xwiki.activiti.SessionFactory;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;

/**
 * @author Sorin Burjan
 */
@Component
@Singleton
@Named("group")
public class ActivitiGroupEntityManagerFactory implements SessionFactory
{
    @Inject
    private ComponentManager componentManager;

    @Inject
    private Logger logger;

    @Override
    public Class< ? > getSessionType()
    {
        return ActivitiGroupEntityManager.class;
    }

    @Override
    public org.activiti.engine.impl.interceptor.Session openSession()
    {
        // Return the custom implementation of GroupEntityManager
        // Instantiate a class of type org.xwiki.activiti.Session
        // and return a object of type Session (default Activiti Session)
        try {
            return componentManager.getInstance(Session.class, "group");
        } catch (ComponentLookupException e) {
            logger.error("Unable to get User Entity Manager", e);
            return null;
        }
    }

}
