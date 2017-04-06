package heap

import ()

type SymRef struct {
	cp        *ConstantPool
	className string
	class     *Class
}
