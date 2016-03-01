#ifndef _TRIE_H_
#define _TRIE_H_

#include <map>
#include <tr1/memory>

using namespace std;
using namespace std::tr1;

class trieNode {
  public:
    trieNode() : terminableSize(0), nodeSize(0) {}
    ~trieNode() {
      map<long, trieNode*>::iterator iter;
      for (iter = next.begin(); iter != next.end(); ++iter) {
        delete iter->second;
      }
    }
    map<long, trieNode*> next;

    long terminableSize;
    long nodeSize;
};

class trie {
  public:
    typedef trieNode Node;
    typedef trieNode* pNode;

    trie() : root(new Node) {
      setlocale(LC_ALL, "");
    }
    ~trie() {
      delete root;
    }

    void insert(const char *str);
    bool find(const char *str);
    bool downNodeAlone(pNode node);
    bool erase(const char *str);
    long sizeAll(pNode node);
    long sizeNoneRedundant(pNode node);
    void printTrie(pNode node);

    pNode root;
};

void trie::insert(const char *str)
{
  wchar_t wc[1024] = {'\0'};
  pNode cur = root;
  pNode pre;
  mbstowcs(wc, str, strlen(str));
  int len = wcslen(wc);
  int i;
  long key;
  map<long, pNode>::iterator iter;
  for ( i = 0; i < len; ++i) {
    key = (long) wc[i];
    iter = cur->next.find(key);
    if (iter == cur->next.end()) {
      cur->next.insert(pair<long, pNode>(key, new Node));
      ++cur->nodeSize;
    }
    pre = cur;
    iter = cur->next.find(key);
    cur = iter->second;
  }
  ++pre->terminableSize;
}

bool trie::find(const char *str)
{
  wchar_t wc[1024] = {'\0'};
  pNode cur = root;
  pNode pre;
  mbstowcs(wc, str, strlen(str));
  int len = wcslen(wc);
  long key;
  map<long, pNode>::iterator iter;
  for (int i = 0; i < len; ++i) {
    key = (long) wc[i];
    iter = cur->next.find(key);
    if (iter == cur->next.end()) {
      return false;
    }
    pre = cur;
    cur = iter->second;
  }
  if (pre->terminableSize > 0)
    return true;
  return false;
}

bool trie::downNodeAlone(pNode node)
{
  pNode cur = node;
  int terminableSum = 0;
  while (cur->nodeSize != 0) {
    terminableSum += cur->terminableSize;
    if (cur->nodeSize > 1)
      return false;
    else {
      cur = (cur->next.begin())->second;
    }
  }
  if (terminableSum == 1)
    return true;
  return false;
}

bool trie::erase(const char *str) 
{
  wchar_t wc[1024] = {'\0'};
  mbstowcs(wc, str, strlen(str));
  int len = wcslen(wc);
  if (find(str)) {
    printf("erase %s\n", str);
    pNode cur = root;
    pNode pre;
    map<long, pNode>::iterator iter;
    int len = wcslen(wc);
    for (int i = 0; i < len; ++i) {
      if (downNodeAlone(cur)) {
        printf("delete %s\n", str);
        delete cur;
        return true;
      }
      iter = cur->next.find(wc[i]);
      pre = cur;
      cur = iter->second;
    }
    if (pre->terminableSize > 0) 
      --pre->terminableSize;
    return true;
  }
  return false;
}

long trie::sizeAll(pNode node)
{
  long rev = node->terminableSize;
  map<long, pNode>::iterator iter;
  for (iter = (node->next).begin(); \
        iter != (node->next).end(); ++iter) {
    rev += sizeAll(iter->second);
  }
  return rev;
}

long trie::sizeNoneRedundant(pNode node)
{
  long rev = 0;
  if (node->terminableSize > 0)
    rev = 1;
  map<long, pNode>::iterator iter;
  for (iter = (node->next).begin(); \
        iter != (node->next).end(); ++iter) {
    rev += sizeNoneRedundant(iter->second);
  }
  return rev;
}

void trie::printTrie(pNode node)
{
  map<long, pNode>::iterator iter;
  for (iter = (node->next).begin(); \
        iter != (node->next).end(); ++iter) {
    printf("%C", (wchar_t)iter->first);
    printTrie(iter->second);
  }
}

#endif
