package org.lighten;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yefeng on 16/11/27.<br/>
 * <pre>
 * public class Blog {
 *     &#064;Mapping("/")
 *     public String index() {
 *         // handle index page...
 *     }
 *
 *     &#064;Mapping("/blog/$1")
 *     public String show(int id) {
 *         // show blog with id...
 *     }
 *
 *     &#064;Mapping("/blog/edit/$1")
 *     public void edit(int id) {
 *         // edit blog with id...
 *     }
 * }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    String value();
}
