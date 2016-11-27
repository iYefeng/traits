package org.lightendemo.example1;

import org.lighten.Mapping;

/**
 * Created by yefeng on 16/11/27.
 */
public class Test {

    @Mapping("/hello")
    public String hello() {
        return "<h1>Hello, world</h1>";
    }

}
