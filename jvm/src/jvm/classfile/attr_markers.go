package classfile

import ()

type MarkerAttribute struct{}

func (self *MarkerAttribute) readInfo(reader *MarkerAttribute) {
	// read nothing
}

type DeprecatedAttribute struct {
	MarkerAttribute
}

type SyntheticAttribute struct {
	MarkerAttribute
}
