package parser

import (
	"os"
)

type SignalHandler func(s os.Signal, arg interface{})

type SignalSet struct {
	m map[os.Signal]SignalHandler
}

func NewSignalSet() *SignalSet {
	ss := new(SignalSet)
	ss.m = make(map[os.Signal]SignalHandler)
	return ss
}

func (self *SignalSet) register(s os.Signal, handler SignalHandler) {
	if _, found := self.m[s]; !found {
		self.m[s] = handler
	}
}

func (self *SignalSet) handle(sig os.Signal, arg interface{}) (err error) {
	if _, found := self.m[sig]; found {
		self.m[sig](sig, arg)
	}
	return nil
}
