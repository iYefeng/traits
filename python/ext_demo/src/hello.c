#include <Python.h>
#include <stdio.h>

int cnt = 0;
char buf[200];

static PyObject* helloworld(PyObject* self)
{
    cnt ++;
    sprintf(buf, "Hello, Pythoner, %d", cnt);
    return Py_BuildValue("s", buf);
}

static char helloworld_docs[] =
"helloworld( ): Any message you want to put here!!\n";

static PyMethodDef helloworld_funcs[] = {
    {"helloworld", (PyCFunction)helloworld, METH_NOARGS, helloworld_docs},
    {NULL}
};

void inithelloworld(void)
{
    Py_InitModule3("helloworld", helloworld_funcs, 
            "Extension module example!");
}
