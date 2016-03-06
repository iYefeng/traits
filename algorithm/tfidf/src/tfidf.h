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

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <set>
#include <map>
#include <vector>
#include <iostream>
#include <cmath>
#include <fstream>

typedef struct configure {
  char input[256];
  char output[256];
  char data[256];
  char conf[256];
  char frisoPath[256];
  int mode;          // 0: train; 1:forecast
  int hasHelp;
  int hasVersion;
  int hasDebug;
  int isErr;
} configure;

using namespace std;

class tfidf {
  public:
    tfidf(configure config);
    ~tfidf();
    int train(const string input);
    int forecast(const string input, map<string, double> &tfidf);
    int word_segment(const string input, vector<string> &output);
    int save_middle_data();
    int load_middle_data();
    
    void print_doc_cnt();

  private:
    friso_t friso;
    friso_config_t frisoConfig;
    friso_task_t frisoTask;
    configure conf;

    int documentNum;
    int wordNum;
    // count documents which contains word[i] 
    map<string, int> docCnt; 
    // count word[i] in the document
    map<string, int> termCnt; 
};

tfidf::tfidf(configure config): documentNum(0), wordNum(0)
{
  friso = friso_new();
  frisoConfig = friso_new_config();
  if ( friso_init_from_ifile(friso, frisoConfig, config.frisoPath) \
        != 1 ) {
    fprintf(stderr, "fail to initialize friso and config.");
    exit(-1);
  }
  frisoTask = friso_new_task();
  conf = config;
}

tfidf::~tfidf()
{
  friso_free_task(frisoTask);
  friso_free_config(frisoConfig);
  friso_free(friso);
}

int tfidf::word_segment(const string input, 
      vector<string> &output)
{
  friso_set_text( frisoTask, (fstring)input.c_str() );

  while ( ( friso_next( friso, frisoConfig, frisoTask ) ) \
        != NULL ) {
    //printf("%s[%d, %d] ", task->hits->word, 
    //	    task->hits->offset, task->hits->length );
    output.push_back(frisoTask->hits->word);
  }

  return 0;
}

int tfidf::train(const string input)
{
  vector<string> wordVector;
  set<string> wordSet;
  map<string, int>::iterator tmpIt;
  documentNum += 1;
  word_segment(input, wordVector);
  for (vector<string>::iterator vit = wordVector.begin(); vit != wordVector.end(); ++vit) {
    wordSet.insert((*vit));
  }

  for (set<string>::iterator sit = wordSet.begin(); sit != wordSet.end(); ++sit) {
    tmpIt = docCnt.find((*sit));
    if ( tmpIt != docCnt.end() ) {
      ++ tmpIt->second;
    } else {
      docCnt.insert(make_pair((*sit), 1));
    }
  }
  return 0;
}
    
int tfidf::forecast(const string input, 
      map<string, double> &ret)
{
  double tf;
  double idf;
  wordNum = 0;
  vector<string> wordVector;
  map<string, int>::iterator tmpIt;
  termCnt.clear();
  word_segment(input, wordVector);
  for (vector<string>::iterator vit = wordVector.begin(); vit != wordVector.end(); ++vit) {
    ++wordNum;
    tmpIt = termCnt.find((*vit));
    if (tmpIt != termCnt.end()) {
      ++tmpIt->second;
    } else {
      termCnt.insert(make_pair((*vit), 1));
    }
  }
  for (map<string, int>::iterator mit = termCnt.begin(); mit != termCnt.end(); ++mit) {
    tf = (double)(mit->second) / wordNum;
    tmpIt = docCnt.find(mit->first);
    if (tmpIt != docCnt.end()) {
      idf = log((double)documentNum / (tmpIt->second));
    } else {
      idf = log((double)documentNum / 1);
    }
    ret.insert(make_pair(mit->first, tf*idf));
  }
  
  return 0;
}

void tfidf::print_doc_cnt()
{
  for (map<string, int>::iterator it = docCnt.begin(); it != docCnt.end(); ++it) {
    printf("%s\t%d\n", it->first.c_str(), it->second);
  }
  printf("total documents:%d\n", documentNum);
}

int tfidf::save_middle_data()
{
  ofstream fo(conf.data);
  fo << documentNum << endl;
  for (map<string, int>::iterator it = docCnt.begin(); it != docCnt.end(); ++it) {
    fo << it->first << "\t" << it->second << endl;
  }
  fo.close();
  return 0;  
}

int tfidf::load_middle_data()
{
  ifstream fi(conf.data);
  string key;
  int value;
  docCnt.clear();
  fi >> documentNum;
  while ( !fi.eof() ) {
    fi >> key >> value;
    docCnt.insert(make_pair(key, value));
  }
  
  fi.close();

  return 0;
}

#endif // tfidf.h
