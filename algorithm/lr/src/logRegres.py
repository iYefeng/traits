# -*- coding: utf-8 -*-

import math

class LogRegres:
  def __init__(self, dataMatLength):
    pass

  def sigmoid(self, x):
    return 1.0 / (1.0 + math.exp(-x))

  def loadDataSet(self, filename):

