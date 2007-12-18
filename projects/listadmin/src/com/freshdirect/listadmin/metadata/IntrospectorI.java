package com.freshdirect.listadmin.metadata;

// This should be a general interface for things that return
// metadata.  One for sql, one for hibernate, one that talks
// to ContentTypeServices and so on...
public interface IntrospectorI {
	public void introspect();
}
