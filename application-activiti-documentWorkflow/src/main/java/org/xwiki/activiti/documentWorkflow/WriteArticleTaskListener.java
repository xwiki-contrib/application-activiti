package org.xwiki.activiti.documentWorkflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class WriteArticleTaskListener implements TaskListener
{

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask)
    {
        DelegateExecution execution = delegateTask.getExecution();
        System.out.println("Write Article Task Listener");
        delegateTask.setAssignee((String) execution.getVariable("usernameReference"));
        delegateTask.setDescription("Write article " + execution.getVariable("documentName"));
        System.out.println("Dynamically assigning Task to " + delegateTask.getAssignee());

    }
}
