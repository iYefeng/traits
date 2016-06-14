#!/usr/bin/env python
# -*- coding: utf-8 -*-

class Bayes:

  def createVocabList(self, dataSet):
    vocabSet = set()
    for doc in dataSet:
      vocabSet = vocabSet | set(doc)
    return list(vocabSet)

  def words2Vec(self, vocabList, inputSet):
    retVec = [0] * len(vocabList)
    for word in inputSet:
      if word in vocabList:
        retVec[vocabList.index(word)] += 1
    return retVec
  
  def _train(self, trainMaxtrix, trainLabel):


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

