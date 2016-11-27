package org.lighten;

import org.lighten.container.ContainerFactory;

import javax.servlet.ServletException;

/**
 * Created by yefeng on 16/11/27.
 */
public class Utils {


    public static ContainerFactory createContainerFactory(String name) throws ServletException {
        ContainerFactory cf = tryInitContainerFactory(name);
        if (cf == null)
            cf = tryInitContainerFactory(ContainerFactory.class.getPackage().getName() + "."
                    + name + ContainerFactory.class.getSimpleName());
        if (cf == null)
            throw new ConfigException("Cannot create container factory by name '" + name + "'.");
        return cf;
    }

    static ContainerFactory tryInitContainerFactory(String clazz) {
        try {
            Object obj = Class.forName(clazz).newInstance();
            if (obj instanceof ContainerFactory)
                return (ContainerFactory) obj;
        }
        catch (Exception e) { }
        return null;
    }

}
