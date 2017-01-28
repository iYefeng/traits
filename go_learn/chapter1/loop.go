package main

import "fmt"

func main() {
	var i, j int

	for i = 2; i < 100; i++ {
		for j = 2; j <= (i/j); j++ {
			if (i % j == 0) {
				break; // if factor found, not prime
			}
		}
		if (j > (i/j) ) {
			fmt.Printf("%d is prime\n", i);
		}
	}

	var a int = 100;
	for a > 0 {
		a--;
		fmt.Printf("a = %d\n", a)
	}

	a = 0
	for a < 20 {
		a ++;
		if a == 10 {
			continue;
		}
		fmt.Printf("value is %d\n", a)
	}
}
