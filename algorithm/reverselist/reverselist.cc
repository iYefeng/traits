/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-10-18 21:07
 * Last modified : 2016-10-18 21:07
 * Filename      : reverselist.cc
 * Description   : 
 * *********************************************************/
#include <stdio.h>
#include <stdlib.h>

struct Node
{
  int data_;
  Node* next_;
};

Node* createList(Node* head)
{
  int x;
  char c;
  Node* q;
  while(1) {
    scanf("%d%c", &x, &c);
    Node* p = (Node*)malloc(sizeof(Node));
    p->data_ = x;
    p->next_ = NULL;
    if (head->next_ == NULL) {
      head->next_ = p;
    } else {
      q->next_ = p;
    }
    q = p;
    if (c == ';') {
      break;
    }
  }
  return head;
}

void printList(Node* head)
{
  Node* p;
  p = head->next_;
  while(p != NULL) {
    printf("%d ", p->data_);
    p = p->next_;
  }
  printf("\n");
}

Node* reverseList(Node* head)
{
  Node *q, *p, *r;
  if (head->next_ == NULL)
    return head;

  p = head->next_;
  while(p->next_ != NULL) {
    q = p->next_;
    p->next_ = q->next_;
    q->next_ = head->next_;
    head->next_ = q;
  }
  return head;

}

int main(int argc, char* argv[])
{
  Node* head = (Node*)malloc(sizeof(Node));
  head->next_ = NULL;
  head = createList(head);
  printList(head);

  head = reverseList(head);
  printList(head);
  
  free(head);
  return 0;
}
