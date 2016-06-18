#!/usr/bin/env python
# -*- coding: utf-8 -*-

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

#计算均值,要求输入数据为numpy的矩阵格式，行表示样本数，列表示特征
def meanX(dataX):
  return np.mean(dataX, axis=0) #axis=0表示按照列来求均值，如果输入list,则axis=1

"""
参数：
 - XMat：传入的是一个numpy的矩阵格式，行表示样本数，列表示特征 
 - k：表示取前k个特征值对应的特征向量
返回值：
 - finalData：参数一指的是返回的低维矩阵，对应于输入参数二
 - reconData：参数二对应的是移动坐标轴后的矩阵
"""
def pca(XMat, k):
  average = meanX(XMat)
  m, n = np.shape(XMat)
  data_adjust = []
  avgs = np.tile(average, (m, 1))
  data_adjust = XMat - avgs
  covX = np.cov(data_adjust.T)   #计算协方差矩阵

