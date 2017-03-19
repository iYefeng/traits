package stack

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type SWAP struct{ base.NoOperandInstruction }

func (self *SWAP) Execute(frame *rtda.Frame) {
	stack := frame.OperandStack()
	slot1 := stack.PopSlot()
	slot2 := stack.PopSlot()
	stack.PushSlot(slot1)
	stack.PushSlot(slot2)
}
