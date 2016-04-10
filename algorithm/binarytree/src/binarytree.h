#ifndef __BINARY_TREE_H__
#define __BINARY_TREE_H__

// define data type for the key and value
typedef int keyType;
typedef int valueType;

// define binary search tree data structure
struct BinaryTreeNode{
  keyType key;
  valueType value;
  struct BinaryTreeNode* left;
  struct BinaryTreeNode* right;
};

// alias for the tree
typedef struct BinaryTreeNode bstree;

// alias for the tree node
typedef struct BinaryTreeNode bstnode;

// search BST by the given key
bstnode* search_by_key(bstree* tree, keyType key);

// insert a (key, value) to BST
int insert_key_value(bstree** tree, keyType key, valueType value);

//delete the value from the BST by key
int delete_by_key(bstree** tree, keyType key);

// destroy binary tree
int destroy_btree(bstree *tree);

//前序排列
void dlr_order(bstree *tree);

//中序排列
void ldr_order(bstree *tree);

#endif
