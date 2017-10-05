#include "btree.h"

template<typename T> void BTree<T>::destroy(Node<T>* node) 
{
  if (node->siblings[0]) {
    for (int i = 0; i <= node->n; ++i) {
      destroy(node->siblings[i]);
    }
  }
  delete node;
}

template<typename T> BTree<T>::BTree(int i) {
  this->t = t;
  this->root = NULL;
}

template<typename T> BTree<T>::~BTree() {
  if (root) { 
    destroy(root);
  }
}

template<typename T> T* BTree<T>::find(T key) 
{

}

template<typename T> void BTree<T>::print() 
{
  if (root) {
    queue<Node<T>*> Q;
    Q.push(root);

    while(!Q.empty()) {
      Node<T>* t = Q.front();
      Q.pop();
      for (int i = 0; i < t->n; ++i) {
        cout << t->keys[i] << " ";
      }
      cout << endl;
      
      if (!t->leaf) {
        for (int i = 0; i <= t->n; ++i) {
          Q.push(t->siblings[i]);
        }
      }
    }
    cout << endl;
  }
}
