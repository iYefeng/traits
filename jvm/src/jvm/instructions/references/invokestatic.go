package references

import (
	"jvm/instructions/base"
	"jvm/rtda"
	"jvm/rtda/heap"
)

type INVOKE_STATIC struct{ base.Index16Instruction }

func (self *INVOKE_STATIC) Execute(frame *rtda.Frame) {
	cp := frame.Method().Class().ConstantPool()
	methodRef := cp.GetConstant(self.Index).(*heap.MethodRef)
	method := methodRef.ResolvedMethod()
	if !method.IsStatic() {
		panic("java.lang.IncompabileClassChangeError")
	}
	base.InvokeMethod(frame, method)
}
