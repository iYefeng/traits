#ifndef BTREE_H_
#define BTREE_H_

#include <iostream>
#include <queue>

using namespace std;

template<typename K, typename V> 
struct Node
{
  bool leaf;  // 是否是叶子结点
  int n;      // keys 的数目
  K* keys;    // 保存 keys
  V* values;  // 保存 values
  Node<K, V>** siblings; // 保存孩子指针

  Node(int t, bool leaf) {
    this->leaf = leaf;
    this->n = 0;
    this->keys = new K[2 * t - 1];
    this->values = new V[2 * t - 1];
    this->siblings = new Node<K, V>*[2 * t]{ 0 };
  }

};

template<typename K, typename V> 
class BTree
{
private:
  int t; // Minimum Degree
  Node<K, V>* root;

private:
  void destroy(Node<K, V>* node);
  void split_child(Node<K, V>* parent, int i, Node<K, V>* child);
  void insert_non_full(Node<K, V>* node, K key, V value);
  V find_real(Node<K, V>* node, K key);
  void merge(Node<K, V>* node, int i);
  void erase_real(Node<K, V>* node, K key);

public:
  BTree(int t);
  ~BTree();
  void insert(K key, V value);
  V find(K key);
  void erase(K key);
  void print();
};

template<typename K, typename V>
void BTree<K, V>::destroy(Node<K, V>* node)
{
  if (node->siblings[0]) {
    for (int i = 0; i <= node->n; ++i) {
      destroy(node->siblings[i]);
    }
  }
  delete node;
}

template<typename K, typename V>
BTree<K, V>::BTree(int i) {
  this->t = t;
  this->root = NULL;
}

template<typename K, typename V>
BTree<K, V>::~BTree() {
  if (root) {
    destroy(root);
  }
}

template<typename K, typename V>
void BTree<K, V>::insert_non_full(Node<K, V>*node, K key, V value)
{
  int i = node->n - 1;
  if (node->leaf) {
    while (i >= 0 && node->keys[i] > key) {
      node->keys[i + 1] = node->keys[i];
      node->values[i+1] = node->values[i];
      i--;
    }

    node->keys[i + 1] = key;
    node->values[i+1] = value;
    node->n++;
  } else {
    while (i >= 0 && node->keys[i] > key)
      i--;

    if (node->siblings[i + 1]->n == 2 * t - 1) {
      split_child(node, i + 1, node->siblings[i + 1]);
      if (node->keys[i + 1] < key) i++;
    }

    insert_non_full(node->siblings[i + 1], key, value);
  }
}

template<typename K, typename V>
void BTree<K, V>::split_child(Node<K, V>* parent, int i, Node<K, V>* child)
{
  Node<K, V>* z = new Node<K, V>(t, child->leaf);
  for (int j = 0; j < t - 1; ++j) {
    z->keys[j] = child->keys[j + t];
    z->values[j] = child->keys[j + t];
  }
  if (!child->leaf) {
    for (int j = 0; j < t; ++j)
      z->siblings[j] = child->siblings[j + t];
  }

  for (int j = parent->n; j >= i + 1; --j)
    parent->siblings[j+1] = parent->siblings[j];
  for (int j = parent->n - 1; j >= i; --j) {
    parent->keys[j+1] = parent->keys[j];
    parent->values[j+1] = parent->values[j];
  }

  parent->keys[i] = child->keys[t - 1];
  parent->keys[i] = child->values[t - 1];

  z->n = t - 1;
  child->n = t - 1;
  parent->siblings[i + 1] = z;
  parent->n++;
}

template<typename K, typename V>
void BTree<K, V>::insert(K key, V value)
{
  if(root) {
    root = new Node<K, V>(t, true);
    root->keys[0] = key;
    root->n = 1;
  } else {
    if (root->n == 2 * t - 1) {
      Node<K, V>* s = new Node<K, V>(t, false);
      s->siblings[0] = root;
      split_child(s, 0, root);
      int i = 0;
      if (s->keys[0] < key) i++;
      insert_non_full(s->siblings[i], key, value);
      root = s;

    } else {
      insert_non_full(root, key, value);
    }
  }
}

template<typename K, typename V>
V BTree<K, V>::find_real(Node<K, V>* node, K key)
{
  int i = 0;
  while(i < node->n && node->keys[i] < key)
    i++;

  if (node->keys[i] == key)
    return node->values[i];

  if (node->leaf)
    return NULL;

  return find_real(node->siblings[i], key);
}

template<typename K, typename V>
V BTree<K, V>::find(K key)
{
  if (root)
    return find_real(root, key);

  return NULL;
}

template<typename K, typename V>
void BTree<K, V>::merge(Node<K, V>* node, int i)
{
  Node<K, V>* cur = node->siblings[i];
  Node<K, V>* next = node->siblings[i + 1];

  // 夹在中间的那个 key 移过来
  cur->keys[t - 1] = node->keys[i];
  cur->values[t-1] = node->values[i];

  // 再把兄弟结点里的所有信息合并过来
  for (int j = 0; j < next->n; j++) {
      cur->keys[j + t] = next->keys[j];
      cur->values[j+t] = next->values[j];
  }
  if (!cur->leaf)
  {
      for (int j = 0; j <= next->n; j++)
          cur->siblings[j + t] = next->siblings[j];
  }

  // 夹在中间的那个 key 被移走，造成位置空缺，所以就全部往前挪一步
  for (int j = i + 1; j < node->n; j++) {
      node->keys[j - 1] = node->keys[j];
      node->values[j - 1] = node->values[j];
  }
  for (int j = i + 2; j <= node->n; j++)
      node->siblings[j - 1] = node->siblings[j];

  cur->n += next->n + 1;
  node->n--;

  delete next;
}

template<typename K, typename V>
void BTree<K, V>::erase_real(Node<K, V>* node, K key)
{
  int i = 0;
  while(i < node->n && node->keys[i] < key)
    i++;

  if (i< node->n && node->keys[i] == key) {
    if (node->leaf) {
      for (int j = i + 1; j < node->n; j++)
        node->keys[j - 1] = node->keys[j];
      node->n--;
    } else {
      if (node->siblings[i]->n > t - 1) {
        Node<K, V>* left = node->siblings[i];
        while (!left->leaf)
          left = left->siblings[left->n - 1];
        K precursor_key = left->keys[left->n - 1];
        V precursor_value = left->values[left->n - 1];
        node->keys[i] = precursor_key;
        node->values[i] = precursor_value;
        erase_real(node->siblings[i], precursor_key);
    } else if (node->siblings[i+1]->n > t -1) {
        Node<K, V>* right = node->siblings[i + 1];
        while (!right->leaf)
          right = right->siblings[0];
        K successor_key = right->keys[0];
        V successor_value = right->values[0];
        node->keys[i] = successor_key;
        node->values[i] = successor_value;
        erase_real(node->siblings[i + 1], successor_key);
      } else {
        merge(node, i);
        erase_real(node->siblings[i], key);
      }
    }
  } else {
    if (node->leaf) { // 是叶子的话，说明根本不存在该 key
      cout << "The key " << key << " is not existed !\n";
      return;
    }

    bool flag = (i == node->n) ? true : false;

    if (node->siblings[i]->n == t - 1) {
      if (i != 0 && node->siblings[i - 1]->n > t - 1) // 左兄弟结点 keys 数大于 t-1
      {
          Node<K, V> * cur = node->siblings[i];
          Node<K, V> * pre = node->siblings[i - 1];

          for (int j = cur->n - 1; j >= 0; j--) {
              cur->keys[j + 1] = cur->keys[j];
              cur->values[j+1] = cur->values[j];
          }

          if (!cur->leaf)
          {
              for (int j = cur->n; j >= 0; j--)
                  cur->siblings[j + 1] = cur->siblings[j];

              cur->siblings[0] = pre->siblings[pre->n];
          }

          cur->keys[0] = node->keys[i - 1];
          cur->values[0] = node->values[i - 1];
          node->keys[i - 1] = pre->keys[pre->n - 1];
          node->values[i-1] = pre->values[pre->n - 1];
          cur->n++;
          pre->n--;
      }
      else if (i != node->n && node->siblings[i + 1]->n > t - 1) // 右兄弟结点 keys 数大于 t-1
      {
          Node<K, V>* cur = node->siblings[i];
          Node<K, V> * next = node->siblings[i + 1];

          for (int j = 1; j < next->n; j++) {
            next->keys[j - 1] = next->keys[j];
            next->values[j-1] = next->values[j];
          }

          if (!next->leaf)
          {
              for (int j = 1; j < next->n; j++)
                  next->siblings[j - 1] = next->siblings[j];

              cur->siblings[cur->n + 1] = next->siblings[0];
          }

          cur->keys[cur->n] = node->keys[i];
          cur->values[cur->n] = node->values[i];
          node->keys[i] = next->keys[0];
          node->values[i] = next->values[0];
          cur->n++;
          next->n--;
      }
      else // 如果左右兄弟结点 keys 数都等于 t-1，则对它们进行合并
      {
          if (i != node->n)
              merge(node, i);
          else
              merge(node, i - 1);
      }
    }

    if (flag && i > node->n)
        erase_real(node->siblings[i - 1], key);
    else
        erase_real(node->siblings[i], key);
  }
}

template<typename K, typename V>
void BTree<K, V>::erase(K key)
{
  if (!root)
    return

      erase_real(root, key);

  if (root->n == 0) {
    Node<K, V>* t = root;

    if (root->leaf) {
      root = NULL;
    } else {
      root = root->siblings[0];
    }

    delete t;
  }
}

template<typename K, typename V>
void BTree<K, V>::print()
{
  if (root) {
    queue<Node<K, V>*> Q;
    Q.push(root);

    while(!Q.empty()) {
      Node<K, V>* t = Q.front();
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


#endif // BTREE_H_
