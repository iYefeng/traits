package math

import (
    "jvm/instructions/base"
    "jvm/rtda"
)

type DMUL struct{ base.NoOperandInstruction }
type FMUL struct{ base.NoOperandInstruction }
type IMUL struct{ base.NoOperandInstruction }
type LMUL struct{ base.NoOperandInstruction }

func (self *DMUL) Execute(frame *rtda.Frame) {
    stack := frame.OperandStack()
    v2 := stack.PopDouble()
    v1 := stack.PopDouble()
    result := v1 * v2
    stack.PushDouble(result)
}

func (self *FMUL) Execute(frame *rtda.Frame) {
    stack := frame.OperandStack()
    v2 := stack.PopFloat()
    v1 := stack.PopFloat()
    result := v1 * v2
    stack.PushFloat(result)
}

func (self *IMUL) Execute(frame *rtda.Frame) {
    stack := frame.OperandStack()
    v2 := stack.PopInt()
    v1 := stack.PopInt()
    result := v1 * v2
    stack.PushInt(result)
}

func (self *LMUL) Execute(frame *rtda.Frame) {
    stack := frame.OperandStack()
    v2 := stack.PopLong()
    v1 := stack.PopLong()
    result := v1 * v2
    stack.PushLong(result)
}
