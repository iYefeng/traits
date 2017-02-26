package main

/*
#include <stdio.h>
void hello() {
    printf("hello, Cgo!\n");
}
*/
import (
	"C"
)

func main() {
	C.hello()
}
