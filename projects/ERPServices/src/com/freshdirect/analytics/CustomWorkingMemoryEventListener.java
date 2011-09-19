package com.freshdirect.analytics;

import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;

public class CustomWorkingMemoryEventListener extends
		DebugWorkingMemoryEventListener {

	@Override
	public void objectInserted(ObjectInsertedEvent event) {
		// TODO Auto-generated method stub
		super.objectInserted(event);
	}

	@Override
	public void objectRetracted(ObjectRetractedEvent event) {
		// TODO Auto-generated method stub
		super.objectRetracted(event);
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event) {
		// TODO Auto-generated method stub
		super.objectUpdated(event);
	}

	
	
}
