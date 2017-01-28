package main

import (
	"fmt"
	"strings"
)

func main() {
	var str1 = "hello world";
	var str2 = "yefeng";
	fmt.Printf("%s\n", str1)
	fmt.Printf("len: %d\n", len(str1))
	fmt.Printf("%s\n", strings.Join([]string{str1, str2}, ", "))
}
