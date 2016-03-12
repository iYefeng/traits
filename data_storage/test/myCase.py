# -*- coding: utf-8 -*-
import sys
sys.path.append("../gen-py")
sys.path.append("../thrift_driver")
from dss import DsServer
from stressCaseBase import StressCaseBase

class StressCase(StressCaseBase):

  cls = DsServer

  def process(self):
    print self.client.ping()
    #print self.client.get("haha")
    #print self.client.getm(["dfd"])

    return 0
