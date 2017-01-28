package main

import "fmt"

func main() {
	var a int = 10;

	/* if statement */
	if (a < 20) {
		fmt.Printf("a is less than 20\n");
	}
	fmt.Printf("value of a is : %d\n", a);

	/* if else statement */
	if (a > 5) {
		fmt.Printf("a is greater than 5\n");
	} else {
		fmt.Printf("a is less than 5\n");
	}

	/* if else if statement */
	a = 20
	if ( a == 10 ) {
		/* if condition is true then print the following */
		fmt.Printf("Value of a is 10\n" ) 
	} else if ( a == 20 ) {
		/* if else if condition is true */
		fmt.Printf("Value of a is 20\n" ) 
	} else if ( a == 30 ) {
		/* if else if condition is true */
		fmt.Printf("Value of a is 30\n" ) 
	} else {
		/* if none of the conditions is true */
		fmt.Printf("None of the values is matching\n" ) 
	}

	/* expression switch statement */
	var grade string = "B"
	var marks int = 90
	switch marks {
		case 90: grade = "A"
		case 80: grade = "B"
		case 50,60,70 : grade = "C"
		default: grade = "D"
	}

	switch {
	case grade == "A" :
		fmt.Printf("Excellent!\n" );
	case grade == "B", grade == "C" :
		fmt.Printf("Well done\n" )
	case grade == "D" :
		fmt.Printf("You passed\n" );
	case grade == "F":
		fmt.Printf("Better try again\n" ) 
	default:
		fmt.Printf("Invalid grade\n" );
	}
	fmt.Printf("Your grade is %s\n", grade );

	/* Type switch statement */
	var x interface{}
	x= "adda"
	switch i := x.(type) {
	case nil:
		fmt.Printf("type of x: %T\n", i)
	case int:
		fmt.Printf("x is int\n")
	case float64:
		fmt.Printf("x is float64\n")
	case func(int) float64:
		fmt.Printf("x is func(int)\n")
	case bool, string:
		fmt.Printf("x is bool or string\n") 
	default:
		fmt.Printf("don't know the type\n")
	}

	/* select statement */
	/* TODO */
}
