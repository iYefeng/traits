#!/usr/bin/env python
# -*- coding: utf-8 -*-
import logging
import os
import simplejson as json

from zkclient import ZKClient, zookeeper, watchmethod

logging.basicConfig(
    level = logging.DEBUG,
    format = "[%(asctime)s] %(levelname)-8s %(message)s"
)

log = logging

class ZookeeperStore(object):
  
  ZK_HOST = "127.0.0.1:32181"
  ROOT = "/webSpider"
  WORKERS_PATH = os.path.join(ROOT, "workers")
  TIMEOUT = 1000

  def __init__(self, verbose = True):
    self.VERBOSE = verbose
    self.path = None

    self.zk = ZKClient(self.ZK_HOST, timeout = self.TIMEOUT)
    self.say("login ok")
    self._init_zk()

  def __del__(self):
    self.zk.close()

  def _init_zk(self):
    nodes = (self.ROOT, self.WORKERS_PATH)
    for node in nodes:
      if not self.zk.exists(node):
        try:
          self.zk.create(node, "")
        except:
          pass

  def create(self, msg):
    msg = self.encode(msg)
    self.path = self.zk.create(self.WORKERS_PATH + "/worker", msg, flags=zookeeper.EPHEMERAL | zookeeper.SEQUENCE)
    self.path = os.path.basename(self.path)
    self.say("register ok! I'm %s" % self.path)
  
  def get_children(self):
    children = self.zk.get_children(self.WORKERS_PATH)
    return children

  def get(self, node):
    msg = self.zk.get(node)
    return msg

  def decode(self, obj):
    return obj

  def encode(self, msg):
    return msg

  def say(self, msg):
    if self.VERBOSE:
      if self.path:
        log.info(msg)
      else:
        log.info(msg)

class JsonZookeeperStore(ZookeeperStore):

  def decode(self, msg):
    return json.loads(msg)
  
  def encode(self, obj):
    return json.dumps(obj)

if __name__ == "__main__":
  zks = JsonZookeeperStore()
  zks.create({"url":"127.0.0.1:9000"})
  zks.create({"url":"127.0.0.1:9001"})
  children = zks.get_children()
  for child in children:
    print zks.decode(zks.get(zks.WORKERS_PATH + "/" + child)[0])["url"]
