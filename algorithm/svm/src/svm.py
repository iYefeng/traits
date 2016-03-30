# -*- coding: utf-8 -*-

import math
from numpy import *
import random

class Svm:
  
  def loadDataSet(self, filename):
    self.data_ = []
    self.label_ = []
    with open(filename, "r") as f:
      for line in f:
        tmp = line.strip("\n").split("\t")
        self.data_.append([float(k) for k in tmp[:-1]])
        self.label_.append(float(tmp[-1]))

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

  def smo(self, C, toler, maxIter):
    dataMat = mat(self.data_)
    labelMat = mat(self.label_).transpose()
    self.b_ = 0
    m,n = shape(dataMat)
    self.alphas_ = mat(zeros((m, 1)))
    cnt = 0
    while (cnt < maxIter):
      alphaPairsChanged = 0
      for i in range(m):
        fXi = float(multiply(self.alphas_, labelMat).T * (dataMat * dataMat[i,:].T)) + self.b_
        Ei = fXi - float(labelMat[i])
        if ((labelMat[i]*Ei) < -toler and (self.alphas_[i] < C)) or ((labelMat[i] * Ei > toler) and (self.alphas_[i] > 0)):
          j = self.selectRand(i, m)
          fXj = float(multiply(self.alphas_, labelMat).T * (dataMat*dataMat[j, :].T)) + self.b_
          Ej = fXj - float(labelMat[j])
          alphaIold = self.alphas_[i].copy()
          alphaJold = self.alphas_[j].copy()
          if (labelMat[i] != labelMat[j]):
            L = max(0, self.alphas_[j] - self.alphas_[i])
            H = min(C, C + self.alphas_[j] - self.alphas_[i])
          else:
            L = max(0, self.alphas_[j] + self.alphas_[i] - C)
            H = min(C, self.alphas_[j] + self.alphas_[i])
          if L == H: print "L==H"; continue
          eta = 2.0 * dataMat[i,:]*dataMat[j,:].T - dataMat[i,:]*dataMat[i,:].T - dataMat[j,:]*dataMat[j,:].T
          if eta >= 0: print "eta>=0"; continue
          self.alphas_[j] -= labelMat[j]*(Ei-Ej)/eta
          self.alphas_[j] = self.clipAlpha(self.alphas_[j], H, L)
          if (abs(self.alphas_[j] - alphaJold) < 0.00001): print "j not moving enough"; continue
          self.alphas_[i] += labelMat[j]*labelMat[i]*(alphaJold - self.alphas_[j])
          b1 = self.b_ - Ei- labelMat[i]*(self.alphas_[i]-alphaIold)*dataMat[i,:]*dataMat[i,:].T - labelMat[j]*(self.alphas_[j]-alphaJold)*dataMat[i,:]*dataMat[j,:].T
          b2 = self.b_ - Ej- labelMat[i]*(self.alphas_[i]-alphaIold)*dataMat[i,:]*dataMat[j,:].T - labelMat[j]*(self.alphas_[j]-alphaJold)*dataMat[j,:]*dataMat[j,:].T
          if (0 < self.alphas_[i]) and (C > self.alphas_[i]): self.b_ = b1
          elif (0 < self.alphas_[j]) and (C > self.alphas_[j]): self.b_ = b2
          else: self.b_ = (b1 + b2)/2.0
          alphaPairsChanged += 1
          print "iter: %d i:%d, pairs changed %d" % (cnt,i,alphaPairsChanged)
      if (alphaPairsChanged == 0): cnt += 1
      else: cnt = 0
      print "iteration number: %d" % cnt
    return self.b_, self.alphas_

if __name__ == "__main__":
  svm = Svm()
  svm.loadDataSet("../data/testSet.txt")
  b, alpha = svm.smo(0.6, 0.001, 40)
  print alpha[alpha>0]

