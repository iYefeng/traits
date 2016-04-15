#ifndef __AC_H__
#define __AC_H__

#include <map>
#include <tr1/memory>

using namespace std;
using namespace std::tr1;

class acNode {
  public:
    acNode(): terminableSize(0), nodeSize(0) {}
    ~acNode() {
      for (map<long, acNode*>::iterator iter = next.begin(); \
            iter != next.end(); ++iter) {
        delete iter->second;
      }
    }

    int patternTag;
    int patternNo;
    int terminableSize;
    int nodeSize;
    acNode* parent;
    acNode* fail;
    map<long, acNode*> next;
};

class ac {

};


#endif // __AC_H__
