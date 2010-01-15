package com.freshdirect.cms.ui.client.changehistory;

import java.util.Date;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.freshdirect.cms.ui.client.CmsGwt;
import com.freshdirect.cms.ui.client.fields.OneToOneRelationField;
import com.freshdirect.cms.ui.client.views.ChangeSetQueryView;
import com.freshdirect.cms.ui.model.changeset.ChangeSetQuery;


public class ChangeSetQueryForm extends ContentPanel {
	
	private static ChangeSetQueryForm instance;
	
	public static ChangeSetQueryForm getInstance() {
		if ( instance == null ) {
			instance = new ChangeSetQueryForm();
		}
		return instance;
	}
	
	TextField<String>		userField;
	DateField				startDateField;
	DateField				endDateField;
	OneToOneRelationField	nodeField;
	TextField<String>		publishIdField;
	
	private ChangeSetQueryForm() {
		
		setHeading( "Query change sets" );
		
		FormLayout layout = new FormLayout( LabelAlign.LEFT );
		setLayout( layout );
		
		userField = new TextField<String>();
		userField.setFieldLabel( "User" );
		userField.setEmptyText( "enter username here" );
		userField.setValue( CmsGwt.getCurrentUser().getName() );
		userField.addPlugin(new FieldClearPlugin());
		add( userField );
		
		startDateField = new DateField();
		startDateField.setFieldLabel( "Start date" );
		startDateField.setEmptyText( "enter start date here" );
		startDateField.addPlugin(new FieldClearPlugin());
		add( startDateField );

		endDateField = new DateField();
		endDateField.setFieldLabel( "End date" );
		endDateField.setEmptyText( "enter end date here" );
		endDateField.addPlugin(new FieldClearPlugin());
		add( endDateField );

		nodeField = new OneToOneRelationField(null,false);
		nodeField.setFieldLabel( "Content node" );
		nodeField.setEmptyText( "enter contentkey here" );
		add( nodeField );
		
		Button queryButton = new Button( "Query" );
		queryButton.addListener( Events.Select, new QueryListener() );
		add( queryButton, new AnchorData("100%") );
		
	}
	
	
	public ChangeSetQuery getQuery() {
		ChangeSetQuery query = new ChangeSetQuery();
		
		query.setContentKey( nodeField.getValue() == null ? null : nodeField.getValue().getKey() );
		query.setUser( userField.getValue() );
		query.setStartDate( startDateField.getValue() );

		if ( endDateField.getValue() != null ) {
			Date endDate = (Date)endDateField.getValue().clone();
			endDate.setDate( endDate.getDate() + 1 );
			query.setEndDate( endDate );
		} else {
			query.setEndDate( null );
		}
		
		query.setChangeSetQuery( true );
		
		return query;
	}
	
	private class QueryListener implements Listener<BaseEvent> {
		public void handleEvent(BaseEvent be) {
			ChangeSetQueryView.getInstance().query( getQuery() );
		};		
	}
	
}
