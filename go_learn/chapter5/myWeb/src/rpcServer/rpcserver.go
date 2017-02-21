package main

import (
	"bufio"
	"log"
	"net"
	"net/http"
	"net/rpc"
	"os"
	"rpcServerHandler"
)

func main() {
	arith := new(rpcServerHandler.Arith)
	rpc.Register(arith)
	rpc.HandleHTTP()
	l, e := net.Listen("tcp", ":1234")
	if e != nil {
		log.Fatal("Listen error:", e)
	}
	go http.Serve(l, nil)
	r := bufio.NewReader(os.Stdin)

	for {
		b, _, _ := r.ReadLine()
		line := string(b)
		if line == "q" {
			break
		}
	}
}
