/*
 * Created on Feb 25, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.Map;

import org.apache.tapestry.BaseComponent;

import com.freshdirect.cms.ITable;

/**
 * @author vszathmary
 */
public abstract class ReportTable extends BaseComponent {

	public Map getTableGroups() {

		return TableUtil.getTableGroups(getTable());

	}

	public abstract ITable getTable();

}