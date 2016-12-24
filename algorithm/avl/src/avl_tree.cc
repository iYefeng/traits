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

void AvlTree::erase(KEY_TYPE key)
{
  if (isEmpty()) {
    printf("the tree is empty\n");
    return;
  }
  root_ = _deleteKey(key, root_);
}

NODE AvlTree::_deleteKey(KEY_TYPE key, NODE root)
{
  if (root != NULL && root->key_ == key) {
    root = _delete(root);
    return root;
  } else if (root != NULL) {
    if(key < root->key_){         //向左子树中寻找
      root->left_ = _deleteKey(key, root->left_);
    }else{                        //向右子树中寻找
      root->right_ = _deleteKey(key, root->right_);
    }
    // 检查高度差，并修复
    if (nodeHeightDiff(root->left_, root->right_) >= 2) {
      root = deleteFixup(root);
    } 
    root->height_ = MAX(nodeHeight(root->left_), nodeHeight(root->right_)) + 1;      // 更新高度
    return root;
  }
  //递归寻找到树的最低层，没有找到要删除的
  printf(":can't find key:%d\n", key); 
  return NULL;
}

NODE AvlTree::_delete(NODE root)
{
  if(root->left_ == NULL){  //只有右儿子(或都为空)
    root = deleteHaveRightChild(root);
  }else if(root->left_ != NULL && root->right_ == NULL){    //只有左儿子
    root = deleteHaveLeftChild(root);
  }else if(root->left_ != NULL && root->right_ != NULL){        //两个儿子，那么删除的实际上是右子树中的最小元素，别忘了要交换关键字
    
    //所以传入了当前节点root指针，好在删除的时候和其交换关键字
    root->right_ = deleteHaveTwoChild(root->right_, root);
  }
  
  if(root != NULL) {
    root->height_ = MAX(nodeHeight(root->left_), nodeHeight(root->right_)) + 1;
    if (nodeHeightDiff(root->left_, root->right_) >= 2) {
      root = deleteFixup(root);
      root->height_ = MAX(nodeHeight(root->left_), nodeHeight(root->right_)) + 1;
    }
  }
  return root;
}

inline NODE AvlTree::deleteHaveRightChild(NODE node)
{
  return node->right_;
}

inline NODE AvlTree::deleteHaveLeftChild(NODE node)
{
  return node->left_;
}

NODE AvlTree::deleteHaveTwoChild(NODE node, NODE trueNode)
{
  if (node->left_ != NULL) {
    node->left_ = deleteHaveTwoChild(node->left_, trueNode);
    if (nodeHeightDiff(node->left_, node->right_) >= 2) {
      // 修正可能出现的高度规则破坏
      node= deleteFixup(node);
    }
    node->height_ = MAX(nodeHeight(node->left_), nodeHeight(node->right_)) + 1;
    return node;
  }
  trueNode->key_ = node->key_;
  trueNode->value_ = node->value_;
  return node->right_;   //别忘了返回节点
}

NODE AvlTree::deleteFixup(NODE node)
{
  if(nodeHeight(node->left_) > nodeHeight(node->right_)){
    if(nodeHeight(node->left_->left_) >= nodeHeight(node->left_->right_)) {
      node = singleRightRotate(node); 
    } else {
      node = doubleRightRotate(node);
    }
  } else {
    if (nodeHeight(node->right_->right_) >= nodeHeight(node->right_->left_)) {
      node = singleLeftRotate(node);
    } else {
      node = doubleLeftRotate(node);
    }
  }
  return node;
}
