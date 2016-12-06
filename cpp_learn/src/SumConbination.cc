/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-06 22:13
 * Last modified : 2016-12-06 22:13
 * Filename      : SumConbination.cc
 * Description   : 输入两个整数 n 和 m,从数列1,2,3.......n 中
 *                 随意取几个数, 使其和等于 m ,要求将其中所有
 *                 的可能组合列出来.
 * *********************************************************/

#include <iostream>

using namespace std;

bool *arr = NULL;

void printResult(int n)
{
  for (int i = 0; i < n; ++i) {
    if (arr[i] == true) {
      cout << i+1 << " ";
    }
  }
  cout << endl;
}

void doCom(int n, int m, int idx) 
{
  if (m == 0) {
    printResult(n);
    return;
  }
  if (idx < n) {
    doCom(n, m, idx+1);
    arr[idx] = true;
    doCom(n, m - idx - 1, idx+1);
    arr[idx] = false;
  }
}

void combination(int n, int m) 
{
  if (((1+n)*n/2) < m | m < 1) return;
  arr = new bool[n];
  for(int i = 0; i < n; ++i) {
    arr[i] = false;
  }
  doCom(n, m, 0);
  delete[] arr;
}


int main(int args, char *argv[])
{
  int n = 10;
  int m = 19;
  combination(n>m? m : n, m);
  return 0;
}
