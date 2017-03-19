package stores

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type ISTORE struct{ base.Index8Instruction }
type ISTORE_0 struct{ base.NoOperandInstruction }
type ISTORE_1 struct{ base.NoOperandInstruction }
type ISTORE_2 struct{ base.NoOperandInstruction }
type ISTORE_3 struct{ base.NoOperandInstruction }

func _istore(frame *rtda.Frame, index uint) {
	val := frame.OperandStack().PopInt()
	frame.LocalVars().SetInt(index, val)
}

func (self *ISTORE) Execute(frame *rtda.Frame) {
	_istore(frame, uint(self.Index))
}

func (self *ISTORE_0) Execute(frame *rtda.Frame) {
	_istore(frame, 0)
}

func (self *ISTORE_1) Execute(frame *rtda.Frame) {
	_istore(frame, 1)
}

func (self *ISTORE_2) Execute(frame *rtda.Frame) {
	_istore(frame, 2)
}

func (self *ISTORE_3) Execute(frame *rtda.Frame) {
	_istore(frame, 3)
}
