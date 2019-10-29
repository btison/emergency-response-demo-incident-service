package com.redhat.cajun.navy.incident.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.redhat.cajun.navy.incident.entity.OutboxEvent;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventEmitter {

    @PersistenceContext
    private EntityManager entityManager;

    public void emitEvent(OutboxEvent event) {
        entityManager.persist(event);
        entityManager.remove(event);
    }

}
