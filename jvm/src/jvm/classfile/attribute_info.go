package classfile

import ()

type AttributeInfo interface {
	readInfo(reader *ClassReader)
}

func readAttributes(reader *ClassReader, cp ConstantPool) []AttributeInfo {

}

func readAttribute(reader *ClassReader, cp ConstantPool) AttributeInfo {

}
