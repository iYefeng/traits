#include <stdio.h>
#include <iostream>
#include <stdlib.h> 
#include <getopt.h> //getopt_long()头文件位置 
#include <string.h>
#include "version.h"
#include "tfidf.h"
#include <fstream>
#include <string>
#include <time.h>
#include <algorithm>

using namespace std;

typedef pair<string, double> PAIR;

int cmp(const PAIR &x, const PAIR &y)
{
  return x.second > y.second;
}

const char *__HELP__ = 
" tf-idf\n"
" Author: yefeng\n"
" Date: "__DATE__"\n"
"\n"
" Usage: %s [options]\n"
" Options:\n"
" -i FILE,     --input=FILE      Texts input file path\n"
" -o FILE,     --output=FILE     Output forecast results path\n"
" -m [train|forcast],   --mode=[train|forecast]\n"
" -d DataFile, --data=DataFile   Store idf results\n"
" -c ConfFile, --conf=ConfFile   conf file path\n"
" -h,          --help            Help information\n"
" -v,          --version         Show version\n"
"              --debug\n";

const char *__ABOUT__ = 
" TF-IDF\n"
" Author:   Yefeng\n"
" Date:     "__TIME__" "__DATE__"\n"
" Version:  "VERSION_NUMBER"\n"; 

void tfidf_help(const char *name)
{
  fprintf(stderr, __HELP__, name);
}

void tfidf_version()
{
  printf("%s", __ABOUT__);
}

void parse_option(configure &conf, int argc, char ** argv)
{
  const char *optstring="i:o:m:d:c:u:hv";
  int c, deb, index;
  struct option opts[]={
    {"input",     required_argument,  NULL, 'i'},
    {"output",    required_argument,  NULL, 'o'},
    {"mode",      required_argument,  NULL, 'm'},
    {"data",      required_argument,  NULL, 'd'},
    {"conf",      required_argument,  NULL, 'c'},
    {"version",   no_argument,        NULL, 'v'},
    {"debug",     no_argument,        &deb, 1  },
    {"help",      no_argument,        NULL, 'h'},
    {0,           0,                  0,    0  }
  };
  while((c=getopt_long(argc, argv, optstring, opts, &index))!=-1) 
  { 
    switch(c) 
    { 
      case 'i': //-i or --input
        strcpy(conf.input, optarg);
        break;
      case 'o': // -o or --output
        strcpy(conf.output, optarg);
        break;
      case 'd':
        strcpy(conf.data, optarg);
        break;
      case 'c':
        strcpy(conf.conf, optarg);
        break;
      case 'm': // -m or --mode
        if ( 0 == strcmp(optarg, "train"))
          conf.mode = 0;
        else if ( 0 == strcmp(optarg, "forecast"))
          conf.mode = 1;
        else
          conf.mode = -1;
        break;
      case 'v': //-v 或者--version,输出版本号
        conf.hasVersion = 1;
        break;
      case 'h':
        conf.hasHelp = 1;
        break;
      case 0:   //flag不为NULL
        conf.hasDebug = 1;
        break;
      case '?': //选项未定义
        fprintf(stderr, "ERROR: unknown command\n");
        conf.hasHelp = 1;
        break;
      default:
        fprintf(stderr, "ERROR: unknown command\n");
        conf.hasHelp = 1;
        break;
    }
  }
}

int run_train(configure &conf)
{
  clock_t s_time, e_time;
  printf("running train mode...\n");
  s_time = clock();
  // FIXME: move the conf to conf file.
  strcpy(conf.frisoPath, "conf/friso.ini");
  strcpy(conf.data, "data/idf.dat");
  tfidf t(conf);
  fstream fin(conf.input);
  string readLine;
  while (getline(fin, readLine)) {
    t.train(readLine);
  }
  fin.close();
  printf("Saving the middle file in %s ...\n", conf.data);
  t.save_middle_data();
  e_time = clock();
  printf("Finished training in %fsec\n", (double) ( e_time - s_time ) / CLOCKS_PER_SEC );
  
  return 0; 
}

int run_forecast(configure &conf)
{
  clock_t s_time, e_time;
  // FIXME: move the conf to conf file.
  strcpy(conf.frisoPath, "conf/friso.ini");
  strcpy(conf.data, "data/idf.dat");
  tfidf t(conf);
  printf("Running forecast mode...\n");
  s_time = clock();
  printf("Loading the middle data from %s\n", conf.data);
  t.load_middle_data();
  fstream fin(conf.input);
  string readLine;
  while (getline(fin, readLine)) {
    map<string, double> result;
    vector<PAIR> pair_vec;
    t.forecast(readLine, result);
    for (map<string, double>::iterator rit = result.begin(); rit != result.end(); ++rit) {
      //printf("%s\t%lf\n", rit->first.c_str(), rit->second);
      pair_vec.push_back(make_pair(rit->first, rit->second));
    }
    sort(pair_vec.begin(), pair_vec.end(), cmp);
    // FIXME: output result to file or stdout
    for (vector<PAIR>::iterator curr = pair_vec.begin(); 
          curr != pair_vec.end(); ++curr) {  
      printf("%s\t%lf\n", curr->first.c_str(), curr->second);
    } 
    printf("================\n");
  }
  fin.close();

  e_time = clock();
  printf("Finished forecasting in %fsec\n", (double) ( e_time - s_time ) / CLOCKS_PER_SEC );
  return 0; 
}

int run_tfidf(configure &conf, int argc, char **argv)
{
  int flag = 1;
  if (conf.hasHelp) {
    tfidf_help(argv[0]);
    exit(0);
  }
  if (conf.hasVersion) {
    tfidf_version();
    exit(0);
  }
  switch (conf.mode) {
    case 1:
      if (0 == strlen(conf.input)) {
        printf("please set input file\n");
        flag = 0;
      }
      if (0 == strlen(conf.output)) {
        printf("please set output file\n");
        flag = 0;
      }
      if (0 == flag) {
        exit(-1);
      }
      run_forecast(conf);
      break;
    case 0:
      if (0 == strlen(conf.input)) {
        printf("please set input file\n");
        flag = 0;
      }
      if (0 == flag) {
        exit(-1);
      }
      run_train(conf);
      break;
    case -1:
      printf("please choose mode: train or forecast\n");
      break;
  }
  
  return 0;
}


int main(int argc, char** argv)
{
  configure conf = {
    "", "", "", "conf/tfidf.ini", "", -1, 0, 0, 0
  };

  parse_option(conf, argc, argv);
  run_tfidf(conf, argc, argv);

  return 0;
}
