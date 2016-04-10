#include <stdio.h>
#include "binarytree.h"

int main()
{
  bstree *bst = NULL;
  bstnode *node = NULL;
  insert_key_value(&bst, 4, 4);
  insert_key_value(&bst, 3, 3);
  insert_key_value(&bst, 15, 15);
  insert_key_value(&bst, 17, 17);
  insert_key_value(&bst, 0, 0);
  insert_key_value(&bst, 134, 134);
  
  printf("前序遍历\n");
  dlr_order(bst);
  printf("\n");
  printf("中序遍历\n");
  ldr_order(bst);
  printf("\n");

  node = search_by_key(bst, 17);
  printf("%d\n", node->value);

  int ret = delete_by_key(&bst, 134);
  printf("delete result %d\n", ret);

  ret = delete_by_key(&bst, 15);
  printf("delete result %d\n", ret);

  ret = delete_by_key(&bst, 0);
  printf("delete result %d\n", ret);
  
  printf("前序遍历\n");
  dlr_order(bst);
  printf("\n");
  printf("中序遍历\n");
  ldr_order(bst);
  printf("\n");
  
  insert_key_value(&bst, 10, 10);
  insert_key_value(&bst, 5, 5);
  
  printf("前序遍历\n");
  dlr_order(bst);
  printf("\n");
  printf("中序遍历\n");
  ldr_order(bst);
  printf("\n");
  
  destroy_btree(bst);
  return 0;
}
