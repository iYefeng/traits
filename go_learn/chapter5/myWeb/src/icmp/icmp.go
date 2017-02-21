package main

import (
	"bytes"
	"errors"
	"fmt"
	"net"
	"os"
	"time"
)

const (
	icmpv4EchoReply   = 0
	icmpv4EchoRequest = 8
	icmpv6EchoRequest = 128
	icmpv6EchoReply   = 129
)

type icmpMessage struct {
	Type     int             // type
	Code     int             // code
	Checksum int             // checksum
	Body     icmpMessageBody // body
}

type icmpMessageBody interface {
	Len() int
	Marshal() ([]byte, error)
}

func (m *icmpMessage) Len() int {
	return 4 + m.Body.Len()
}

// Marshal returns the binary enconding of the ICMP echo request or
// reply message m.
func (m *icmpMessage) Marshal() ([]byte, error) {
	b := []byte{byte(m.Type), byte(m.Code), 0, 0}
	if m.Body != nil && m.Body.Len() != 0 {
		mb, err := m.Body.Marshal()
		if err != nil {
			return nil, err
		}
		b = append(b, mb...)
	}
	switch m.Type {
	case icmpv6EchoRequest, icmpv6EchoReply:
		return b, nil
	}

	s := CheckSum(b)

	// Place checksum back in header; using ^= avoids the
	// assumption the checksum bytes are zero.
	b[2] ^= byte(s & 0xff)
	b[3] ^= byte(s >> 8)
	return b, nil
}

func CheckSum(data []byte) uint16 {
	csumcv := len(data) - 1 // checksum coverage
	s := uint32(0)
	for i := 0; i < csumcv; i += 2 {
		s += uint32(data[i+1])<<8 | uint32(data[i])
	}
	if csumcv&1 == 0 {
		s += uint32(data[csumcv])
	}
	s = s>>16 + s&0xffff
	s = s + s>>16
	return uint16(^s)
}

// parseICMPMessage parses b as an ICMP message.
func parseICMPMessage(b []byte) (*icmpMessage, error) {
	msglen := len(b)
	if msglen < 4 {
		return nil, errors.New("message too short")
	}
	m := &icmpMessage{Type: int(b[0]), Code: int(b[1]), Checksum: int(b[2])<<8 | int(b[3])}
	if msglen > 4 {
		var err error
		switch m.Type {
		case icmpv4EchoRequest, icmpv4EchoReply, icmpv6EchoRequest, icmpv6EchoReply:
			m.Body, err = parseICMPEcho(b[4:])
			if err != nil {
				return nil, err
			}
		}
	}
	return m, nil
}

// imcpEcho represenets an ICMP echo request or reply message body.
type icmpEcho struct {
	icmpMessageBody
	ID   int    // identifier
	Seq  int    // sequence number
	Data []byte // data
}

func (p *icmpEcho) Len() int {
	if p == nil {
		return 0
	}
	return 4 + len(p.Data)
}

// Marshal returns the binary enconding of the ICMP echo request or
// reply message body p.
func (p *icmpEcho) Marshal() ([]byte, error) {
	b := make([]byte, 4+len(p.Data))
	b[0], b[1] = byte(p.ID>>8), byte(p.ID&0xff)
	b[2], b[3] = byte(p.Seq>>8), byte(p.Seq&0xff)
	copy(b[4:], p.Data)
	return b, nil
}

// parseICMPEcho parses b as an ICMP echo request or reply message body.
func parseICMPEcho(b []byte) (*icmpEcho, error) {
	bodylen := len(b)
	p := &icmpEcho{ID: int(b[0])<<8 | int(b[1]), Seq: int(b[2])<<8 | int(b[3])}
	if bodylen > 4 {
		p.Data = make([]byte, bodylen-4)
		copy(p.Data, b[4:])
	}
	return p, nil
}

func ipv4Payload(b []byte) []byte {
	if len(b) < 20 {
		return b
	}
	hdrlen := int(b[0]&0x0f) << 2
	return b[hdrlen:]
}

func main() {
	arg_num := len(os.Args)

	if arg_num < 2 {
		fmt.Print(
			"Please runAs [super user] in [terminal].\n",
			"Usage:\n",
			"\tgoping url\n",
			"\texample: goping www.baidu.com",
		)
		time.Sleep(5e9)
		return
	}

	service := os.Args[1]
	conn, err := net.Dial("ip4:icmp", service)
	if err != nil {
		fmt.Println(err.Error())
		return
	}
	defer conn.Close()

	typ := icmpv4EchoRequest
	xid := os.Getpid() & 0xffff
	xseq := 0

	for {
		xseq += 1
		wb, err := (&icmpMessage{
			Type: typ, Code: 0,
			Body: &icmpEcho{
				ID: xid, Seq: xseq,
				Data: bytes.Repeat([]byte("hello baidu"), 3),
			},
		}).Marshal()

		conn.SetReadDeadline((time.Now().Add(time.Second * 5)))

		if _, err := conn.Write(wb); err != nil {
			fmt.Println(err.Error())
			return
		}
		t_start := time.Now()

		rb := make([]byte, 20+len(wb))
		_, err = conn.Read(rb)
		if err != nil {
			fmt.Println(err.Error())
		}

		rb = ipv4Payload(rb)
		m, err := parseICMPMessage(rb)

		if err != nil {
			fmt.Println("请求超时")
			continue
		}

		t_end := time.Now()

		dur := t_end.Sub(t_start).Nanoseconds() / 1e6

		switch m.Body.(type) {
		case *icmpEcho:
			echo := m.Body.(*icmpEcho)
			fmt.Printf("来自 %s 的回复: 时间 = %dms, type = %d, seq = %d, data = %s\n", service, dur, m.Type, echo.Seq, echo.Data)
		default:
			fmt.Printf("来自 %s 的回复: 时间 = %dms, type = %d\n", service, dur, m.Type)
		}

		time.Sleep(1 * time.Second)

	}

}
