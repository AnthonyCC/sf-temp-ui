package com.freshdirect.analytics;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.DebugAgendaEventListener;

public class CustomAgendaEventListener extends DebugAgendaEventListener {

	@Override
	public void activationCancelled(ActivationCancelledEvent event) {
		// TODO Auto-generated method stub
		super.activationCancelled(event);
		System.err.println("activationCancelled");
	}

	@Override
	public void activationCreated(ActivationCreatedEvent event) {
		// TODO Auto-generated method stub
		super.activationCreated(event);
		System.err.println("activationCreated");
	}

	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		// TODO Auto-generated method stub
		super.afterActivationFired(event);
		System.err.println("afterActivationFired");
	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		// TODO Auto-generated method stub
		super.agendaGroupPopped(event);
		System.err.println("agendaGroupPopped");
	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		// TODO Auto-generated method stub
		super.agendaGroupPushed(event);
		System.err.println("agendaGroupPushed");
	}

	@Override
	public void beforeActivationFired(BeforeActivationFiredEvent event) {
		// TODO Auto-generated method stub
		super.beforeActivationFired(event);
		System.err.println("beforeActivationFired");
	}

}
