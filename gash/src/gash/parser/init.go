package parser

import (
	"fmt"
	"os"
	"os/signal"
	"syscall"
)

func Setup() {
	ss := NewSignalSet()
	sigint_handler := func(s os.Signal, arg interface{}) {
		fmt.Printf("\n[gash]$ ")
	}

	sigquit_handler := func(s os.Signal, arg interface{}) {
	}

	ss.register(syscall.SIGINT, sigint_handler)
	ss.register(syscall.SIGQUIT, sigquit_handler)
	//ss.register(syscall.SIGUSR1, handler)
	//ss.register(syscall.SIGUSR2, handler)

	var sigs []os.Signal
	for sig := range ss.m {
		sigs = append(sigs, sig)
	}

	for {
		c := make(chan os.Signal)
		signal.Notify(c, sigs...)
		sig := <-c
		err := ss.handle(sig, nil)
		if err != nil {
			panic(fmt.Errorf("unknown signal received: %v\n", sig))
			os.Exit(1)
		}
	}

}
