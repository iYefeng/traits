package org.lighten.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yefeng on 16/11/27.
 */
public class ConverterFactory {

    Map<Class<?>, Converter<?>> map = new HashMap<Class<?>, Converter<?>>();

    public ConverterFactory() {

    }

    public boolean canConvert(Class<?> clazz) {
        return clazz.equals(String.class) || map.containsKey(clazz);
    }

    public Object convert(Class<?> clazz, String s) {
        Converter<?> c = map.get(clazz);
        return c.convert(s);
    }

}
