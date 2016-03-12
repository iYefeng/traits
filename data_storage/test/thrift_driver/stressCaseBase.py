# -*- coding: utf-8 -*-

from thriftClient import ThriftClient

class StressCaseBase:

  cls = None

  def __init__(self, host, port):
    self.conn = ThriftClient(self.cls, host, port)
    self.client = self.conn.open()

  def __del__(self):
    self.conn.close()

  def process(self):
    print self.client.ping()
    #print self.client.get("haha")
    #print self.client.getm(["dfd"])
