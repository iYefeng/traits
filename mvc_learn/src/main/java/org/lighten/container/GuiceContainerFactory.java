package org.lighten.container;

import com.google.inject.*;
import org.lighten.Config;
import org.lighten.ConfigException;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yefeng on 16/11/27.
 */
public class GuiceContainerFactory implements ContainerFactory {

    private Injector injector_ = null;

    public void init(Config config) {
        String value = config.getInitParameter("modules");
        if (value==null)
            throw new ConfigException("Init guice failed. Missing parameter '<modules>'.");
        String[] ss = value.split(",");
        List<Module> moduleList = new ArrayList<Module>(ss.length);
        Module m = null;
        for (String s : ss) {
            m = initModule(s, config.getServletContext());
            if (m != null)
                moduleList.add(m);
        }
        if (moduleList.isEmpty())
            throw new ConfigException("No module found.");
        this.injector_ = Guice.createInjector(Stage.PRODUCTION, moduleList);

    }

    Module initModule(String s, ServletContext context) {
        s = s.trim();
        if (s.length() > 0) {
            try {
                Object m = Class.forName(s).newInstance();
                if (m instanceof Module) {
                    return (Module) m;
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Object> findAllBeans() {
        Map<Key<?>, Binding<?>> kv = injector_.getAllBindings();
        Set<Key<?>> keys = kv.keySet();
        List<Object> list = new ArrayList<Object>(keys.size());
        Object bean = null;
        for (Key<?> key : keys) {
            bean = injector_.getBinding(key);
            list.add(bean);
        }
        return list;
    }

    public void destroy() {

    }
}
