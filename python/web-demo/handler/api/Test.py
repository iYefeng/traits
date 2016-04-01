#!/usr/bin/env python
# -*- coding: utf-8 -*-

import tornado.web
import sys
reload(sys)
sys.setdefaultencoding("utf-8")
import logging

logger = logging.getLogger("tornado.access")

class TestHandler(tornado.web.RequestHandler):
  def get(self):
    logger.info("haha it is working")
    self.write("hello world")
