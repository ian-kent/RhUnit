package uk.co.iankent.RhUnit.assertors;

import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Ian
 * Date: 14/09/12
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Assertor {
    public String value();
}
