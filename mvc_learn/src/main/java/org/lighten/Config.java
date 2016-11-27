package org.lighten;

import javax.servlet.ServletContext;

/**
 * Created by yefeng on 16/11/26.
 */
public interface Config {
    /**
     * Get ServletContext object.
     */
    public ServletContext getServletContext();

    /**
     * Get init parameter value by name.
     */
    public String getInitParameter(String name);
}
