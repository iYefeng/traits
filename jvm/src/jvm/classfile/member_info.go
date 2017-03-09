package classfile

import ()

type MemberInfo struct {
	cp              ConstantPool
	accessFlags     uint16
	nameIndex       uint16
	descriptorIndex uint16
	attribute       []AttributeInfo
}

func readMembers(reader *ClassReader, cp ConstantPool) []*MemberInfo {

}
