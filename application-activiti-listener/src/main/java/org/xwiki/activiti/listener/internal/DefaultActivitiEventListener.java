package org.xwiki.activiti.listener.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.activiti.ActivitiEngine;
import org.xwiki.activiti.XWikiActivitiBridge;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.bridge.event.DocumentCreatedEvent;
import org.xwiki.bridge.event.DocumentDeletedEvent;
import org.xwiki.bridge.event.DocumentUpdatedEvent;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.DocumentReferenceResolver;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.Event;
import org.xwiki.observation.event.FilterableEvent;
import org.xwiki.observation.event.filter.EventFilter;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryManager;
import org.xwiki.stability.Unstable;

@Unstable
@Component("activitiListener")
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
    private XWikiActivitiBridge bridge;

    @Inject
    private Logger logger;

    @Inject
    private EntityReferenceSerializer<String> entityReferenceSerializer;

    private String startMappingQuery =
        "select mapping.processId from Document doc, doc.object(Activiti.EventStartMappingClass) as mapping where mapping.event = :event and mapping.space = :space";

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
        String documentReferenceFromEvent = filter.getFilter();
        DocumentReference documentReferenceInternal = documentReferenceResolver.resolve(documentReferenceFromEvent);
        String documentReference = documentReferenceFromEvent;
        String documentName = documentReferenceInternal.getName();
        String documentSpace = documentReferenceInternal.getParent().getName();
        // String documentURL = bridge.getXWikiDocumentURL(documentReference);
        DocumentReference usernameReferenceInternal = documentAccessBridge.getCurrentUserReference();
        String usernameReference = this.entityReferenceSerializer.serialize(usernameReferenceInternal);
        // String usernameURL = bridge.getXWikiDocumentURL(usernameReference);
        // Boolean documentExists = documentAccessBridge.exists(documentReference);

        // Query to get the List of Processes to be started

        if (event instanceof DocumentCreatedEvent) {
            this.computeOnEvent("DocumentCreatedEvent", documentReference, documentName, documentSpace,
                usernameReference);
        }

        if (event instanceof DocumentUpdatedEvent) {
            this.computeOnEvent("DocumentUpdatedEvent", documentReference, documentName, documentSpace,
                usernameReference);
        }

        if (event instanceof DocumentDeletedEvent) {
            this.computeOnEvent("DocumentDeletedEvent", documentReference, documentName, documentSpace,
                usernameReference);
        }

    }

    private void computeOnEvent(String event, String documentReference, String documentName, String documentSpace,
        String usernameReference)
    {
        try {
            List<String> startMappingResults =
                queryManager.createQuery(this.startMappingQuery, Query.XWQL).bindValue("event", event)
                    .bindValue("space", documentSpace).execute();
            List<String> signalMappingResults =
                queryManager.createQuery(this.signalMappingQuery, Query.XWQL).bindValue("event", event)
                    .bindValue("space", documentSpace).execute();
            if (!startMappingResults.isEmpty() || !signalMappingResults.isEmpty()) {
                ActivitiEngine activiti = componentManager.getInstance(ActivitiEngine.class);

                for (int i = 0; i < startMappingResults.size(); i++) {
                    Map<String, Object> variables = new HashMap<String, Object>();
                    variables.put("documentReference", documentReference);
                    variables.put("documentName", documentName);
                    variables.put("documentSpace", documentSpace);
                    variables.put("usernameReference", usernameReference);
                    activiti.getProcessEngine().getRuntimeService()
                        .startProcessInstanceById(startMappingResults.get(i), variables);

                    logger.info("Process Variables: " + variables);
                    logger.info(event + " fired in space " + documentSpace + " on document " + documentName
                        + " by user " + usernameReference + ". Starting Process: " + startMappingResults.get(i)
                        + " to Activiti Engine");
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
