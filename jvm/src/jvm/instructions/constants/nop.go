package constants

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type NOP struct {
	base.NoOperandInstruction
}

func (self *NOP) Execute(frame *rtda.Frame) {
	// noting to do
}
