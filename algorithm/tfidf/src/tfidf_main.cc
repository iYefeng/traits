#include <stdio.h> 
#include <stdlib.h> 
#include <getopt.h> //getopt_long()头文件位置 
#include <string.h>
#include "version.h"

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

typedef struct configura {
  char input[256];
  char output[256];
  char data[256];
  char conf[256];
  int mode;          // 0: train; 1:forecast
  int has_help;
  int has_version;
  int has_debug;
  int is_err;
} configura;

void print_help(const char *name)
{
  fprintf(stderr, __HELP__, name);
}

void print_version()
{
  printf("%s", __ABOUT__);
}

void parse_option(configura &conf, int argc, char ** argv)
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
        conf.has_version = 1;
        break;
      case 'h':
        conf.has_help = 1;
        break;
      case 0:   //flag不为NULL
        conf.has_debug = 1;
        break;
      case '?': //选项未定义
        fprintf(stderr, "ERROR: unknown command\n");
        conf.has_help = 1;
        break;
      default:
        fprintf(stderr, "ERROR: unknown command\n");
        conf.has_help = 1;
        break;
    }
  }
}


int run_train(configura &conf)
{
  printf("running train mode...\n");
  return 0; 
}

int run_forecast(configura &conf)
{
  printf("running forecast mode...\n");
  return 0; 
}

int run_tfidf(configura &conf, int argc, char **argv)
{
  int flag = 1;
  if (conf.has_help) {
    print_help(argv[0]);
    exit(0);
  }
  if (conf.has_version) {
    print_version();
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
  configura conf = {
    "", "", "", "conf/tfidf.ini", -1, 0, 0, 0
  };

  parse_option(conf, argc, argv);
  run_tfidf(conf, argc, argv);

  return 0;
}
