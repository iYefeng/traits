#!/usr/bin/env python
# -*- coding: utf-8 -*-

import data_trans_pb2
query = data_trans_pb2.Query()
query.num = 1
query.data = "hello_world"
query_str = query.SerializeToString()

print query_str
query2 = data_trans_pb2.Query()
query2.ParseFromString(query_str)
print query2.num
print query2.data
