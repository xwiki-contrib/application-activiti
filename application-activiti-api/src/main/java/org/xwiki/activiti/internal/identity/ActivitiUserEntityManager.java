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

package org.xwiki.activiti.internal.identity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.slf4j.Logger;
import org.xwiki.activiti.Session;
import org.xwiki.bridge.DocumentAccessBridge;
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
@Named("user")
public class ActivitiUserEntityManager extends UserEntityManager implements Session
{

    @Inject
    private ComponentManager componentManager;

    @Inject
    private QueryManager queryManager;

    @Inject
    private Logger logger;

    @Inject
    private DocumentReferenceResolver<String> documentReferenceResolver;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    @Override
    public User createNewUser(String userId)
    {
        // Creates an Activiti User object
        throw new ActivitiException("You are not allowed to create XWiki users from Activiti");
    }

    @Override
    public void insertUser(User user)
    {
        // Used to actually insert users in the DB,
        throw new ActivitiException("You are not allowed to create XWiki users from Activiti");
    }

    @Override
    public void updateUser(UserEntity updatedUser)
    {
        // Updates the informations of a User
        throw new ActivitiException("You are not allowed to modify XWiki users from Activiti");
    }

    @Override
    public UserEntity findUserById(String userId)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public void deleteUser(String userId)
    {
        // Deleting an Activiti user means deleting an XWiki user. This is not allowed from here
        throw new ActivitiException("You are not allowed to delete XWiki users from Activiti");
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public List<Group> findGroupsByUser(String userId)
    {
        List<Group> groups = new ArrayList<Group>();
        String queryStatement =
            "select doc.name from Document doc, doc.object(XWiki.XWikiGroups) as grp where grp.member = :username";
        List<Object> queryResults;
        try {
            queryResults = queryManager.createQuery(queryStatement, Query.XWQL).bindValue("username", userId).execute();
            for (Object xwikiGroupObject : queryResults) {
                Group group = new GroupEntity();
                DocumentReference docRef = documentReferenceResolver.resolve((String) xwikiGroupObject);
                group.setId(docRef.getName());
                group.setName(docRef.getName());
                groups.add(group);
            }
        } catch (QueryException e) {
            logger.error("Unable to execute query", e);
        }
        logger.info("called from User Entity: " + groups);
        return groups;
    }

    @Override
    public UserQuery createNewUserQuery()
    {
        return super.createNewUserQuery();
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId, String key)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId, String type)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public Boolean checkPassword(String userId, String password)
    {
        throw new ActivitiException("Method not implemented");
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId)
    {
        throw new ActivitiException("Method not implemented");
    }
}
