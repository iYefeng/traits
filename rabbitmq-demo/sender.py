#!/usr/bin/env python
import pika

creds_broker = pika.PlainCredentials("yefeng", "123456")
connection = pika.BlockingConnection(
        pika.ConnectionParameters(host='192.168.0.107', credentials=creds_broker))
channel = connection.channel()

channel.queue_declare(queue='hello')

channel.basic_publish(exchange='',
                      routing_key='hello',
                      body='Hello World!')
print(" [x] Sent 'Hello World!'")
connection.close()
