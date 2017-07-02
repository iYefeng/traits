package main

import (
	"flag"
	"fmt"
	"os"
)

type Cmd struct {
	helpFlag         bool
	versionFlag      bool
	verboseClassFlag bool
	verboseInstFlag  bool
	cpOption         string
	XjreOption       string
	class            string
	args             []string
}

func printUsage() {
	fmt.Printf("Usage: %s [-option] class [args...]\n", os.Args[0])
}

func parseCmd() *Cmd {
	cmd := Cmd{}

	flag.Usage = printUsage
	flag.BoolVar(&cmd.helpFlag, "help", false, "print help message")
	flag.BoolVar(&cmd.helpFlag, "?", false, "print help message")
	flag.BoolVar(&cmd.versionFlag, "version", false, "show version")
	flag.BoolVar(&cmd.verboseClassFlag, "verbose:class", false, "enable verbose output")
	flag.BoolVar(&cmd.verboseInstFlag, "verbose:inst", false, "enable verbose output")
	flag.StringVar(&cmd.XjreOption, "Xjre", "", "jre path")
	flag.StringVar(&cmd.cpOption, "classpath", "", "classpath")
	flag.StringVar(&cmd.cpOption, "cp", "", "classpath")
	flag.Parse()

	args := flag.Args()
	if len(args) > 0 {
		cmd.class = args[0]
		cmd.args = args[1:]
	}

	return &cmd
}
