# -*- coding: utf-8 -*-

import sys
import math
from texttable import Texttable

class RecommendByUser(object):

  # input: 1.item info file; 2.user rate file
  def __init__(self, rate_file, item_file):
    self.item_info_ = self.parseItemInfo(item_file)
    self.userDict_, self.itemUser_ = self.parseRatingInfo(rate_file)

  def getItemInfo(self):
    return self.item_info_

  def parseItemInfo(self, filename):
    data = self.readFile(filename)
    item_info = {}
    for line in data:
      single_info = line.split("|")
      item_info[single_info[0]] = single_info[1:]
    return item_info
  
  def parseRatingInfo(self, filename):
    userDict = {}
    itemUser = {}
    data = self.readFile(filename)
    for line in data:
      tmp = line.split("\t")
      k = (tmp[0], tmp[1], int(tmp[2]))
      user_rank = (k[1], k[2])
      if k[0] in userDict:
        userDict[k[0]].append(user_rank)
      else:
        userDict[k[0]] = [user_rank]

      if k[1] in itemUser:
        itemUser[k[1]].append(k[0])
      else:
        itemUser[k[1]] = [k[0]]
    return userDict, itemUser

  # calculate cosine distance
  def getCosDist(self, user1, user2):
    sum_x = 0.0
    sum_y = 0.0
    sum_xy = 0.0
    for key1 in user1:
      for key2 in user2:
        if key1[0] == key2[0]:
          sum_x += key1[1] * key1[1]
          sum_y += key2[1] * key2[1]
          sum_xy += key1[1] * key2[1]
    if sum_xy == 0.0:
      return 0
    tmp = math.sqrt(sum_x * sum_y)
    return sum_xy / tmp
  
  def findNearestNeighbor(self, userId, userDict, itemUser):
    neighbors = []
    for item in userDict[userId]:
      for otherUser in itemUser[item[0]]:
        if otherUser != userId and otherUser not in neighbors:
          neighbors.append(otherUser)
    neighbors_dist = []
    for otherUser in neighbors:
      dist = self.getCosDist(userDict[userId], userDict[otherUser])
      neighbors_dist.append([dist, otherUser])
    neighbors_dist.sort(reverse = True)
    return neighbors_dist

  def readFile(self, filename):
    with open(filename, "r") as f:
      for line in f:
        yield line.strip("\n")
  
  # input: 1.userID; 2.neighbor count; 3. item count
  def recommend(self, userId, nc=5, ic=20):
    # nearest k neighbors
    nearestNeighbors = self.findNearestNeighbor(userId, self.userDict_, self.itemUser_)[:nc]
    recommend_dict = {}
    for neighbor in nearestNeighbors:
      neighbor_user_id = neighbor[1]
      items = self.userDict_[neighbor_user_id]
      for item in items:
        if item[0] in recommend_dict:
          recommend_dict[item[0]] += neighbor[0]
        else:
          recommend_dict[item[0]] = neighbor[0]
    
    recommend_list = []
    for key, val in recommend_dict.items():
      recommend_list.append([val, key])
    recommend_list.sort(reverse = True)
    user_items = [k[0] for k in self.userDict_[userId]]
    recommend_item = []
    for k in recommend_list:
      if k[1] not in set(user_items):
        recommend_item.append(k[1])
    return recommend_item[:ic], user_items

if __name__ == "__main__":
  rate_file = "../data/ml-100k/u.data"
  item_file = "../data/ml-100k/u.item"
  cf = RecommendByUser(rate_file, item_file)
  recommend_list, user_items = cf.recommend('50', 100, 20)
  item_info = cf.getItemInfo()
  table = Texttable()  
  table.set_deco(Texttable.HEADER)  
  table.set_cols_dtype(['t', 't', 't'])  
  table.set_cols_align(["l", "l", "l"])
  rows = []
  rows.append([u"movie name",u"release", u"from userid"])
  for item_id in recommend_list:
    rows.append([item_info[item_id][0], item_info[item_id][1], ""])
  table.add_rows(rows)
  print table.draw()



