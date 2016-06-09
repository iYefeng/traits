#include "ac.h"

int main()
{
  AC testas;
  string word[]={"nihao","hao","hs","hsr", "是天"};
  string a = "sdmfhs叶峰是天才gnshejfgnihaofhsrnihao";
  size_t s_count=sizeof(word)/sizeof(string);
  vector<string> pattern(word,word+s_count);
  testas.loadPattern(pattern);
  testas.buildTree();
  testas.buildFailPath();
  testas.match(a);

  return 0;
}
