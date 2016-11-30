package org.lighten;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by yefeng on 16/11/28.
 */
public class Execution {

    public final HttpServletRequest request_;
    public final HttpServletResponse response_;
    private final Action action_;
    private final Object[] args_;

    public Execution(HttpServletRequest request,
                     HttpServletResponse response,
                     Action action,
                     Object[] args) {
        this.request_ = request;
        this.response_ = response;
        this.action_ = action;
        this.args_ = args;
    }

    public Object execute() throws Exception {
        try {
            return action_.method.invoke(action_.instance, args_);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t!=null && t instanceof Exception)
                throw (Exception) t;
            throw e;
        }
    }

}
