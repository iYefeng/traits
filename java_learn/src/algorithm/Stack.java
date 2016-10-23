package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */

public class Stack {
    Node head = new Node(0);
    int size_ = 0;

    public void push(int data) {
        Node tmp = new Node(data);
        tmp.next_ = head.next_;
        head.next_ = tmp;
        size_ ++;
    }

    public Node pop() {
        if (size_ > 0) {
            size_--;
            Node tmp = head.next_;
            head.next_ = tmp.next_;
            return tmp;
        } else {
            return null;
        }
    }

    public Node peek() {
        if (head.next_ != null) {
            return head.next_;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return size_ == 0;
    }


    public static void main(String[] args) {
        Stack ss = new Stack();
        ss.push(1);
        ss.push(2);
        ss.push(3);
        ss.push(4);
        ss.push(5);
        Node tmp = ss.pop();
        while (tmp != null) {
            System.out.println(tmp.data_);
            tmp = ss.pop();
        }

    }
}
