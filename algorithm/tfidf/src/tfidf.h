#ifndef _TFIDF_H_
#define _TFIDF_H_

#ifdef __cplusplus
extern "C" {
#endif

#include "friso_API.h"
#include "friso.h"

#ifdef __cplusplus
}
#endif

#include <string>
#include <set>
#include <map>

using namespace std;

class tfidf {
  public:
    typedef set<string> wordSet;

    tfidf() : sample_num(0) {}
    int train(const string &input);
    int forecast(const string &input, map<string, double> tfidf);
    int wordSegment(const string &input, wordSet output);
    int saveMiddleData();
    int loadMiddleData();

  private:
    int documentNum;
    int wordNum;
    // count documents which contains word[i] 
    map<string, int> docCnt; 
    // count word[i] in the document
    map<string, int> termCnt; 
};

#endif // tfidf.h
