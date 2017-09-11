package base

import (
	"jvm/rtda"
	"jvm/rtda/heap"
)

func InvokeMethod(invokeFrame *rtda.Frame, method *heap.Method) {
	thread := invokeFrame.Thread()
	newFrame := thread.NewFrame(method)
	thread.PushFrame(newFrame)

	//fmt.Printf("%s is native = %t\n", method.Name(), method.IsNative())

	argSlotSlot := int(method.ArgSlotCount())
	if argSlotSlot > 0 {
		for i := argSlotSlot - 1; i >= 0; i-- {
			slot := invokeFrame.OperandStack().PopSlot()
			newFrame.LocalVars().SetSlot(uint(i), slot)
		}
	}
	//	// hack!
	//	if method.IsNative() {
	//		if method.Name() == "registerNatives" {
	//			thread.PopFrame()
	//		} else {
	//			panic(fmt.Sprintf("native method: %v.%v%v\n",
	//				method.Class().Name(), method.Name(), method.Descriptor()))
	//		}
	//	}
}
