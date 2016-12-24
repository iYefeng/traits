/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-24 01:04
 * Last modified : 2016-12-24 01:04
 * Filename      : avl_test.cc
 * Description   : 
 * *********************************************************/

#include "avl_tree.h"
#include <iostream>
#include<time.h>

using namespace std;

#define COUNT 200

int main(int argc, char *argv[])
{
  srand((unsigned int)time(0));
  AvlTree avl;
  int i;

  for(i=0; i < COUNT; i++){
    cout << endl <<i << endl;
    avl.insert(rand()%(COUNT*2), 1);
  }
  
  cout << avl.printHeight() << endl;

  for(i=0;i<(COUNT);i++){
    avl.erase(rand()%(COUNT*2));
  }

  cout << avl.printHeight() << endl;




}
