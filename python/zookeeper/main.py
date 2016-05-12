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
  TIMEOUT = 1000

  def __init__(self, verbose = True, database='/test', table='targets'):
    self.VERBOSE = verbose
    self.path = None
    self.database = database
    self.table = os.path.join(self.database, table)

    self.zk = ZKClient(self.ZK_HOST, timeout = self.TIMEOUT)
    self.say("login ok")
    self._init_zk()

  def __del__(self):
    self.zk.close()

  def _init_zk(self):
    nodes = (self.database, self.table)
    for node in nodes:
      if not self.zk.exists(node):
        try:
          self.zk.create(node, "")
        except:
          pass

  def create(self, path, msg, flags=0):
    msg = self.encode(msg)
    self.path = os.path.join(self.table, path)
    nodes = self.path.split("/")
    # FIXME
    for i in range(4,len(nodes)):
      node = "/".join(nodes[:i])
      if not self.zk.exists(node):
        try:
          self.zk.create(node, "")
        except:
          pass
    if not self.zk.exists(self.path):
      self.path = self.zk.create(self.path, msg, flags=flags)
    else:
      self.zk.set(self.path, msg)
    #self.path = os.path.basename(self.path)
    self.say("register ok! I'm %s" % self.path)
  
  def get_children(self, path=""):
    if not path:
      path = self.table
    children = self.zk.get_children(path)
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
    if msg:
      return json.loads(msg)
    else:
      return {}
  
  def encode(self, obj):
    if obj:
      return json.dumps(obj)
    else:
      return ""

if __name__ == "__main__":
  zks = JsonZookeeperStore(database="/webSpider", table="targets")
  zks.create("127.0.0.1:9000", {"url":"127.0.0.1:9000"}, 1)
  zks.create("127.0.0.1:9001", {"url":"127.0.0.1:9001"}, 1)
  children = zks.get_children()
  for child in children:
    print zks.decode(zks.get(zks.table + "/" + child)[0])["url"]

  zkeggs = JsonZookeeperStore(database="/webSpider", table="eggs")
  zkeggs.create("project1/v1.0", {"data":"hello1"}, 0)
  zkeggs.create("project1/v2.0", {"data":"hello2"}, 0)
  zkeggs.create("project2/v2.0", {"data":"hello2"}, 0)
  children = zkeggs.get_children()
  for child in children:
    data = zkeggs.get_children(zkeggs.table + "/" + child)
    print data
