package org.lighten;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * Created by yefeng on 16/11/26.
 */
public class DispatcherServlet extends GenericServlet {

    private final Logger log_ = Logger.getLogger(getClass().getName());

    private Dispatcher dispatcher_;

    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        log_.info(">> Init DispatcherServlet");
        dispatcher_ = new Dispatcher();
        dispatcher_.init(
            new Config() {
                public String getInitParameter(String name) {
                    return config.getInitParameter(name);
                }

                public ServletContext getServletContext() {
                    return config.getServletContext();
                }
            }
        );
    }

    public void service(ServletRequest req, ServletResponse resp)
            throws ServletException, IOException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        if (dispatcher_.service(httpReq, httpResp)) {

            return;
        }
        httpResp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    public void destroy() {
        log_.info("<< Destroy DispatcherServlet");
        dispatcher_.destroy();
    }

}
