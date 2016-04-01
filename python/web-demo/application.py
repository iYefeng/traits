#!/usr/bin/env python
# -*- coding: utf-8 -*-

from url import url

import tornado.web
import os

setting = dict(
    template_path = os.path.join(os.path.dirname(__file__), "template"),
    static_path = os.path.join(os.path.dirname(__file__), "static"),
    )

class Application(tornado.web.Application):

  def __init__(self):
    tornado.web.Application.__init__(
        self,
        url,
        **setting
        )

