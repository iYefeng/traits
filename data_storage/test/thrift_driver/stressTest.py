#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
from taskQueue import TaskQueue
import time

class StressTest:
  def __init__(self, cls, host, port, num_workers = 5):
    self.cls = cls
    self.host = host
    self.port = port
    self.num_workers = num_workers
    self.cnt = 0
    self.ok = 0
    self.err = 0

  def start(self):
    self.stime = time.time()
    q = TaskQueue(self.num_workers)
    
    def task(*args, **kwargs):
      test = self.cls(self.host, self.port)
      while(1):
        self.cnt += 1
        if q.is_exit == True:
          break
        ret = test.process()
        if ret == 0:
          self.ok += 1
        else:
          self.err += 1

    for item in range(self.num_workers):
      q.add_task(task)
    q.join()
    self.etime = time.time()
  
  def status(self):
    print "-"*20
    print "All done"
    print "time eclipse:% s" % (self.etime - self.stime)
    print "count: %s" % self.cnt
    print "ok: %s" % self.ok
    print "err: %s" % self.err
    print "QPS: %s" % (float(self.cnt) / (self.etime - self.stime))


def main():
  clazz = sys.argv[1]
  host = sys.argv[2]
  port = int(sys.argv[3])
  num_thread = int(sys.argv[4])
  case = __import__(clazz.split(".")[0])
  test = StressTest(case.StressCase, host, port, num_thread)
  test.start()
  test.status()


if __name__ == "__main__":
  main()
