package mlib

import (
	"errors"
)

type SMPManager struct {
	musics []SMPEntry
}

func NewSMPManager() *SMPManager {
	return &SMPManager{make([]SMPEntry, 0)}
}

func (m *SMPManager) Len() int {
	return len(m.musics)
}

func (m *SMPManager) Get(i int) (music *SMPEntry, err error) {
	if i < 0 || i >= len(m.musics) {
		return nil, errors.New("index out of range")
	}
	return &m.musics[i], nil
}

func (m *SMPManager) Add(music *SMPEntry) {
	m.musics = append(m.musics, *music)
}

func (m *SMPManager) Remove(index int) *SMPEntry {
	if index < 0 || index >= len(m.musics) {
		return nil
	}

	removedMusic := &m.musics[index]
	m.musics = append(m.musics[:index], m.musics[index+1:]...)
	return removedMusic
}

func (m *SMPManager) Find(name string) *SMPEntry {
	if len(m.musics) == 0 {
		return nil
	}
	for _, it := range m.musics {
		if it.Name == name {
			return &it
		}
	}
	return nil
}

func (m *SMPManager) RemoveByName(name string) *SMPEntry {
	if len(m.musics) == 0 {
		return nil
	}

	for i, it := range m.musics {
		if it.Name == name {
			return m.Remove(i)
		}
	}
	return nil
}
