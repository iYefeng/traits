#ifndef __AC_H__
#define __AC_H__

#include <vector>
#include <set>
#include <map>
#include <tr1/memory>
#include <string>

using namespace std;
using namespace std::tr1;

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
    int insert(const char *str);
    int buildFailPath();
    int match(const string doc);

    pNode root_;
    map<int, string> patternIdx_;
    int patternCnt_;
    vector<acResult> result_;
};

int AC::loadPattern(const vector<string> vStr)
{
  int size_t len = vStr.size();
  for (int i = 0; i < len; ++i) {
    patternIdx_.insert(pair<int, string>(patternCnt_, vStr[i]));
    ++patternCnt_;
  }
  return 0;  
}

int AC::buildTree()
{
  pNode tmp1 = NULL, tmp2 = NULL;
  for (map<int, string>::iterator iter = patternIdx_.begin(); iter != patternIdx_.end(); ++iter) {
    tmp1 = root;

  }
}


#endif // __AC_H__
