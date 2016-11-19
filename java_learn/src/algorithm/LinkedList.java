package algorithm;

import java.util.Hashtable;

/**
 * Created by yefeng on 16/10/22.
 */

class Node{
    public Node(int data) {
        data_ = data;
    }
    Node next_ = null;
    int data_;
}

public class LinkedList {



    Node head = new Node(0);
    Node tail = head;

    public void addNode(int val) {
        Node tmp = new Node(val);
        tail.next_ = tmp;
        tail = tmp;
    }

    public void reverseList() {
        Node p, q;
        if (head.next_ == null) {
            System.out.println("List is null");
            return;
        }

        p = head.next_;
        while (p.next_ != null) {
            q = p.next_;
            p.next_ = q.next_;
            q.next_ = head.next_;
            head.next_ = q;
        }
    }

    public void deleteDuplecate() {
        Hashtable<Integer, Integer> table = new Hashtable<Integer, Integer>();
        Node tmp = head.next_;
        Node pre = tmp;
        while(tmp != null) {
            if (table.containsKey(tmp.data_)) {
                pre.next_ = tmp.next_;
            } else {
                table.put(tmp.data_, 1);
                pre = tmp;
            }
            tmp = tmp.next_;
        }
    }

    public int length() {
        int length = 0;
        Node tmp = head.next_;
        while(tmp != null) {
            length ++;
            tmp = tmp.next_;
        }
        return length;
    }

    public void printList() {
        Node tmp = head.next_;
        while(tmp != null) {
            System.out.println(tmp.data_);
            tmp = tmp.next_;
        }
    }

    public Node searchNode(int pos) {
        Node tmp = head.next_;
        if (pos < 0) {
            return null;
        }
        if (tmp == null) {
            return null;
        }
        while (pos-- > 0 && tmp != null) {
            tmp = tmp.next_;
        }
        return tmp;
    }

    public static void main(String[] args)
    {
        LinkedList ll = new LinkedList();
        ll.addNode(1);
        ll.addNode(2);
        ll.addNode(3);
        ll.addNode(4);
        ll.addNode(4);
        ll.addNode(5);
        ll.printList();
        ll.reverseList();
        ll.printList();
        System.out.println("List length: " + ll.length());
        ll.deleteDuplecate();
        ll.printList();
        Node tmp = ll.searchNode(2);
        if (tmp != null)
            System.out.println(tmp.data_);

    }
}


