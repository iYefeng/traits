/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-21 21:28
 * Last modified : 2016-12-21 21:28
 * Filename      : avl_tree.cc
 * Description   : 
 * *********************************************************/

#include "avl_tree.h"

TreeNode::TreeNode(KEY_TYPE key, VALUE_TYPE value) :
  key_(key),
  value_(value),
  left_(NULL),
  right_(NULL),
  height_(0)
{
}

AvlTree::AvlTree() 
{
  root_ = NULL;
}

NODE AvlTree::_insert(KEY_TYPE key, VALUE_TYPE value, NODE root)
{
  if (root == NULL) {
    NODE node = NODE(new TreeNode(key, value));
    return node;
  } else {
    if(key < root->key_) { // should insert into left subtree
      root->left_ = _insert(key, value, root->left_);
      if (nodeHeight(root->left_) - nodeHeight(root->right_) >= 2) {
        // 
        


      }
    } else {               // should insert into right subtree
      root->right_ = _insert(key, value, root->right_);
      if (nodeHeight(root->right_) - nodeHeight(root->left_) >= 2) {
        // 
        


      }
    }
  }
  root->height_ = MAX(nodeHeight(root->left_), nodeHeight(root->right_)) +1;
  return root;
}

void AvlTree::insert(KEY_TYPE key, VALUE_TYPE value) 
{
  root_ = _insert(key, value, root_);
}
