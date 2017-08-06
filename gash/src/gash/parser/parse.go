package parser

import (
	"bufio"
	"fmt"
	"os"
	"os/exec"
)

var cmdline string

func ShellLoop() {

	for true {
		fmt.Print("[gash]$ ")
		if readCommand() == -1 {
			break
		}
		parseCommand()
		executeCommand()
	}

}

func readCommand() int {
	reader := bufio.NewReader(os.Stdin)
	line, _, err := reader.ReadLine()
	if err != nil {
		return -1
	}
	cmdline = string(line)
	return 0
}

func parseCommand() int {
	fmt.Println("parse: " + cmdline)
	return 0
}

func executeCommand() int {
	cmd := exec.Command("sh", "-c", cmdline)
	out, err := cmd.Output()
	if err != nil {
		panic("exec error")
	}
	fmt.Println(string(out))
	return 0
}
