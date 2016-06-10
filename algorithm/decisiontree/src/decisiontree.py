#!/usr/bin/env python
# -*- coding: utf-8 -*-

from math import log

class DecisionTree():

  def calcShannonEnt(self, dataSet):
    numEntries = len(dataSet)
    labelCounts = {}
    for featVec in dataSet: #the the number of unique elements and their occurance
      currentLabel = featVec[-1]
      if currentLabel not in labelCounts.keys(): 
        labelCounts[currentLabel] = 0
      labelCounts[currentLabel] += 1
    shannonEnt = 0.0
    for key in labelCounts:
      prob = float(labelCounts[key])/numEntries
      shannonEnt -= prob * log(prob,2) #log base 2
    return shannonEnt


if __name__ == "__main__":
  tree = DecisionTree()
  dataSet = [
      [1, 1, 'yes'],
      [1, 1, 'yes'],
      [1, 0, 'no'],
      [0, 1, 'no'],
      [0, 1, 'no']
      ]
  print tree.calcShannonEnt(dataSet)
