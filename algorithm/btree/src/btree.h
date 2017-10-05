#ifndef BTREE_H_
#define BTREE_H_

#include <iostream>
#include <queue>

using namespace std;

template<typename T>
struct Node
{
  bool leaf;  // 是否是叶子结点
  int n;      // keys 的数目
  T* keys;    // 保存 keys
  Node<T>** siblings; // 保存孩子指针

  Node(int t, bool leaf) {
    this->leaf = leaf;
    this->n = 0;
    this->keys = new T[2 * t - 1];
    this->siblings = new Node<T>*[2 * t]{ 0 };
  }

};

template<typename T>
class BTree
{
private:
  int t; // Minimum Degree
  Node<T>* root;

private:
  void destroy(Node<T>* node);
  void split_child(Node<T>* parent, int i, Node<T>* child);
  void insert_non_full(Node<T>* node, T key);
  bool find_real(Node<T>* node, T key);
  void merge(Node<T>* node, int i);
  void erase_real(Node<T>* node, T key);

public:
  BTree(int t);
  ~BTree();
  void insert(T key);
  T* find(T key);
  void erase(T key);
  void print();
};

#endif // BTREE_H_
