# -*- coding: utf-8 -*-
import sys

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

class ThriftClient:
  def __init__(self, cls, host="localhost", port=9090):
    transport_ = TSocket.TSocket(host, port)
    self.transport = TTransport.TFramedTransport(transport_)
    self.protocol = TBinaryProtocol.TBinaryProtocol(self.transport)
    self.cls = cls

  def __del__(self):
    self.transport.close()

  def open(self):
    self.client = self.cls.Client(self.protocol)
    self.transport.open()
    return self.client

  def close(self):
    self.transport.close()
