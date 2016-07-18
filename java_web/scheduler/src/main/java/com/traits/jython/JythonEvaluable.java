package com.traits.jython;

import org.apache.log4j.Logger;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by YeFeng on 2016/7/17.
 */
public class JythonEvaluable {
    Logger logger = Logger.getLogger("jython");

    private final String s_functionName;

    private static PythonInterpreter _engine;

    static {
        File libPath = new File("scheduler/WEB-INF/lib/jython");
        if (!libPath.exists() && !libPath.canRead()) {
            libPath = new File("main/scheduler/WEB-INF/lib/jython");
            if (!libPath.exists() && !libPath.canRead()) {
                libPath = null;
            }
        }

        if (libPath != null) {
            Properties props = new Properties();
            props.setProperty("python.path", libPath.getAbsolutePath());
            props.put("python.console.encoding", "UTF-8");
            PythonInterpreter.initialize(System.getProperties(), props, new String[]{""});
        } else {
            Properties props = new Properties();
            props.put("python.console.encoding", "UTF-8");
            PythonInterpreter.initialize(System.getProperties(), props, new String[]{""});
        }

        _engine = new PythonInterpreter();
    }

    public JythonEvaluable(String s) {
        this.s_functionName = String.format("__temp_%d__", Math.abs(s.hashCode()));

        if (_engine.get(s_functionName) == null) {
            // indent and create a function out of the code
            String[] lines = s.split("\r\n|\r|\n");

            StringBuffer sb = new StringBuffer(102400);
            sb.append("# coding=utf-8\n");
            sb.append("import sys\n");
            sb.append("reload(sys)\n");
            sb.append("sys.setdefaultencoding('utf-8')\n");
            sb.append("def ");
            sb.append(s_functionName);
            sb.append("():");
            for (String line : lines) {
                sb.append("\n  ");
                sb.append(line);
            }

            logger.debug("script:\n" + sb.toString());

            _engine.execfile(new ByteArrayInputStream(sb.toString().getBytes()));
        }
    }

    public Object evaluate() {
        try {
            // call the temporary PyFunction directly
            Object result = ((PyFunction) _engine.get(s_functionName)).__call__();

            return unwrap(result);
        } catch (PyException e) {
            return new EvalError(e.toString());
        }
    }

    protected Object unwrap(Object result) {
        if (result != null) {
            if (result instanceof PyString) {
                return ((PyString) result).asString();
            } else if (result instanceof PyInteger) {
                return (long) ((PyInteger) result).asInt();
            } else if (result instanceof PyLong) {
                return ((PyLong) result).getLong(Long.MIN_VALUE, Long.MAX_VALUE);
            } else if (result instanceof PyFloat) {
                return ((PyFloat) result).asDouble();
            } else if (result instanceof  PyList) {
                List<Object> ret = new ArrayList<Object>();
                PyObject[] tmp = ((PyList) result).getArray();
                for (PyObject i : tmp) {
                    ret.add(unwrap(i));
                }
                return ret.toArray();
            } else if (result instanceof PyDictionary) {
                Map<String, Object> ret = new HashMap<String, Object>();
                ConcurrentMap<PyObject, PyObject> tmp = ((PyDictionary) result).getMap();
                for (Map.Entry<PyObject, PyObject> i : tmp.entrySet()) {
                    String key = String.valueOf(i.getKey());
                    Object value = unwrap(i.getValue());
                    ret.put(key, value);
                }
              return ret;
            } else if (result instanceof PyObject) {
                return unwrap((PyObject) result);
            }
        }

        return result;
    }

    protected Object unwrap(PyObject po) {

        if (po instanceof PyNone) {
            return null;
        } else if (po.getType().getName().equals("int")) {
            return ((PyInteger) po).asInt();
        } else if (po.getType().getName().equals("long")) {
            return ((PyLong) po).asLong();
        }else if (po.getType().getName().equals("float")) {
            return ((PyFloat) po).asDouble();
        } else if (po.getType().getName().equals("str")) {
            return ((PyString) po).asString();
        } else if (po.isSequenceType()) {
            Iterator<PyObject> i = po.asIterable().iterator();

            List<Object> list = new ArrayList<Object>();
            while (i.hasNext()) {
                list.add(unwrap((Object) i.next()));
            }

            return list.toArray();
        } else {
            return po;
        }
    }

}
