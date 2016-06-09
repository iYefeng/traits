/**
 * Aho-Corasick
 * 1.建立模式的Trie
 * 2.给Trie添加失败路径
 * 3.根据AC自动机，搜索待处理的文本
 * */

#ifndef __AC_H__
#define __AC_H__

#include <vector>
#include <set>
#include <map>
#include <queue>
#include <tr1/memory>
#include <string>
#define DEBUG
#include <assert.h>
#include <iostream>
#include <stdlib.h>
#include <stdio.h>

using namespace std;
using namespace std::tr1;

std::wstring c2w(const char *pc)
{
  std::wstring val = L"";

  if(NULL == pc)
  {
    return val;
  }
  //size_t size_of_ch = strlen(pc)*sizeof(char);
  //size_t size_of_wc = get_wchar_size(pc);
  size_t size_of_wc;
  size_t destlen = mbstowcs(0,pc,0);
  if (destlen ==(size_t)(-1))
  {
    return val;
  }
  size_of_wc = destlen+1;
  wchar_t * pw  = new wchar_t[size_of_wc];
  mbstowcs(pw,pc,size_of_wc);
  val = pw;
  delete pw;
  return val;
}

std::string w2c(const wchar_t * pw)
{
  std::string val = "";
  if(!pw)
  {
    return val;
  }
  size_t size= wcslen(pw)*sizeof(wchar_t);
  char *pc = NULL;
  if(!(pc = (char*)malloc(size)))
  {
    return val;
  }
  size_t destlen = wcstombs(pc,pw,size);
  /*转换不为空时，返回值为-1。如果为空，返回值0*/
  if (destlen ==(size_t)(-1))
  {
    delete pc;
    return val;
  }
  val = pc;
  delete pc;
  return val;
}

class AcNode {
  public:
    AcNode(): terminableSize_(0), nodeSize_(0), patternTag_(0), \
              parent_(NULL), fail_(NULL) {}
    ~AcNode() {
      for (map<long, AcNode*>::iterator iter = next_.begin(); \
            iter != next_.end(); ++iter) {
        delete iter->second;
      }
    }

    int patternTag_;
    int patternNo_;
    int terminableSize_;
    int nodeSize_;
    AcNode* parent_;
    AcNode* fail_;
    map<long, AcNode*> next_;
};

class AC {
  public:
    typedef AcNode Node;
    typedef AcNode* pNode;
    typedef struct result {
      string pattern;
      int location;
    } acResult;
    
    AC(): root_(new Node), patternCnt_(0) {
      setlocale(LC_ALL, "");
    }
    ~AC() {
      delete root_;
    }
  
    int loadPattern(const vector<string> vStr);
    int buildTree();
    //int insert(const char *str);
    int nodeToQueue(pNode root, queue< pair<long, pNode> > &acQueue);
    int buildFailPath();
    int match(const string doc);
    int output(int patternNo);

    pNode root_;
    map<int, wstring> patternIdx_;
    int patternCnt_;
    vector<acResult> result_;
};

int AC::loadPattern(const vector<string> vStr)
{
  size_t len = vStr.size();
  for (int i = 0; i < len; ++i) {
    wstring tmp = c2w(vStr[i].c_str());
    patternIdx_.insert(pair<int, wstring>(patternCnt_, tmp));
    ++patternCnt_;
  }
  return 0;  
}

int AC::buildTree()
{
  pNode cur = NULL, pre = NULL;
  long key;
  map<long, pNode>::iterator idx;
  pair<map<long, pNode>::iterator, bool> ret;
  for (map<int, wstring>::iterator iter = patternIdx_.begin(); iter != patternIdx_.end(); ++iter) {
    cur = root_;
    for (size_t j = 0; j < iter->second.size(); ++j) {
      key = (long) iter->second[j];
      idx = cur->next_.find(key);
      if (idx == cur->next_.end()) {
        ret = cur->next_.insert(pair<long, pNode>(key, new Node));
        if (ret.second) {
          idx = ret.first;
          idx->second->parent_ = cur;
        }
      }
      assert(idx != cur->next_.end());
      pre = cur;
      cur = idx->second;
      ++cur->nodeSize_;
    }
    ++cur->terminableSize_;
    cur->patternTag_ = 1;
    cur->patternNo_ = iter->first;
  }
  return 0;
}

int AC::nodeToQueue(pNode root, queue< pair<long, pNode> > &acQueue)
{
  map<long, AcNode*>::iterator iter;
  for (iter = root->next_.begin(); iter != root->next_.end(); ++iter) {
    acQueue.push(pair<long, AcNode*>(iter->first, iter->second));
  }
  return 0;
}

int AC::buildFailPath()
{
  int i;
  long key;
  queue< pair<long, pNode> > acQueue;
  map<long, pNode>::iterator iter;
  root_->fail_ = root_;
  for (iter = root_->next_.begin(); \
        iter != root_->next_.end(); ++iter) {
    nodeToQueue(iter->second, acQueue);
    iter->second->fail_ = root_;
  }
  pNode tmp = NULL, parent = NULL;
  pair<long, pNode> pairtmp;
  while (!acQueue.empty()) {
    pairtmp = acQueue.front();
    key = pairtmp.first;
    tmp = pairtmp.second;
    acQueue.pop();
    nodeToQueue(tmp, acQueue);
    parent = tmp->parent_;
    while (true) {
      iter = parent->fail_->next_.find(key);
      if (iter != parent->fail_->next_.end()) {
        tmp->fail_ = iter->second;
        break;
      } else {
        if (parent->fail_ == root_) {
          tmp->fail_ = root_;
          break;
        } else
          parent = parent->fail_->parent_;
      }
    }
  }
  return 0;
}

int AC::output(int patternNo) {
  map<int, wstring>::iterator patIter;
  patIter = patternIdx_.find(patternNo);
  assert(patIter != patternIdx_.end());
  cout << "patternNo: ";
  cout << patternNo;
  cout << "pattern: ";
  cout << w2c(patIter->second.c_str());
  cout << endl;
  return 0;
}

int AC::match(const string doc)
{
  pNode tmp = root_;
  wstring wstr = c2w(doc.c_str());
  int len = wstr.size();
  int i = 0;
  long key;
  map<long, pNode>::iterator iter;
  while(i < len) {
    key = wstr[i];
    iter = tmp->next_.find(key);
    if (iter != tmp->next_.end()) {
      tmp = iter->second;
      if (tmp->patternTag_ == 1) {
        output(tmp->patternNo_);       
      }
      i++;
    } else {
      if (tmp == root_) {
        i++;    
      } else {
        tmp = tmp->fail_;
      } if (tmp->patternTag_ == 1) {
        output(tmp->patternNo_);       
      }
    }
  }
  while (tmp != root_) {
    tmp = tmp->fail_;
    if (tmp->patternTag_ == 1) {
      output(tmp->patternNo_);       
    }
  }
  return 0;  
}

int main()
{
  AC testas;
   

  return 0;
}

#endif // __AC_H__
