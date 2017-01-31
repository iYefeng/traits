package main

import (
	"fmt"
)

type Stack struct {
	data []interface{}
	size int
}

func(s *Stack) Push(x interface{}) {
	s.data = append(s.data, x)
	s.size++
	fmt.Println("pushed data", s.size-1, s.data[s.size-1], s.data)
}

func(s *Stack) Pop() interface{} {
	if s.size-1 < 0 {
		return nil
	}
	fmt.Println(s.data, s.size)
	res := s.data[s.size-1]
	s.data[s.size-1] = nil
	s.size--
	s.data = s.data[:s.size]
	fmt.Println("pop data", s.size-1, res, s.data)
	return res
}

func(s *Stack) Size() int {
	return s.size
}

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

	var s *Stack = new(Stack)
	s.size = 1
	s.data = append(s.data, 10)
	s.Push(1)
	s.Push(3)
	fmt.Println(s.Size())
	s.Push(6)
	s.Push("jee")
	fmt.Println(s.Pop())
	fmt.Println(s.Pop())
	fmt.Println(s.Pop())
	fmt.Println(s.Size())
}
