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
package org.xwiki.activitiTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.internal.DefaultActivitiEngine;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.query.QueryManager;
import org.xwiki.query.internal.DefaultQueryManager;
import org.xwiki.script.ScriptContextManager;
import org.xwiki.script.internal.DefaultScriptContextManager;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

/**
 * Tests for the {@link ActivitiEngine} component.
 */
public class ActivitiTest
{
    @Rule
    public MockitoComponentMockingRule<ActivitiEngine> mocker = new MockitoComponentMockingRule<ActivitiEngine>(
        DefaultActivitiEngine.class);

    @Rule
    public MockitoComponentMockingRule<QueryManager> queryManager = new MockitoComponentMockingRule<QueryManager>(
        DefaultQueryManager.class);

    @Rule
    public MockitoComponentMockingRule<ScriptContextManager> scriptContextManager =
        new MockitoComponentMockingRule<ScriptContextManager>(DefaultScriptContextManager.class);

    public DocumentReference docRef = mock(DocumentReference.class);

    public void testDefaultActivitiEngine1() throws ComponentLookupException
    {
        SessionFactory userEntityManagerFactory = mocker.getInstance(SessionFactory.class, "user");
        UserEntityManager userEntityManager = mock(UserEntityManager.class);
        when(userEntityManagerFactory.openSession()).thenReturn(userEntityManager);

        UserEntity userOne = mock(UserEntity.class);
        when(userEntityManager.createNewUser("sorinello")).thenReturn(userOne);

        Assert.assertEquals(1, mocker.getComponentUnderTest().getProcessEngine().getRepositoryService()
            .createProcessDefinitionQuery().count());
    }

    @Test
    public void testDefaultActivitiEngine() throws Exception
    {
        // System.out.print(scriptContextManager);
        // ScriptContextManager scm = mocker.getInstance(DefaultScriptContextManager.class, "default");

        // System.out.println("Component under test: " + mocker.getComponentUnderTest());
        // when(scm.getScriptContext().getBindings(ScriptContext.ENGINE_SCOPE)).thenReturn(( mocker);
        // Assert that we have only one process deployed in the engine
        // Assert.assertEquals(3, mocker.getComponentUnderTest().getProcessEngine().getRepositoryService()
        // .createProcessDefinitionQuery().count());
        // mocker.getComponentUnderTest().getProcessEngine();

        // // Assert that the process deployed has the key "myProcess"
        // Assert.assertEquals("demoProcess", mocker.getComponentUnderTest().getProcessEngine().getRepositoryService()
        // .createProcessDefinitionQuery().singleResult().getKey());
        //
        // Assert.assertEquals("Demo Process", mocker.getComponentUnderTest().getProcessEngine().getRepositoryService()
        // .createProcessDefinitionQuery().singleResult().getName());
        //
        // Assert
        // .assertEquals(
        // "This is a demo process. It is automatically deployed at runtime when the XWiki-Activiti component is initialized.It is used for demonstration purposes to show that is working when assigning a task to a User directly, to a list of Users or to a Goup. It also uses forms with all the Activiti internal data types in conjuction with the custom implemented Form Rendering",
        // mocker.getComponentUnderTest().getProcessEngine().getRepositoryService().createProcessDefinitionQuery()
        // .singleResult().getDescription());

    }
}
