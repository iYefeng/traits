#include <stdio.h>
#include <string.h>
#include <locale.h>
#include <stdlib.h>
#include "trie.h"


int main(int argc, char *argv[])
{
  trie t;
  t.printTrie(t.root);

  t.insert("你好");
  t.printTrie(t.root);
  printf("\n");
  printf("%ld\n", t.sizeAll(t.root));
  printf("%ld\n", t.sizeNoneRedundant(t.root));
  t.erase("你好");
  t.printTrie(t.root);
  printf("\n");
  printf("%ld\n", t.sizeAll(t.root));
  printf("%ld\n", t.sizeNoneRedundant(t.root));
  
  
  
  t.insert("你好");
  t.insert("你好吗");
  t.insert("你好嗨");
  t.insert("你好嗨");
  t.insert("你好坏");
  t.insert("你好坏");
  t.insert("你");
  
  printf("%ld\n", t.sizeAll(t.root));
  printf("%ld\n", t.sizeNoneRedundant(t.root));

  t.printTrie(t.root);
  printf("\n");
  
  t.erase("你好");
  t.erase("你好");
  t.erase("你好");
  t.erase("你好");
  t.erase("你");
  t.erase("你");
  t.erase("你好坏");
  t.erase("你好坏");
  t.erase("你好吗");
  t.erase("你好嗨");
  t.erase("你好嗨");
  
  
  printf("%ld\n", t.sizeAll(t.root));
  printf("%ld\n", t.sizeNoneRedundant(t.root));
  t.printTrie(t.root);
  printf("\n");
  return 0;
}
