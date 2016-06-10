#!/usr/bin/env python
# -*- coding: utf-8 -*-

from math import log
import simplejson as json

class DecisionTree():

  def __init__(self, name=None, dataSet=None, labels=None, infilename=None, outfilename=None):
    if infilename:
      self.isModelExists_ = True
      self.infilename_ = infilename
      tree, labels = self.grabTree(infilename)
      self.outfilename_ = outfilename
      self.tree_ = tree
      self.labels_ = labels
      self.dataSet_ = None
      self.name_ = name if name else "test"
    else:
      self.isModelExists_ = False
      self.infilename_ = infilename
      self.outfilename_ = outfilename
      self.tree_ = None
      self.labels_ = labels
      self.dataSet_ = dataSet
      self.name_ = name if name else "test"

  def __del__(self):
    if not self.outfilename_:
      if not self.isModelExists_ and self.name_:
        filename = "../models/%s.model" % self.name_
        self.storeTree(filename)
    else:
      self.storeTree(self.outfilename_)

  def grabTree(self, filename):
    fr = open(filename)
    data = json.loads(fr.read())
    fr.close()
    return (data["tree"], data["labels"])

  def storeTree(self, filename):
    fw = open(filename, 'w')
    data = {}
    data["tree"] = self.tree_
    data["labels"] = self.labels_
    out = json.dumps(data, ensure_ascii=False)
    fw.write(out)
    fw.close()

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

  def splitDataSet(self, dataSet, axis, value):
    retDataSet = []
    for featVec in dataSet:
      if featVec[axis] == value:
        reducedFeatVec = featVec[:axis]
        reducedFeatVec.extend(featVec[axis+1:])
        retDataSet.append(reducedFeatVec)
    return retDataSet

  def chooseBestFeatureToSplit(self, dataSet):
    numFeatures = len(dataSet[0]) - 1
    baseEntropy = self.calcShannonEnt(dataSet)
    bestInfoGain = 0.0; bestFeature = -1
    for i in range(numFeatures):
      featList = [example[i] for example in dataSet]
      uniqueVals = set(featList)
      newEntropy = 0.0
      for value in uniqueVals:
        subDataSet = self.splitDataSet(dataSet, i, value)
        prob = len(subDataSet)/float(len(dataSet))
        newEntropy += prob * self.calcShannonEnt(subDataSet)
      infoGain = baseEntropy - newEntropy
      if (infoGain > bestInfoGain):
        bestInfoGain = infoGain
        bestFeature = i
    return bestFeature
  
  def majorityCnt(self, classList):
    classCount = {}
    for vote in classList:
      if vote not in classCount.keys(): classCount[vote] = 0
      classCount[vote] += 1
    sortedClassCount = sorted(classCount.iteritems(), \
        key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]

  def createTree(self):
    import copy
    if self.dataSet_ and self.labels_:
      tmpLabels = copy.deepcopy(self.labels_)
      self.tree_ = self._createTree(self.dataSet_, tmpLabels)

  def _createTree(self, dataSet, labels):
    classList = [example[-1] for example in dataSet]
    if classList.count(classList[0]) == len(classList):
      return classList[0]
    if len(dataSet[0]) == 1:
      return self.majorityCnt(classList)
    bestFeat = self.chooseBestFeatureToSplit(dataSet)
    bestFeatLabel = labels[bestFeat]
    myTree = {bestFeatLabel:{}}
    del(labels[bestFeat])
    featValues = [example[bestFeat] for example in dataSet]
    uniqueVals = set(featValues)
    for value in uniqueVals:
      subLabels = labels[:]
      myTree[bestFeatLabel][str(value)] = self._createTree(self.splitDataSet(dataSet, bestFeat, value), subLabels)
    return myTree

  def classify(self, testVec):
    if self.tree_ and self.labels_:
      return self._classify(self.tree_, self.labels_, testVec)
    else:
      print "please create tree first"
      return None

  def _classify(self, inputTree, featLabels, testVec):
    firstStr = inputTree.keys()[0]
    secondDict = inputTree[firstStr]
    featIndex = featLabels.index(firstStr)
    key = testVec[featIndex]
    valueOfFeat = secondDict[str(key)]
    if isinstance(valueOfFeat, dict): 
      classLabel = self._classify(valueOfFeat, featLabels, testVec)
    else: classLabel = valueOfFeat
    return classLabel

if __name__ == "__main__":
  dataSet = [
      [1, 1, 'yes'],
      [1, 1, 'yes'],
      [1, 0, 'no'],
      [0, 1, 'no'],
      [0, 1, 'no']
      ]
  labels = ['no surfacing','flippers']
  tree = DecisionTree("test", dataSet, labels)
  tree.createTree()
  print tree.classify([0,0])
  #print tree.calcShannonEnt(dataSet)
  #print tree.splitDataSet(dataSet, 0, 1)
  #print tree.splitDataSet(dataSet, 0, 0)
  #print tree.chooseBestFeatureToSplit(dataSet)
  #print tree.createTree(dataSet, labels)
  del tree
  tree1 = DecisionTree(infilename="../models/test.model")
  print tree1.classify([1,1])
  print tree1.classify([0,1])

