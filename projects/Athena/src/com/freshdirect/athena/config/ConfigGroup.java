package com.freshdirect.athena.config;

import java.io.Serializable;
import java.util.List;

public class ConfigGroup implements Serializable {
	
	AthenaConfig athenaConfig;
	List<DatasourceConfig> dsConfigs;
	List<ApiConfig> serviceConfigs;
	
	public ConfigGroup(AthenaConfig athenaConfig,
			List<DatasourceConfig> dsConfigs, List<ApiConfig> serviceConfigs) {
		super();
		this.athenaConfig = athenaConfig;
		this.dsConfigs = dsConfigs;
		this.serviceConfigs = serviceConfigs;
	}

	public AthenaConfig getAthenaConfig() {
		return athenaConfig;
	}

	public List<DatasourceConfig> getDsConfigs() {
		return dsConfigs;
	}

	public List<ApiConfig> getServiceConfigs() {
		return serviceConfigs;
	}

	@Override
	public String toString() {
		return "ConfigLoadResult [athenaConfig=" + athenaConfig
				+ ", dsConfigs=" + dsConfigs + ", serviceConfigs="
				+ serviceConfigs + "]";
	}
}
