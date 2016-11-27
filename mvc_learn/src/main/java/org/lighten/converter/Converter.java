package org.lighten.converter;

/**
 * Created by yefeng on 16/11/27.
 */
public interface Converter<T> {

    /**
     * Convert a not-null String to specified object.
     */
    T convert(String s);

}
