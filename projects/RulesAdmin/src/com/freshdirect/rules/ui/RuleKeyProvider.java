package com.freshdirect.rules.ui;

import java.io.Serializable;

import net.sf.tacos.model.IKeyProvider;

import com.freshdirect.rules.Rule;

/**
 * @author knadeem Date Mar 28, 2005
 */
public class RuleKeyProvider implements IKeyProvider {

	public final static RuleKeyProvider INSTANCE = new RuleKeyProvider();

	public Serializable getKey(Object element) {
		return ((Rule) element).getId();
	}

}
