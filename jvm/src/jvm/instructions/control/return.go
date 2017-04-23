package control

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type RETURN struct{ base.NoOperandInstruction }
type ARETURN struct{ base.NoOperandInstruction }
type IRETURN struct{ base.NoOperandInstruction }
type LRETURN struct{ base.NoOperandInstruction }
type FRETURN struct{ base.NoOperandInstruction }
type DRETURN struct{ base.NoOperandInstruction }

func (self *RETURN) Execute(frame *rtda.Frame) {
	frame.Thread().PopFrame()
}

func (self *IRETURN) Execute(frame *rtda.Frame) {
	thread := frame.Thread()
	currentFrame := thread.PopFrame()
	invokeFrame := thread.TopFrame()

	retVal := currentFrame.OperandStack().PopInt()
	invokeFrame.OperandStack().PushInt(retVal)
}

func (self *LRETURN) Execute(frame *rtda.Frame) {
	thread := frame.Thread()
	currentFrame := thread.PopFrame()
	invokeFrame := thread.TopFrame()

	retVal := currentFrame.OperandStack().PopLong()
	invokeFrame.OperandStack().PushLong(retVal)
}

func (self *FRETURN) Execute(frame *rtda.Frame) {
	thread := frame.Thread()
	currentFrame := thread.PopFrame()
	invokeFrame := thread.TopFrame()

	retVal := currentFrame.OperandStack().PopFloat()
	invokeFrame.OperandStack().PushFloat(retVal)
}

func (self *DRETURN) Execute(frame *rtda.Frame) {
	thread := frame.Thread()
	currentFrame := thread.PopFrame()
	invokeFrame := thread.TopFrame()

	retVal := currentFrame.OperandStack().PopDouble()
	invokeFrame.OperandStack().PushDouble(retVal)
}
