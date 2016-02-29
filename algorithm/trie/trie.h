#ifndef _TRIE_H_
#define _TRIE_H_

#include <map>
#include <tr1/memory>

using namespace std;
using namespace std::tr1::shared_ptr;

class trieNode {
  public:
    trieNode() : terminableSize(0), nodeSize(0) {}
    map<long, shared_ptr<trieNode>> next;
    
  private:
    long terminableSize;
    long nodeSize;
}

class trie {
  public:
    typedef trieNode Node;
    typedef shared_ptr<trieNode> pNode;

    trie() : root(new Node) {}

    void insert(const wchar_t *wc);
    bool find(const wchar_t *wc);
    bool erase(const wchar_t *wc);
    long sizeAll(pNode node);
    long sizeNoneRedundant(pNode node);

  private:
    pNode root;
}

void trie::insert(const wchar *wc)
{
  pNode cur = root;
  pNode pre;
  int len = wcslen(wc);
  int i;
  long key;
  map<long, pNode>::iterator iter;
  for ( i = 0; i < len; ++i) {
    key = (long) wc[i];
    iter = cur->next.find(wc[i])
    if (iter == cur->next.end()) {
      cur->next.insert(key, new Node);
      ++cur->nodeSize;
    }
    pre = cur;
    iter = cur->next.find(key)
    cur = iter->secode;
  }
  ++pre->terminableSize;
}

bool trie::find(const wchar_t *wc)
{
  pNode cur = root;
  pNode pre;
  int len = wcslen(wc);
  long key;
  map<long, pNode>::iterator iter;
  for (i 

}

#endif
