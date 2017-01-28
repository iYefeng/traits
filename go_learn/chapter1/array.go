package main

import (
	"fmt"
)

func average(arr []int, size int) float32 {
	var avg, sum float32;

	for i:= 0; i < size; i++ {
		sum += float32(arr[i])
	}

	avg = sum / float32(size)
	return avg
}

func main() {
	var balance = []int {1000, 2, 3, 17, 50} 
	var avg float32
	avg = average(balance, len(balance))
	fmt.Printf("avg: %f\n", avg)
}
