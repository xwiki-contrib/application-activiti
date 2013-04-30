package org.xwiki.activiti.internal;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;

public class IdentityScriptService
{
    @Inject
    private Logger logger;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    private ActivitiEngine activitiEngine;

    IdentityScriptService(ActivitiEngine activitiEngine)
    {
        this.activitiEngine = activitiEngine;
    }
}
