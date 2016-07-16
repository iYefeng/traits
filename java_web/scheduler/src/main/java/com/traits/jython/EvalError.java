package com.traits.jython;

import java.io.Serializable;

/**
 * Created by YeFeng on 2016/7/17.
 */
public class EvalError implements Serializable {

    final public String message;

    public EvalError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
