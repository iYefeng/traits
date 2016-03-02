#!/usr/bin/env python
# -*- coding: utf-8 -*-

from threading import Thread
import Queue
import time
import sys
import signal

class TaskQueue():
  
  def __init__(self, num_workers = 1):
    self.is_exit = False
    self.queue = Queue.Queue(0)
    self.num_workers = num_workers
    self.workers = []
    self.start_workers()
  
  def add_task(self, task, *args, **kwargs):
    args = args or ()
    kwargs = kwargs or {}
    self.queue.put((task, args, kwargs))

  def start_workers(self):
    for i in range(self.num_workers):
      t = Thread(target = self.worker)
      t.daemon = True
      self.workers.append(t)
      t.start()
  
  def worker(self):
    while not self.is_exit:
      item, args, kwargs = self.queue.get()
      try:
        item(*args, **kwargs)
      except Exception, e:
        print e

  def handler(self, signum, frame):
    self.int_cnt += 1
    if self.int_cnt == 1:
      print "reveive Ctrl-C, soft exit (if you want to exit immediately, press Ctrl-C again.)"  
      self.is_exit = True
    elif self.int_cnt > 1:
      print "system exit now"
      sys.exit(0)
  
  def join(self):
    self.int_cnt = 0
    signal.signal(signal.SIGINT, self.handler)  
    signal.signal(signal.SIGTERM, self.handler)
    while True:
      alive = False
      for i in self.workers:
        alive = alive or i.isAlive()
      if not alive:
        break
      time.sleep(0.1)

def test():
  def blocktest(*args, **kwargs):
    time.sleep(8)
    print "block test %s" % args[0]

  q = TaskQueue(num_workers = 5)

  for item in range(100):
    q.add_task(blocktest, *[item])
  
  q.join()

  print "All done"

if __name__ == "__main__":
  test()


