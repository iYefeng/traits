package org.lightendemo.example1;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * Created by yefeng on 16/11/27.
 */
public class Modules implements Module {

    public void configure(Binder binder) {
        binder.bind(Test.class).asEagerSingleton();
    }

}
