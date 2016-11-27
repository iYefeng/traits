package org.lighten;

import java.lang.reflect.Method;

/**
 * Created by yefeng on 16/11/27.
 */
public class Action {
    /**
     * Object instance.
     */
    public final Object instance;

    /**
     * Method instance.
     */
    public final Method method;

    /**
     * Method's arguments' types.
     */
    public final Class<?>[] arguments;

    public Action(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        this.arguments = method.getParameterTypes();
    }
}
