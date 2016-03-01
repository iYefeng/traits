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
    }
    pre = cur;
    iter = cur->next.find(key);
    cur = iter->second;
    ++cur->nodeSize;
  }
  ++cur->terminableSize;
}

bool trie::find(const char *str)
{
  wchar_t wc[1024] = {'\0'};
  pNode cur = root;
  //pNode pre;
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
    //pre = cur;
    cur = iter->second;
  }
  if (cur->terminableSize > 0)
    return true;
  return false;
}

bool trie::erase(const char *str) 
{
  printf("i_want_to_erase_%s\n", str);
  wchar_t wc[1024] = {'\0'};
  mbstowcs(wc, str, strlen(str));
  int len = wcslen(wc);
  if (find(str)) {
    printf("erasing_ %s\n", str);
    pNode cur = root;
    pNode pre;
    map<long, pNode>::iterator iter;
    int len = wcslen(wc);
    for (int i = 0; i < len; ++i) {
      iter = cur->next.find(wc[i]);
      pre = cur;
      cur = iter->second;
      --cur->nodeSize;
      if (cur->nodeSize == 0) {
        printf("delete %s\n", str);
        delete cur;
        (pre->next).erase(iter);
        return true;
      }
    }
    if (cur->terminableSize > 0) 
      --cur->terminableSize;
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
