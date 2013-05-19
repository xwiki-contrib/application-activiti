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
/**
 * @author Sorin Burjan
 *
 */

package org.xwiki.activiti.internal;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.slf4j.Logger;
import org.xwiki.activiti.Session;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryManager;

/**
 * @author Sorin Burjan
 */
@Component
@Named("group")
public class ActivitiGroupEntityManager extends GroupEntityManager implements Session
{
    @Inject
    private ComponentManager componentManager;

    @Inject
    private QueryManager queryManager;

    @Inject
    private Logger logger;

    @Inject
    private DocumentReferenceResolver<String> documentReferenceResolver;

    @Override
    public Group createNewGroup(String groupId)
    {
        // Creates an Activiti Group object
        throw new ActivitiException("You are not allowed to create XWiki groups from Activiti");
    }

    @Override
    public void insertGroup(Group group)
    {
        // Used to actually insert groups in the DB,
        throw new ActivitiException("You are not allowed to create XWiki groups from Activiti");
    }

    @Override
    public void updateGroup(GroupEntity updatedGroup)
    {
        // Updates the informations of a User
        throw new ActivitiException("You are not allowed to update XWiki groups from Activiti");
    }

    @Override
    public void deleteGroup(String groupId)
    {
        // Deleting an Activiti group means deleting an XWiki group. This is not allowed from here
        throw new ActivitiException("You are not allowed to delete XWiki groups from Activiti");
    }

    @Override
    public GroupQuery createNewGroupQuery()
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public GroupEntity findGroupById(String groupId)
    {
        throw new ActivitiException("Method not implemented");
        // DocumentReference groupReference = new DocumentReference("xwiki", "XWiki", groupId);
        // ObjectReference objectReference = new ObjectReference("XWiki.XWikiGroups", groupReference);
        // // Since in XWiki the group name is the document name which holds the XWiki.XWikiGroups object,
        // // the groupId and group name will be set with the same value
        // GroupEntity groupEntity = new GroupEntity(groupId);
        // groupEntity.setName(groupId);
        // // Don't set Group type since XWiki has a different architecture
        // return groupEntity;
    }

    @Override
    public List<Group> findGroupsByUser(String userId)
    {
        // logger.info("I got the userId param: " + userId);
        List<Group> groups = new ArrayList<Group>();
        String queryStatement =
            "select doc.fullName from Document doc, doc.object(XWiki.XWikiGroups) as grp where grp.member = :username";
        List<Object> queryResults;
        try {
            queryResults = queryManager.createQuery(queryStatement, Query.XWQL).bindValue("username", userId).execute();
            // logger.info("Query Results: " + queryResults);
            for (Object xwikiGroupObject : queryResults) {
                Group group = new GroupEntity();
                DocumentReference docRef = documentReferenceResolver.resolve((String) xwikiGroupObject);
                group.setId(docRef.toString());
                group.setName(docRef.toString());
                // logger.info("adding group " + group.getId() + group.getName() + group.getType());
                groups.add(group);
            }
        } catch (QueryException e) {
            logger.error("Unable to execute query", e);
        }
        // logger.info("called from Group Entity: " + groups);
        return groups;
    }

    @Override
    public List<Group> findPotentialStarterUsers(String proceDefId)
    {
        throw new ActivitiException("Method not implemented");
    }

}
