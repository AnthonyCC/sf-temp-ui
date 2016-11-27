package com.freshdirect.cmsadmin;

import java.lang.reflect.InvocationTargetException;

import org.junit.runner.notification.RunNotifier;
import org.mockito.runners.MockitoJUnitRunner;

public class ConsoleMockitoJUnitRunner extends MockitoJUnitRunner {

    public ConsoleMockitoJUnitRunner(Class<?> klass) throws InvocationTargetException {
        super(klass);
    }

    @Override
    public void run(final RunNotifier notifier) {
        notifier.addListener(new MockitoTestExecutionListener());
        super.run(notifier);
    }

}
