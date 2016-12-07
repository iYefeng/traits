/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-07 22:16
 * Last modified : 2016-12-07 22:16
 * Filename      : ContinuMax.cc
 * Description   :  写一个函数,它的原形是 
 *                  int continumax(char *outputstr,char *intputstr)
 *                  功能:
 *                      在字符串中找出连续最长的数字串,并把这
 *                      个串的长度返回, 并把这个最长数字串付给
 *                      其中一个函数参数 outputstr 所指内存。 
 *                  例如:
 *                      "abcd12345ed125ss123456789"的首地址传
 *                      给intputstr 后,函数将返回9, outputstr
 *                      所指的值为123456789
 * *********************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <iostream>

using namespace std;

int continumax(char *outputstr, char *inputstr)
{
  int len = 0;
  int max = 0;
  char *pstart = NULL;
  while(true) {
    if (*inputstr >= '0' && *inputstr <= '9') {
      len ++;
    } else {
      if (len > max) {
        max = len;
        pstart = inputstr - len;
      }
      len = 0;
    }
    if (*inputstr++ == '\0') break;
  }
  for (int i = 0; i < max; ++i) {
    *outputstr++ = *pstart++;
  }
  *outputstr = '\0';
  return max;
}

int main(int args, char *argv[])
{
  char input[] = "abcd12345ed125ss123456789";
  char output[100];
  int ret = continumax(output, input);
  printf("%d , %s\n", ret, output);
  return 0;
}
