# -*- coding: utf-8 -*-

import math
import numpy as np
import random

class Svm:
  
  def loadDataSet(self, filename):
    self.dataMat_ = []
    self.labelMat_ = []
    with open(filename, "r") as f:
      for line in f:
        tmp = line.strip("\n").split("\t")
        self.dataMat_.append([float(k) for k in tmp[:-1]])
        self.labelMat_.append(float(tmp[-1]))

  def selectRand(self, i, m):
    j = i
    while j == i:
      j = int(random.uniform(0, m))
    return j

  def clipAlpha(self, aj, H, L):
    if aj > H:
      aj = H
    if L > aj:
      aj = L
    return aj

  def smo(self)
