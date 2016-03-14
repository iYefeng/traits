# -*- coding: utf-8 -*-
import sys
sys.path.append("../gen-py")
sys.path.append("../thrift_driver")
from dss import DsServer
from dss.ttypes import request
from stressCaseBase import StressCaseBase
import random

class StressCase(StressCaseBase):

  cls = DsServer

  def process(self):
    req = request()
    r = str(random.randint(1, 100000))
    req.key = "key2:" + r
    req.value = "value2:" + r
    #print self.client.ping()
    self.client.put(req)
    ret = self.client.get(req.key)
    print req.key + "\t" + ret
    #print self.client.getm(["dfd"])

    return 0
