package main

import (
	"fmt"
	"math"
)

func max(x int, y int) int {
	if (x > y) {
		return x
	} else {
		return y
	}
}

func swap(x *int, y *int) {
	var temp int
	temp = *x;
	*x = *y
	*y = temp
}

func getSequence() func() int {
	var i int = 0;
	return func() int {
		i++;
		return i
	}
}

type Circle struct {
	x, y, radius float64
}

func(circle Circle) area() float64 {
	return math.Pi * circle.radius * circle.radius 
}

func main() {
	var  a, b int = 2, 3;
	fmt.Printf("max value is %d\n", max(a,b));

	fmt.Printf("a = %d, b = %d\n", a, b);
	swap(&a, &b);
	fmt.Printf("a = %d, b = %d\n", a, b);

	xxx := swap
	xxx(&a, &b)
	fmt.Printf("a = %d, b = %d\n", a, b);

	number := getSequence()
	fmt.Printf("number is %d\n", number())
	fmt.Printf("number is %d\n", number())
	fmt.Printf("number is %d\n", number())
	fmt.Printf("number is %d\n", number())
	
	number1 := getSequence()
	fmt.Printf("number1 is %d\n", number1())
	fmt.Printf("number1 is %d\n", number1())
	fmt.Printf("number1 is %d\n", number1())
	fmt.Printf("number1 is %d\n", number1())

	circle := Circle{x:0, y:0, radius:5}
	fmt.Printf("Circle area: %f\n", circle.area())

}
