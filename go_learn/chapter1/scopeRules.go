package main

import "fmt"

var g int
var g1 int = 100

func main() {
	/* local variable declaration */ 
	var a, b, c, d int
	var g1 int = 200

	/* actual initialization */ 
	a = 10
	b = 20
	c = a + b
	g = c * 2
	fmt.Printf ("value of a = %d, b = %d and c = %d\n", a, b, c)
	fmt.Printf ("value of g = %d\n", g)
	fmt.Printf ("value of g1 = %d\n", g1)
	fmt.Printf ("value of d = %d\n", d)
}
