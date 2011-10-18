package com.freshdirect.webapp.taglib.buildver;

import com.freshdirect.fdstore.util.Buildver;

import junit.framework.TestCase;

public class AbstractBuildverTagTest extends TestCase {
	BuildverMock buildverMock = new BuildverMock();

	@Override
	protected void setUp() throws Exception {
		Buildver.mockInstance(buildverMock);
	}

	public void testUseNotMinified() {
		buildverMock.setUseMinified(false);
		assertEquals("http://alpha-moon-base.com/yui.js", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js"));
		assertEquals("http://alpha-moon-base.com/yui.js", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js"));
		assertEquals("http://alpha-moon-base.com/yui.css", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css"));
		assertEquals("http://alpha-moon-base.com/yui-min.png", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png"));

		assertEquals("http://alpha-moon-base.com/yui.js?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js?"));
		assertEquals("http://alpha-moon-base.com/yui.js?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js?"));
		assertEquals("http://alpha-moon-base.com/yui.css?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css?"));
		assertEquals("http://alpha-moon-base.com/yui-min.png?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png?"));
		
		assertEquals("http://alpha-moon-base.com/yui.js?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui.js?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui.css?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui-min.png?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png?param1=value1"));
	}

	public void testUseMinified() {
		buildverMock.setUseMinified(true);
		assertEquals("http://alpha-moon-base.com/yui-min.js", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js"));
		assertEquals("http://alpha-moon-base.com/yui-min.js", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js"));
		assertEquals("http://alpha-moon-base.com/yui-min.css", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css"));
		assertEquals("http://alpha-moon-base.com/yui-min.png", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png"));
		assertEquals("http://alpha-moon-base.com/yui.png", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.png"));

		assertEquals("http://alpha-moon-base.com/yui-min.js?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js?"));
		assertEquals("http://alpha-moon-base.com/yui-min.js?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js?"));
		assertEquals("http://alpha-moon-base.com/yui-min.css?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css?"));
		assertEquals("http://alpha-moon-base.com/yui-min.png?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png?"));
		assertEquals("http://alpha-moon-base.com/yui.png?", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.png?"));
		
		assertEquals("http://alpha-moon-base.com/yui-min.js?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.js?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui-min.js?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.js?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui-min.css?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.css?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui-min.png?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui-min.png?param1=value1"));
		assertEquals("http://alpha-moon-base.com/yui.png?param1=value1", AbstractBuildverTag.preProcessUri("http://alpha-moon-base.com/yui.png?param1=value1"));
	}
}
