package main

import (
	"gash/parser"
)

func main() {
	go parser.Setup()
	parser.ShellLoop()
}
