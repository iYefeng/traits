package stack

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type POP struct{ base.NoOperandInstruction }
type POP2 struct{ base.NoOperandInstruction }

func (self *POP) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	stack.PopSlot()
}

func (self *POP2) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	stack.PopSlot()
	stack.PopSlot()
}
