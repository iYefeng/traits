package stores

import (
	"jvm/instructions/base"
	"jvm/rtda"
)

type ASTORE struct{ base.Index8Instruction }
type ASTORE_0 struct{ base.NoOperandInstruction }
type ASTORE_1 struct{ base.NoOperandInstruction }
type ASTORE_2 struct{ base.NoOperandInstruction }
type ASTORE_3 struct{ base.NoOperandInstruction }

func _astore(frame *rtda.Frame, index uint) {
	ref := frame.OperandStack().PopRef()
	frame.LocalVars().SetRef(index, ref)
}

func (self *ASTORE) Execute(frame *rtda.Frame) {
	_astore(frame, uint(self.Index))
}

func (self *ASTORE_0) Execute(frame *rtda.Frame) {
	_astore(frame, 0)
}

func (self *ASTORE_1) Execute(frame *rtda.Frame) {
	_astore(frame, 1)
}

func (self *ASTORE_2) Execute(frame *rtda.Frame) {
	_astore(frame, 2)
}

func (self *ASTORE_3) Execute(frame *rtda.Frame) {
	_astore(frame, 3)
}
