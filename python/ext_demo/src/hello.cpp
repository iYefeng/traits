#include <Python.h>
#include <stdio.h>

int cnt = 0;
char buf[200];

#ifdef __cplusplus
extern "C" {
#endif

static PyObject* helloworld(PyObject* self)
{
    cnt ++;
    sprintf(buf, "Hello, Pythoner, %d", cnt);
    return Py_BuildValue("s", buf);
}

static PyObject* operation(PyObject* self, PyObject *args)
{
    int a;
    int b;
    PyObject *call;
    PyObject *result = NULL;
    PyObject *arglist;

    if (PyArg_ParseTuple(args, "iiO:set_callback", &a, &b, &call)) {
        if (!PyCallable_Check(call)) {
            PyErr_SetString(PyExc_TypeError, "parameter must be callable");
            return NULL;
        }

        arglist = Py_BuildValue("(ii)", a, b);
        result = PyObject_CallObject(call, arglist);
        Py_DECREF(arglist);
        return result;
    }
    return result;
}

static char helloworld_docs[] =
    "helloworld( ): Any message you want to put here!!\n";

static PyMethodDef helloworld_funcs[] = {
    {"helloworld", (PyCFunction)helloworld, METH_NOARGS, helloworld_docs},
    {"operation", (PyCFunction)operation, METH_VARARGS, helloworld_docs},
    {NULL, NULL, 0, NULL}
};

void inithelloworld(void)
{
    Py_InitModule3("helloworld", helloworld_funcs, 
            "Extension module example!");
}

#ifdef __cplusplus
}
#endif
