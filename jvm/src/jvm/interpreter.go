package main

import (
	"fmt"
	"jvm/instructions"
	"jvm/instructions/base"
	"jvm/rtda"
	"jvm/rtda/heap"
)

func interpret(method *heap.Method, logInst bool) {
	thread := rtda.NewThread()
	frame := thread.NewFrame(method)
	thread.PushFrame(frame)

	defer catchErr(thread)
	loop(thread, logInst)
}

func catchErr(thread *rtda.Thread) {
	if r := recover(); r != nil {
		logFrames(thread)
		panic(r)
	}
}

func logFrames(thread *rtda.Thread) {
	for !thread.IsStackEmpty() {
		frame := thread.PopFrame()
		method := frame.Method()
		className := method.Class().Name()
		fmt.Println(">> pc:%4d %v.%v%v \n",
			frame.NextPC(), className, method.Name(), method.Descriptor())
	}
}

func logInstruction(frame *rtda.Frame, inst base.Instruction) {
	method := frame.Method()
	className := method.Class().Name()
	methodName := method.Name()
	pc := frame.Thread().PC()
	fmt.Println("%v.%v() #%2d %T %v\n", className, methodName, pc, inst, inst)
}

func loop(thread *rtda.Thread, logInst bool) {
	reader := &base.BytecodeReader{}

	for {
		frame := thread.CurrentFrame()
		pc := frame.NextPC()
		thread.SetPC(pc)

		// decode
		reader.Reset(frame.Method().Code(), pc)
		opcode := reader.ReadUint8()
		inst := instructions.NewInstruction(opcode)
		inst.FetchOperands(reader)
		frame.SetNextPC(reader.PC())

		if logInst {
			logInstruction(frame, inst)
		}

		// execute
		fmt.Printf("pc:%2d inst:%T %v\n", pc, inst, inst)
		inst.Execute(frame)
		if thread.IsStackEmpty() {
			break
		}
	}
}
