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

AvlTree::AvlTree() :
  root_(NULL),
  maxHeight_(0)
{
}

AvlTree::~AvlTree()
{
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
        //判断是插入哪里导致高度规则被破坏
        if(key < root->left_->key_) { 
          printf("singleRightRotate");
          //如果插入到左子树中的左边则执行一次对应的单旋转
          root = singleRightRotate(root);
        }
        else { //否则执行对应的 双旋转
          printf("doubleRightRotate");
          root = doubleRightRotate(root);
        }
      }
    } else {               // should insert into right subtree
      root->right_ = _insert(key, value, root->right_);
      if (nodeHeight(root->right_) - nodeHeight(root->left_) >= 2) {
        // 
        if(key >= root->right_->key_) {
          printf("singleLeftRotate");
          root = singleLeftRotate(root);
        } else {
          printf("doubleLeftRotate");
          root = doubleLeftRotate(root);
        }

      }
    }
  }
  root->height_ = MAX(nodeHeight(root->left_), nodeHeight(root->right_)) +1;
  return root;
}

void AvlTree::insert(KEY_TYPE key, VALUE_TYPE value) 
{
  printf("insert %d\n", key);
  root_ = _insert(key, value, root_);
}

NODE AvlTree::singleRightRotate(NODE k2)
{
  NODE k1;
  k1 = k2->left_;
  k2->left_ = k1->right_;
  k1->right_ = k2;
  k2->height_ = MAX(nodeHeight(k2->left_), nodeHeight(k2->right_)) + 1;
  k1->height_ = MAX(nodeHeight(k1->left_), k2->height_) + 1;
  return k1;  /* New root */
}

NODE AvlTree::singleLeftRotate(NODE k1)
{
  NODE k2;
  k2 = k1->right_;
  k1->right_ = k2->left_;
  k2->left_ = k1;
  k1->height_ = MAX(nodeHeight(k1->left_), nodeHeight(k1->right_)) + 1;
  k2->height_ = MAX(nodeHeight(k2->right_), k1->height_) + 1;
  return k2;  /* New root */
}

NODE AvlTree::doubleRightRotate(NODE k3)
{
  /* Rotate between K1 and K2 */
  k3->left_ = singleLeftRotate(k3->left_);
  /* Rotate between K3 and K2 */
  return singleRightRotate(k3);
}

NODE AvlTree::doubleLeftRotate(NODE k1)
{
  /* Rotate between K3 and K2 */
  k1->right_ = singleRightRotate(k1->right_);
  /* Rotate between K1 and K2 */
  return singleLeftRotate(k1);
}

int AvlTree::printHeight(){
  maxHeight_ = 0;
  _print(root_);
  return maxHeight_;
}

void AvlTree::_print(NODE root)
{
  if (root != NULL) {
    if (nodeHeightDiff(root->left_, root->right_) >= 2) {
      printf("\n\nheight difference greater than 2\n\n");
      exit(1);
    }
    maxHeight_ = MAX(maxHeight_, root->height_);
    printf("\nkey:%d height: %d (left height: %d right height: %d ) ", 
        root->key_,
        nodeHeight(root),
        nodeHeight(root->left_),
        nodeHeight(root->right_)
        );
    _print(root->left_);

    _print(root->right_);
  }
}
