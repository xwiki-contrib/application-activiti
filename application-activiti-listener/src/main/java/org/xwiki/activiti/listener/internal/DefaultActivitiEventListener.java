package org.xwiki.activiti.listener.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.bridge.event.DocumentDeletedEvent;
import org.xwiki.bridge.event.DocumentUpdatedEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.observation.event.FilterableEvent;
import org.xwiki.observation.event.filter.EventFilter;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryManager;

@Component("activiti")
@Singleton
public class DefaultActivitiEventListener implements EventListener
{
    @Inject
    private ComponentManager componentManager;

    @Inject
    private DocumentReferenceResolver<String> documentReferenceResolver;

    @Inject
    private DocumentAccessBridge documentAccessBridge;

    @Inject
    private QueryManager queryManager;

    @Inject
    private Logger logger;

    @Override
    public List<Event> getEvents()
    {
        return Arrays
            .<Event> asList(new DocumentCreatedEvent(), new DocumentUpdatedEvent(), new DocumentDeletedEvent());
    }

    @Override
    public String getName()
    {
        return "activitiEventListener";
    }

    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        FilterableEvent filterableEvent = (FilterableEvent) event;
        EventFilter filter = filterableEvent.getEventFilter();
        String documentName = filter.getFilter();
        DocumentReference documentReference = documentReferenceResolver.resolve(documentName);
        String documentSpace = documentReference.getParent().getName();
        Boolean documentExists = documentAccessBridge.exists(documentReference);

        String queryStatement =
            "select mapping.message from Document doc, doc.object(Activiti.EventListenerMappingClass) as mapping where mapping.event = :event and mapping.space = :space";

        if (event instanceof DocumentCreatedEvent) {

            try {
                List<String> queryResults =
                    queryManager.createQuery(queryStatement, Query.XWQL).bindValue("event", "DocumentCreatedEvent")
                        .bindValue("space", documentSpace).execute();
                if (!queryResults.isEmpty()) {
                    ActivitiEngine activiti = componentManager.getInstance(ActivitiEngine.class);

                    for (int i = 0; i < queryResults.size(); i++) {
                        ProcessInstance processInstance =
                            activiti.getProcessEngine().getRuntimeService()
                                .startProcessInstanceByMessage(queryResults.get(i));
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Name", documentName);
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Space", documentSpace);
                        logger.info("DocumentCreatedEvent fired. Document: " + documentName + " was created in space: "
                            + documentSpace + ". Sending message " + queryResults.get(i) + " to Activiti Engine");
                    }
                }
            } catch (QueryException e) {
                logger.error("Query failed. ", e);
            } catch (ComponentLookupException e) {
                logger.error("Unable to get Activiti Engine. ", e);
                e.printStackTrace();
            }
        }

        if (event instanceof DocumentUpdatedEvent) {
            try {
                List<String> queryResults =
                    queryManager.createQuery(queryStatement, Query.XWQL).bindValue("event", "DocumentUpdatedEvent")
                        .bindValue("space", documentSpace).execute();
                if (!queryResults.isEmpty()) {
                    ActivitiEngine activiti = componentManager.getInstance(ActivitiEngine.class);

                    for (int i = 0; i < queryResults.size(); i++) {
                        ProcessInstance processInstance =
                            activiti.getProcessEngine().getRuntimeService()
                                .startProcessInstanceByMessage(queryResults.get(i));
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Name", documentName);
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Space", documentSpace);
                        logger.info("DocumentUpdatedEvent fired. Document: " + documentName + " was updated in space: "
                            + documentSpace + ". Sending message " + queryResults.get(i) + " to Activiti Engine");
                    }
                }
            } catch (QueryException e) {
                logger.error("Query failed. ", e);
            } catch (ComponentLookupException e) {
                logger.error("Unable to get Activiti Engine. ", e);
                e.printStackTrace();
            }
        }

        if (event instanceof DocumentDeletedEvent) {
            try {
                List<String> queryResults =
                    queryManager.createQuery(queryStatement, Query.XWQL).bindValue("event", "DocumentDeletedEvent")
                        .bindValue("space", documentSpace).execute();
                if (!queryResults.isEmpty()) {
                    ActivitiEngine activiti = componentManager.getInstance(ActivitiEngine.class);

                    for (int i = 0; i < queryResults.size(); i++) {
                        ProcessInstance processInstance =
                            activiti.getProcessEngine().getRuntimeService()
                                .startProcessInstanceByMessage(queryResults.get(i));
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Name", documentName);
                        activiti.getProcessEngine().getRuntimeService()
                            .setVariable(processInstance.getId(), "document Space", documentSpace);
                        logger.info("DocumentDeletedEvent fired. Document: " + documentName + " was deleted in space: "
                            + documentSpace + ". Sending message " + queryResults.get(i) + " to Activiti Engine");
                    }
                }
            } catch (QueryException e) {
                logger.error("Query failed. ", e);
            } catch (ComponentLookupException e) {
                logger.error("Unable to get Activiti Engine. ", e);
                e.printStackTrace();
            }
        }
    }
}
