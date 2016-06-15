#!/usr/bin/env python
# -*- coding: utf-8 -*-

from numpy import *

class Bayes:

  def createVocabList(self, dataSet):
    vocabSet = set()
    for doc in dataSet:
      vocabSet = vocabSet | set(doc)
    return list(vocabSet)

  def words2Vec(self, inputSet):
    retVec = [0] * len(self.vocabList_)
    for word in inputSet:
      if word in self.vocabList_:
        retVec[self.vocabList_.index(word)] += 1
    return retVec
  
  def _train(self, trainMaxtrix, trainLabel):
    numTrainDocs = len(trainMaxtrix)
    numWords = len(trainMaxtrix[0])
    pAbusive = sum(trainLabel) / float(numTrainDocs)
    p0Num = ones(numWords)
    p1Num = ones(numWords)
    p0Denom = 2.0
    p1Denom = 2.0
    for i in xrange(numTrainDocs):
      if trainLabel[i] == 1:
        p1Num += trainMaxtrix[i]
        p1Denom += sum(trainMaxtrix[i])
      else:
        p0Num += trainMaxtrix[i]
        p0Denom += sum(trainMaxtrix[i])
    p1Vect = log(p1Num / p1Denom)
    p0Vect = log(p0Num / p0Denom)
    return p0Vect, p1Vect, pAbusive

  def train(self, dataSet, labels):
    self.vocabList_ = self.createVocabList(dataSet)
    trainMat = []
    for doc in dataSet:
      trainMat.append(self.words2Vec(doc))
    self.p0V_, self.p1V_, self.pAb_ = self._train(array(trainMat), array(labels))


  def classify(self, vec2Classify):
    p1 = sum(vec2Classify * self.p1V_) + log(self.pAb_)
    p0 = sum(vec2Classify * self.p0V_) + log(1 - self.pAb_)
    if p1 > p0:
      return 1
    else:
      return 0


if __name__ == "__main__":
  docs = [
      ['my', 'dog', 'has', 'flea', 'problems', 'help', 'please'],
      ['maybe', 'not', 'take', 'him', 'to', 'dog', 'park', 'stupid'],
      ['my', 'dalmation', 'is', 'so', 'cute', 'I', 'love', 'him'],
      ['stop', 'posting', 'stupid', 'worthless', 'garbage'],
      ['mr', 'licks', 'ate', 'my', 'steak', 'how', 'to', 'stop', 'him'],
      ['quit', 'buying', 'worthless', 'dog', 'food', 'stupid']
    ]
  labels = [0,1,0,1,0,1]

