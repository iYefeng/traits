package org.lighten;

import org.lighten.container.ContainerFactory;
import org.lighten.converter.ConverterFactory;
import org.lighten.render.Renderer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by yefeng on 16/11/26.
 */
public class Dispatcher {

    private ServletContext servletContext_;
    private final Logger log_ = Logger.getLogger(getClass().getName());
    private ContainerFactory containerFactory_ = null;
    private ConverterFactory converterFactory = new ConverterFactory();
    private UrlMatcher[] urlMatchers_ = null;
    private Map<UrlMatcher, Action> urlMap_ = new HashMap<UrlMatcher, Action>();

    public void init(Config config) throws ServletException {
        log_.info(">> Init Dispatcher");
        this.servletContext_ = config.getServletContext();
        try {
            initAll(config);
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Dispatcher init failed.", e);
        }
    }

    void initAll(Config config) throws Exception {

        // init IoC container
        String containerName = config.getInitParameter("container");
        if (containerName == null) {
            throw new ConfigException("Missing init parameter <container>.");
        }
        this.containerFactory_ = Utils.createContainerFactory(containerName);
        this.containerFactory_.init(config);
        List<Object> beans = this.containerFactory_.findAllBeans();

        initComponents(beans);

    }

    void initComponents(List<Object> beans) {
        for (Object bean : beans) {
            addActions(bean);
        }

        this.urlMatchers_ = urlMap_.keySet().toArray(new UrlMatcher[urlMap_.size()]);
        // sort url matchers by its url:
        Arrays.sort(
                this.urlMatchers_,
                new Comparator<UrlMatcher>() {
                    public int compare(UrlMatcher o1, UrlMatcher o2) {
                        String u1 = o1.url;
                        String u2 = o2.url;
                        int n = u1.compareTo(u2);
                        if (n==0)
                            throw new ConfigException("Cannot mapping one url '" + u1 + "' to more than one action method.");
                        return n;
                    }
                }
        );
    }

    // find all action methods and add them into urlMap_:
    void addActions(Object bean) {
        Class<?> clazz = bean.getClass();
        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            if (isActionMethod(m)) {
                Mapping mapping = m.getAnnotation(Mapping.class);
                String url = mapping.value();
                System.out.println("method url = " + url);
                UrlMatcher matcher = new UrlMatcher(url);
                if (matcher.getArgumentCount() != m.getParameterTypes().length) {
                    warnInvalidActionMethod(m, "Arguments in URL '" + url + "' does not match the arguments of method.");
                    continue;
                }
                log_.info("Mapping url '" + url + "' to method '" + m.toGenericString() + "'.");
                urlMap_.put(matcher, new Action(bean, m));
            }
        }
    }

    // check if the specified method is a vaild action method:
    boolean isActionMethod(Method m) {
        Mapping mapping = m.getAnnotation(Mapping.class);
        if (mapping == null)
            return false;
        if (mapping.value().length() == 0) {
            warnInvalidActionMethod(m, "Url mapping cannot be empty.");
            return false;
        }
        if (Modifier.isStatic(m.getModifiers())) {
            warnInvalidActionMethod(m, "method is static.");
            return false;
        }
        Class<?>[] argTypes = m.getParameterTypes();
        for (Class<?> argType : argTypes) {
            if (!converterFactory.canConvert(argType)) {
                warnInvalidActionMethod(m, "unsupported parameter '" + argType.getName() + "'.");
                return false;
            }
        }
        Class<?> retType = m.getReturnType();
        if (retType.equals(void.class)
                || retType.equals(String.class)
                || Renderer.class.isAssignableFrom(retType)
                )
            return true;
        warnInvalidActionMethod(m, "unsupported return type '" + retType.getName() + "'.");
        return false;
    }

    // log warning message of invalid action method
    void warnInvalidActionMethod(Method m, String string) {
        log_.warning("Invalid Action method '" + m.toGenericString() + "': " + string);
    }


    public boolean service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log_.info(">> [Dispatcher] Handle request");

//        // 设置响应内容类型
//        resp.setContentType("text/html");
//        // 实际的逻辑是在这里
//        PrintWriter out = resp.getWriter();
//        out.println("hello world");

        String url = req.getRequestURI();
        String path = req.getContextPath();
        if (path.length() > 0) {
            url = url.substring(path.length());
        }
        System.out.println(url);
        Execution execution = null;
        System.out.println("matcher num=" + urlMatchers_.length);
        for (UrlMatcher matcher : this.urlMatchers_) {
            System.out.println(matcher.url);
            String[] args = matcher.getMatchedParameters(url);
            System.out.println(args);
            if (args!=null) {
                Action action = urlMap_.get(matcher);
                Object[] arguments = new Object[args.length];
                for (int i=0; i<args.length; i++) {
                    Class<?> type = action.arguments[i];
                    if (type.equals(String.class))
                        arguments[i] = args[i];
                    else
                        arguments[i] = converterFactory.convert(type, args[i]);
                }
                execution = new Execution(req, resp, action, arguments);
                break;
            }
        }
        if (execution!=null) {
            handleExecution(execution, req, resp);
        }
        log_.info("<< [Dispatcher] Handle request");
        return execution!=null;
    }


    public void handleExecution(Execution execution, HttpServletRequest request, HttpServletResponse response) {
        try {
            Object result = execution.execute();
            if (result instanceof String) {
                String text = (String) result;
                System.out.println(text);
                response.setContentType("text/html");
                // 实际的逻辑是在这里
                PrintWriter out = response.getWriter();
                out.println(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void destroy() {
        log_.info("<< Destroy Dispatcher");
    }
}
