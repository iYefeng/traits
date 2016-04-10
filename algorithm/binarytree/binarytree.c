#include "binarytree.h"
#include <stdlib.h>
#include <stdio.h>

bstnode* search_by_key(bstree* tree, keyType key)
{
  bstnode* node = tree;
  int found = 0;
  while(NULL != node) {
    if (key == node->key) {
      found = 1;
      break;
    }
    node = (key > node->key)? node->right : node->left;
  }
  return found? node : NULL;
}

int insert_key_value(bstree** tree, keyType key, valueType value)
{
  if (NULL == *tree) {
    *tree = (bstnode*) malloc(sizeof(bstnode));
    (*tree)->key = key;
    (*tree)->value = value;
    (*tree)->left = NULL;
    (*tree)->right = NULL;
    return 1;
  }

  int found = 0;
  bstnode* curr = *tree;
  bstnode* prev = NULL;
  while(NULL != curr) {
    if (key == curr->key) {
      found = 1;
      break;
    }
    prev = curr;
    curr = (key > curr->key) ? curr->right : curr->left;
  }
  if (!found && NULL == curr) {
    curr = (bstnode*) malloc(sizeof(bstnode));
    curr->key = key;
    curr->value = value;
    curr->left = NULL;
    curr->right = NULL;
    if (key > prev->key) 
      prev->right = curr;
    else
      prev->left = curr;
    return 1;
  }
  return 0;
}

int delete_by_key(bstree** tree, keyType key)
{
  // set a temporary root node to prevent delete the real root node
  bstnode* head = (bstnode*) malloc(sizeof(bstnode));
  head->left = *tree;

  bstnode *curr = *tree, *prev = head;
  bstnode *t1 = NULL, *t2 = NULL;
  int found = 0;
  while(NULL != curr) {
    if (key == curr->key) {
      found = 1;
      break;
    }
    prev = curr;
    curr = ((key > curr->key) ? curr->right : curr->left);
  }
  if (found) {
    // delete the node with the given key
    if (NULL == curr->left) {
      if (curr == prev->left) 
        prev->left = curr->right;
      else
        prev->right = curr->right;
      free(curr);
    } else if (NULL == curr->right) {
      if (curr == prev->left)
        prev->left = curr->left;
      else
        prev->right = curr->left;
    } else {
      t1 = curr->left;
      while(NULL != t1->right) {
        t2 = t1;
        t1 = t1->right;
      }
      curr->key = t1->key;
      curr->value = t1->value;
      if (NULL == t2) 
        curr->left = t1->left;
      else
        t2->right = t1->left;
      free(t1);
    }
  }
  *tree = head->left;
  free(head);

  return found;
}

// destroy binary tree
int destroy_btree(bstree *tree)
{
  if (NULL != tree->left)
    destroy_btree(tree->left);
  if (NULL != tree->right)
    destroy_btree(tree->right);
  free(tree);
  return 0;  
}

//前序排列
void dlr_order(bstree *tree)
{
  printf("%d ", tree->key);
  if (NULL != tree->left)
    dlr_order(tree->left);
  if (NULL != tree->right)
    dlr_order(tree->right);
}

//中序排列
void ldr_order(bstree *tree)
{ 
  if (NULL != tree->left)
    ldr_order(tree->left);
  printf("%d ", tree->key);
  if (NULL != tree->right)
    ldr_order(tree->right);
}
