package org.xwiki.activiti.documentWorkflow;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class DescribeArticleTaskListener implements TaskListener
{

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask)
    {
        DelegateExecution execution = delegateTask.getExecution();
        System.out.println("Describe Article Task Listener");
        delegateTask.setAssignee((String) execution.getVariable("usernameReference"));
        delegateTask.setDescription("Fill information for article " + execution.getVariable("documentName"));
        System.out.println("Dynamically assigning Task to " + delegateTask.getAssignee());
    }
}
