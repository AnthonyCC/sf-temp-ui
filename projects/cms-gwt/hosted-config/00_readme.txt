Place your hosted mode configuration files here.

You have to place the following files:
CMSServiceConfig_gwt.xml (a modified version of CMSServiceConfig.xml, which accesses a database directly (avoiding using JNDI)
freshdirect.properties (property pointing to the overridden registry below)
freshdirect.registry (a modified version of the registry containing reference to CMSServiceConfig_gwt.xml)
log4j.xml (mandatory needed to hack the class path of the hosting Jetty server)

