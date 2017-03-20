package math

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type DNEG struct{ base.NoOperandInstruction }
type FNEG struct{ base.NoOperandInstruction }
type INEG struct{ base.NoOperandInstruction }
type LNEG struct{ base.NoOperandInstruction }

func (self *DNEG) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	val := stack.PopDouble()
	stack.PushDouble(-val)
}

func (self *FNEG) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	val := stack.PopFloat()
	stack.PushFloat(-val)
}

func (self *INEG) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	val := stack.PopInt()
	stack.PushInt(-val)
}

func (self *LNEG) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	val := stack.PopLong()
	stack.PushLong(-val)
}
