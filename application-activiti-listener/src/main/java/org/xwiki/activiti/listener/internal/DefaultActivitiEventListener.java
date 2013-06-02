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

    private String startMappingQuery =
        "select mapping.processId from Document doc, doc.object(Activiti.EventStartMappingClass) as mapping where mapping.event = :event and mapping.space = :space";

    private String messageMappingQuery =
        "select mapping.message from Document doc, doc.object(Activiti.EventMessageMappingClass) as mapping where mapping.event = :event and mapping.space = :space";

    private String signalMappingQuery =
        "select mapping.signal from Document doc, doc.object(Activiti.EventSignalMappingClass) as mapping where mapping.event = :event and mapping.space = :space";

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

        // Query to get the List of Processes to be started

        if (event instanceof DocumentCreatedEvent) {
            this.computeOnEvent("DocumentCreatedEvent", documentName, documentSpace);
        }

        if (event instanceof DocumentUpdatedEvent) {
            this.computeOnEvent("DocumentUpdatedEvent", documentName, documentSpace);
        }

        if (event instanceof DocumentDeletedEvent) {
            this.computeOnEvent("DocumentDeletedEvent", documentName, documentSpace);
        }

    }

    private void computeOnEvent(String event, String documentName, String documentSpace)
    {
        try {
            List<String> startMappingResults =
                queryManager.createQuery(this.startMappingQuery, Query.XWQL).bindValue("event", event)
                    .bindValue("space", documentSpace).execute();
            List<String> messageMappingResults =
                queryManager.createQuery(this.messageMappingQuery, Query.XWQL).bindValue("event", event)
                    .bindValue("space", documentSpace).execute();
            List<String> signalMappingResults =
                queryManager.createQuery(this.signalMappingQuery, Query.XWQL).bindValue("event", event)
                    .bindValue("space", documentSpace).execute();
            if (!startMappingResults.isEmpty() || !messageMappingResults.isEmpty() || !signalMappingResults.isEmpty()) {
                ActivitiEngine activiti = componentManager.getInstance(ActivitiEngine.class);

                for (int i = 0; i < startMappingResults.size(); i++) {
                    ProcessInstance processInstance =
                        activiti.getProcessEngine().getRuntimeService()
                            .startProcessInstanceById(startMappingResults.get(i));
                    activiti.getProcessEngine().getRuntimeService()
                        .setVariable(processInstance.getId(), "document Name", documentName);
                    activiti.getProcessEngine().getRuntimeService()
                        .setVariable(processInstance.getId(), "document Space", documentSpace);
                    logger.info(event + " fired in space " + documentSpace + " on document " + documentName
                        + ". Starting Process: " + startMappingResults.get(i) + " to Activiti Engine");
                }
                for (int i = 0; i < messageMappingResults.size(); i++) {

                    // activiti.getRuntimeService().

                    logger.info(event + " fired in space " + documentSpace + " on document " + documentName
                        + ". Sending message " + signalMappingResults.get(i) + " to Activiti Engine");
                }
                for (int i = 0; i < signalMappingResults.size(); i++) {
                    activiti.getRuntimeService().signalEventReceived(signalMappingResults.get(i));
                    logger.info(event + " fired in space " + documentSpace + " on document " + documentName
                        + ". Sending signal " + signalMappingResults.get(i) + " to Activiti Engine");
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
