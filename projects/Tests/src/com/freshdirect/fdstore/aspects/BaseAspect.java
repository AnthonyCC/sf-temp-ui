package com.freshdirect.fdstore.aspects;

import org.mockejb.interceptor.Aspect;
import org.mockejb.interceptor.Pointcut;

public abstract class BaseAspect implements Aspect {

    Pointcut pointCut;

    public BaseAspect() {
    }

    public BaseAspect(Pointcut pc) {
        this.pointCut = pc;
    }

    public Pointcut getPointcut() {
        return pointCut;
    }

}
