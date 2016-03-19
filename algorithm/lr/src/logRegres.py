# -*- coding: utf-8 -*-

import math
import numpy as np
import random

class LogRegres:
  def __init__(self):
    pass

  def sigmoid(self, x):
    #return 1.0 / (1.0 + np.exp(-x))
    return .5 * (1 + np.tanh(.5 * x))

  def loadDataSet(self, filename):
    with open(filename, "r") as f:
      labelMat = []
      dataMat = []
      for line in f:
        tmp = line.strip("\n").split("\t")
        labelMat.append(float(tmp[-1]))
        dataMat.append( [1.0] + [float(k) for k in tmp[:-1]] )
      return dataMat, labelMat

  def train(self, filename, numIter = 250):
    dataAttr, labels = self.loadDataSet(filename)
    dataAttr = np.mat(dataAttr)
    m,n =  np.shape(dataAttr)
    if not hasattr(self, "weights"):
      self.weights = np.ones((n,1))
    for j in range(numIter):
      dataIndex = range(m)
      for i in range(m):
        alpha = 4.0 / (1.0+j+i) + 0.01
        randIndex = int(random.uniform(0, len(dataIndex)))
        x = dataAttr[dataIndex[randIndex]]
        ret = self.sigmoid(np.dot(x, self.weights))
        error = labels[dataIndex[randIndex]] - ret
        self.weights = self.weights + alpha * x.transpose() * error
        del dataIndex[randIndex]
    return self.weights

  def forecast(self, data):
    x = np.mat(data)
    ret = np.dot(x, self.weights)
    if ret > 0:
      return 1
    else:
      return 0


if __name__ == "__main__":
  lr = LogRegres()
  lr.train("../data/horseColicTraining.txt", 500)
  error = 0
  right = 0
  total = 0
  with open("../data/horseColicTest.txt") as f:
    for line in f:
      total += 1
      tmp = line.strip("\n").split("\t")
      label = float(tmp[-1])
      data = [1] + [float(k) for k in tmp[:-1]]
      ret = lr.forecast(data)
      if label == ret:
        right += 1
      else:
        error += 1

  print "%s\t%s%%\t%s\t%s" % (total, float(right)/total*100, right, error)
  


