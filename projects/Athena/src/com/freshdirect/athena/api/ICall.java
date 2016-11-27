package com.freshdirect.athena.api;

import java.util.List;

import com.freshdirect.athena.config.Api;
import com.freshdirect.athena.config.Parameter;
import com.freshdirect.athena.data.Data;

public interface ICall {
	
	public abstract Data getData(Api api, List<Parameter> params);

}
