#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

#计算均值,要求输入数据为numpy的矩阵格式，行表示样本数，列表示特征
def meanX(dataX):
  return np.mean(dataX, axis=0) #axis=0表示按照列来求均值，如果输入list,则axis=1

def dataAdjust(dataX):
  average = meanX(dataX)
  m, n = np.shape(dataX)
  data_adjust = []
  avgs = np.tile(average, (m,1))
  data_adjust = dataX - avgs
  return data_adjust, average

"""
参数：
 - XMat：传入的是一个numpy的矩阵格式，行表示样本数，列表示特征 
 - k：表示取前k个特征值对应的特征向量
返回值：
 - finalData：参数一指的是返回的低维矩阵，对应于输入参数二
 - reconData：参数二对应的是移动坐标轴后的矩阵
"""
def pca(XMat, k):
  m, n = np.shape(XMat)
  data_adjust, average = dataAdjust(XMat)
  covX = np.cov(data_adjust.T)   #计算协方差矩阵
  featValue, featVec = np.linalg.eig(covX)
  index = np.argsort(-featValue)
  finalData = []
  if k > n:
    print "k must be less than feature number"
    return
  else:
    selectVec = np.matrix(featVec.T[index[:k]])
    finalData = data_adjust * selectVec.T
    reconData = (finalData * selectVec) + average
    return finalData, reconData

def loaddata(datafile):
  return np.array(pd.read_csv(datafile, sep="\t", header=-1)).astype(np.float)

def plotBestFit(data1, data2):
  dataArr1 = np.array(data1)
  dataArr2 = np.array(data2)

  m = np.shape(dataArr1)[0]
  axis_x1 = []
  axis_y1 = []
  axis_x2 = []
  axis_y2 = []
  for i in range(m):
    axis_x1.append(dataArr1[i, 0])
    axis_y1.append(dataArr1[i, 1])
    axis_x2.append(dataArr2[i, 0])
    axis_y2.append(dataArr2[i, 1])
  fig = plt.figure()
  ax = fig.add_subplot(111)
  ax.scatter(axis_x1, axis_y1, s=50, c='red', marker='s')
  ax.scatter(axis_x2, axis_y2, s=50, c='blue')
  plt.xlabel('x1')
  plt.ylabel('x2')
  plt.savefig("outfile.png")
  plt.show()

#根据数据集data.txt
def main():    
  datafile = "../data/data.txt"
  XMat = loaddata(datafile)
  k = 2
  return pca(XMat, k)

if __name__ == "__main__":
  finalData, reconMat = main()
  plotBestFit(finalData, reconMat)
