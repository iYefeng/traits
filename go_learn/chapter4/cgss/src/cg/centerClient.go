package cg

import (
	"encoding/json"
	"errors"
	"ipc"
)

type CenterClient struct {
	*ipc.IpcClient
}

func (client *CenterClient) AddPlayer(player *Player) error {
	b, err := json.Marshal(*player)
	if err != nil {
		return err
	}
	resp, err := client.Call("addplayer", string(b))
	if resp.Code == "200" {
		return nil
	}
	return err
}

func (client *CenterClient) RemovePlayer(name string) error {
	ret, _ := client.Call("removeplayer", name)
	if ret.Code == "200" {
		return nil
	}
	return errors.New(ret.Code)
}

func (client *CenterClient) ListPlayer(params string) (ps []*Player, err error) {
	ret, _ := client.Call("listplayer", params)
	if ret.Code != "200" {
		err = errors.New(ret.Code)
		return
	}
	err = json.Unmarshal([]byte(ret.Body), &ps)
	return
}

func (client *CenterClient) Broadcast(message string) error {
	m := &Message{Content: message}
	b, err := json.Marshal(m)
	if err != nil {
		return err
	}

	ret, _ := client.Call("broadcast", string(b))

	if ret.Code == "200" {
		return nil
	}
	return errors.New(ret.Code)
}
