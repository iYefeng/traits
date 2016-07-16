#!/usr/bin/env python
# -*- coding: utf-8 -*-

from pykafka import KafkaClient


client = KafkaClient(hosts="127.0.0.1:9092")
print client.topics
topic = client.topics['test']
#with topic.get_sync_producer() as producer:
#  for i in range(4):
#    producer.produce('test message ' + str(i ** 2))

with topic.get_producer(delivery_reports=True) as producer:
  count = 0
  while True:
    count += 1
    producer.produce('test msg %s' % count, partition_key='{}'.format(count))
    if count % 10 ** 5 == 0:  # adjust this or bring lots of RAM ;)
      while True:
        try:
          msg, exc = producer.get_delivery_report(block=False)
          if exc is not None:
            print 'Failed to deliver msg {}: {}'.format(
                msg.partition_key, repr(exc))
          #else:
          #  print 'Successfully delivered msg {}'.format(
          #      msg.partition_key)
        except:
          break
