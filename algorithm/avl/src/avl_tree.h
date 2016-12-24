/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-21 21:27
 * Last modified : 2016-12-21 21:27
 * Filename      : avl_tree.h
 * Description   : 
 * *********************************************************/

#ifndef AVL_TREE_H_
#define AVL_TREE_H_

#include <memory>
#include <stdio.h>

using namespace std;

typedef int KEY_TYPE;
typedef int VALUE_TYPE;
#define MAX(a,b)    ((a)>(b)?(a):(b))
#define ABS(x) (((x)>0)?(x):(-(x)))

struct TreeNode;
typedef shared_ptr<TreeNode> NODE;

struct TreeNode
{
  TreeNode(KEY_TYPE key, VALUE_TYPE value);
  ~TreeNode(){}
  KEY_TYPE key_;
  VALUE_TYPE value_;
  NODE left_;
  NODE right_;
  int height_;
};

class AvlTree
{
public:
  AvlTree();
  ~AvlTree();
  void insert(KEY_TYPE key, VALUE_TYPE value);
  NODE find(KEY_TYPE key);
  void erase(KEY_TYPE key);
  int printHeight();
  bool isEmpty()
  {
    return root_ == NULL;
  }

private:
  int nodeHeight(NODE node) 
  {
    if (node == NULL) return -1;
    else return node->height_;
  }

  int nodeHeightDiff(NODE left, NODE right) 
  {
    int hd = nodeHeight(left) - nodeHeight(right);
    return ABS(hd);
  }

  NODE singleLeftRotate(NODE k2);
  NODE singleRightRotate(NODE k1); 
  NODE doubleLeftRotate(NODE k3);
  NODE doubleRightRotate(NODE k1);

  NODE _insert(KEY_TYPE key, VALUE_TYPE value, NODE root);
  void _print(NODE root);

  NODE deleteFixup(NODE node);
  NODE deleteHaveRightChild(NODE node);
  NODE deleteHaveLeftChild(NODE node);
  NODE deleteHaveTwoChild(NODE node, NODE trueNode);
  NODE _delete(NODE root);
  NODE _deleteKey(KEY_TYPE key, NODE root);

  NODE root_;
  int maxHeight_;
};



#endif // AVL_TREE_H_
