#!/usr/bin/env python
import sys
sys.path.append("../gen-py")

from dss import DsServer
from thrift_driver import thriftClient

def main():
  conn = thriftClient.ThriftClient(DsServer, "127.0.0.1", 9090)
  DsClient = conn.open()
  DsClient.ping()
  print DsClient.get("test")
  print DsClient.getm(["test"])
  print DsClient.status()

  conn.close()

if __name__ == "__main__":
  main()
