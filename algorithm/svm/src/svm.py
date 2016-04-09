# -*- coding: utf-8 -*-

import math
from numpy import *
import random

class Kernel:
  def kernelTrans(self, X, A, kTup):
    m,n = shape(X)
    K = mat(zeros((m,1)))
    if kTup[0] == 'lin': K = X * A.T
    elif kTup[0] == 'rbf':
      for j in range(m):
        deltaRow = X[j,:] - A
        K[j] = deltaRow*deltaRow.T
      K = exp(K/(-1*kTup[1]**2))
    else: raise NameError('%s is not reconized' & kTup)
    return K

class Svm:

  kernel_ = Kernel()

  def setKernel(self, cls):
    self.kernel_ = cls
  
  def loadDataSet(self, filename):
    _data = []
    _label = []
    with open(filename, "r") as f:
      for line in f:
        tmp = line.strip("\n").split("\t")
        _data.append([float(k) for k in tmp[:-1]])
        _label.append(float(tmp[-1]))
    self.dataMat_ = mat(_data)
    self.labelMat_ = mat(_label).transpose()
    self.m_ = shape(self.dataMat_)[0]
    self.alphas_ = mat(zeros((self.m_, 1)))
    self.b_ = 0
    self.eCache_ = mat(zeros((self.m_, 2)))

  def clipAlpha(self, aj, H, L):
    if aj > H:
      aj = H
    if L > aj:
      aj = L
    return aj
  
  def calcEk(self, k):
    fXk = float(multiply(self.alphas_, self.labelMat_).T * \
        (self.dataMat_*self.dataMat_[k,:].T) + self.b_)
    Ek = fXk - float(self.labelMat_[k])
    return Ek
  
  def selectJRand(self, i, m):
    j = i
    while j == i:
      j = int(random.uniform(0, m))
    return j
  
  def selectJ(self, i, Ei):
    maxK = -1; maxDeltaE = 0; Ej = 0
    self.eCache_[i] = [1, Ei]
    validEcacheList = nonzero(self.eCache[:,0].A)[0]
    if (len(validEcacheList)) > 1:
      for k in validEcacheList:
        if k == i: continue
        Ek = self.calcEk(k)
        deltaE = abs(Ei - Ek)
        if (deltaE > maxDeltaE):
          maxK = k; maxDeltaE = deltaE; Ej = Ek
      return maxK, Ej
    else:
      j = self.selectJRand(i, self.m_)
      Ej = self.calcEk(j)
      return j, Ej

  def updateEk(self, k):
    Ek = self.calcEk(k)
    self.eCache_[k] = [1,Ek]

  def innerL(self, i):
    Ei = self.calcEk(i)


  def smoP(self, C, toler, maxIter, kTup=('lin', 0)):
    self.K_ = mat(zeros((self.m_, self.m_)))
    for i in range(self.m_):
      self.K_[:,i] = self.kernel_.kernelTrans(self.dataMat_, self.dataMat_[i,:], kTup)


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
          j = self.selectJRand(i, m)
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

