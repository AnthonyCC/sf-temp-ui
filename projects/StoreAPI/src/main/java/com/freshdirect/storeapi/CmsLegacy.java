package com.freshdirect.storeapi;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Types annotated with CmsLegacy are subjects
 * of removal with the legacy CMS project.
 *
 * @author segabor
 *
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
public @interface CmsLegacy {

}
