package org.ei.telemedicine.repository;

import org.ei.telemedicine.domain.TimelineEvent;

import java.util.List;

public class AllTimelineEvents {
    private TimelineEventRepository repository;

    public AllTimelineEvents(TimelineEventRepository repository) {
        this.repository = repository;
    }

    public List<TimelineEvent> forCase(String caseId) {
        return repository.allFor(caseId);
    }

    public void add(TimelineEvent timelineEvent) {
        repository.add(timelineEvent);
    }
}
