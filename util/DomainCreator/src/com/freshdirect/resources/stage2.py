#
# STAGE 2 Script
#
# @param domainName Domain name
# @param serverName
# @param vHostName
# @param vHostPort
#

# Constants
JMS_NAME='jmsServer'
MODULE_NAME='jmsModule'
SUBDEPL_NAME='subdepl'
QFTY_NAME='queueFactory'
FSTORE_NAME='fileStore'

# Define target
connect(wl_user, wl_pwd, wl_url)

targ=jarray.array([ObjectName('com.bea:Name='+serverName+',Type=Server')], ObjectName)

edit()
startEdit()
cd('/')
cmo.createFileStore(FSTORE_NAME)

cd('/FileStores/'+FSTORE_NAME)
set('Targets',targ)

activate()


startEdit()

cd('/')
cmo.createJMSServer(JMS_NAME)

cd('/Deployments/'+JMS_NAME)
cmo.setPersistentStore(getMBean('/FileStores/'+FSTORE_NAME))
set('Targets',targ)

activate()


startEdit()

cd('/')
cmo.createJMSSystemResource(MODULE_NAME)

cd('/SystemResources/'+MODULE_NAME)
set('Targets',targ)

activate()


startEdit()
cmo.createSubDeployment(SUBDEPL_NAME)

cd('/SystemResources/'+MODULE_NAME+'/SubDeployments/'+SUBDEPL_NAME)
set('Targets',jarray.array([], ObjectName))

activate()


startEdit()

cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME)
cmo.createConnectionFactory(QFTY_NAME)

cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/ConnectionFactories/'+QFTY_NAME+'/SecurityParams/'+QFTY_NAME)
cmo.setAttachJMSXUserId(false)

cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/ConnectionFactories/'+QFTY_NAME)
cmo.setJNDIName('queueFactory')
cmo.setDefaultTargetingEnabled(true)

# MAKE TRANSACTIONAL
cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/ConnectionFactories/'+QFTY_NAME+'/TransactionParams/'+QFTY_NAME)
cmo.setTransactionTimeout(3600)
cmo.setXAConnectionFactoryEnabled(true)

activate()


startEdit()

cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME)
cmo.createTemplate('template')

activate()


# Queues
#
queues = ['captureQueue', 'mailQueue', 'oasQueue', 'registerQueue', 'releaseQueue', 'routingQueue', 'sapQueue']
for qname in queues:
    startEdit()

    cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME)
    cmo.createQueue(qname)
    cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/Queues/'+qname)
    cmo.setJNDIName(qname)

    
    cd('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/Queues/'+qname)
    cmo.setTemplate(getMBean('/JMSSystemResources/'+MODULE_NAME+'/JMSResource/'+MODULE_NAME+'/Templates/template'))
    cmo.setSubDeploymentName(SUBDEPL_NAME)
    
    cd('/SystemResources/'+MODULE_NAME+'/SubDeployments/'+SUBDEPL_NAME)
    set('Targets',jarray.array([ObjectName('com.bea:Name='+JMS_NAME+',Type=JMSServer')], ObjectName))
    
    activate()

##
## DataSources
##
sources = [
      {"name": "fddatasource",
       "url": "jdbc:oracle:thin:@zetor:1521:DBEU01",
       "user": "fdstore_prda",
       "password": "fdstore_prda"},
      {"name": "fdmktdatasource",
       "url": "jdbc:oracle:thin:@zetor:1521:DBEU01",
       "user": "fdstore_prda",
       "password": "fdstore_prda"},
      {"name": "fdtrndatasource",
       "url": "jdbc:oracle:thin:@zetor:1521:DBEU01",
       "user": "fdstore_prda",
       "password": "fdstore_prda"},
]

for ds in sources:
    startEdit()
    
    cd('/')
    cmo.createJDBCSystemResource(ds['name'])
    
    _respath = '/JDBCSystemResources/'+ds['name']+'/JDBCResource/'+ds['name']
    
    cd(_respath)
    cmo.setName(ds['name'])
    
    cd(_respath+'/JDBCDataSourceParams/'+ds['name'])
    set('JNDINames',jarray.array([], String))
    
    cd(_respath+'/JDBCDriverParams/'+ds['name'])
    cmo.setUrl(ds['url'])
    cmo.setDriverName('oracle.jdbc.OracleDriver')
    cmo.setPassword(ds['password'])

    cd(_respath+'/JDBCConnectionPoolParams/'+ds['name'])
    cmo.setTestTableName('SQL SELECT 1 FROM DUAL\r\n\r\n')
    
    cd(_respath+'/JDBCDriverParams/'+ds['name']+'/Properties/'+ds['name'])
    cmo.createProperty('user')
    
    cd(_respath+'/JDBCDriverParams/'+ds['name']+'/Properties/'+ds['name']+'/Properties/user')
    cmo.setValue(ds['user'])
    
    cd('/JDBCSystemResources/'+ds['name']+'/JDBCResource/'+ds['name']+'/JDBCDataSourceParams/'+ds['name'])
    cmo.setGlobalTransactionsProtocol('EmulateTwoPhaseCommit')
    
    cd('/SystemResources/'+ds['name'])
    set('Targets',targ)
    
    activate()


##
## Virtual Host for CRM App
##
# Create HTTP Channel for Virtual Host
startEdit()

cd('/Servers/'+serverName)
cmo.createNetworkAccessPoint('crm_channel')

cd('/Servers/'+serverName+'/NetworkAccessPoints/crm_channel')
cmo.setProtocol('http')
cmo.setListenPort(vHostPort)
cmo.setEnabled(true)
cmo.setHttpEnabledForThisProtocol(true)
cmo.setTunnelingEnabled(false)
cmo.setOutboundEnabled(false)
cmo.setTwoWaySSLEnabled(false)
cmo.setClientCertificateEnforced(false)

activate()

# Create Virtual Host Entry
startEdit()

cd('/')
cmo.createVirtualHost(vHostName)

activate()

startEdit()

cd('/VirtualHosts/'+vHostName)
cmo.setNetworkAccessPoint('crm_channel')
set('VirtualHostNames',jarray.array([String(vHostName)], String))

activate()

# save()
disconnect()
