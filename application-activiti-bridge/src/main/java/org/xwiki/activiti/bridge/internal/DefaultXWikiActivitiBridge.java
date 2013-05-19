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

package org.xwiki.activiti.bridge.internal;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;

import com.xpn.xwiki.XWikiContext;

/**
 * This class implements XWikiActivitiBridge declared in the xwiki-activiti-api project. Is used by the
 * xwiki-activiti-api as a bridge so it does not use directly xwiki-platform-oldcore
 * 
 * @author Sorin Burjan
 */
@Component
@Singleton
public class DefaultXWikiActivitiBridge implements XWikiActivitiBridge
{

    @Inject
    private Execution execution;

    /**
     * Verifies if the context user has Admin Rights
     * 
     * @return
     */
    @Override
    public boolean hasPermissions()
    {
        XWikiContext xwikiContext = getXWikiContext();
        return xwikiContext.isMainWiki() && xwikiContext.getWiki().getRightService().hasAdminRights(xwikiContext);
    }

    public String getDocumentReference(Object xwikiDocument)
    {
        return null;

    }

    private XWikiContext getXWikiContext()
    {
        return (XWikiContext) this.execution.getContext().getProperty(XWikiContext.EXECUTIONCONTEXT_KEY);
    }

}
