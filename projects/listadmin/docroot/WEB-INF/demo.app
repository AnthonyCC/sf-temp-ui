<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE application PUBLIC
  "-//Apache Software Foundation//Tapestry Specification 3.0//EN"
  "http://jakarta.apache.org/tapestry/dtd/Tapestry_3_0.dtd">

<application name="demo" engine-class="org.apache.tapestry.engine.BaseEngine">
    <description>add a description</description>
    
    <page name="Home" specification-path="Home.page"/>

    <library
      id="myws"
      specification-path="/myws/tapestry/components/MyWorkspace.library"/>

</application>
