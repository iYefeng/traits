package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class Queue {
    Node head = new Node(0);
    Node tail = head;
    int size_;

    public void push(int data) {
        Node tmp = new Node(data);
        tail.next_ = tmp;
        tail = tmp;
        size_ ++;
    }

    public Node pop() {
        Node tmp = head.next_;
        if (tmp != null) {
            size_ --;
            head.next_ = tmp.next_;
        }
        return tmp;
    }

    public static void main(String[] args) {
        Queue q = new Queue();
        q.push(1);
        q.push(2);
        q.push(3);
        q.push(4);
        Node tmp = q.pop();
        while(tmp != null) {
            System.out.println(tmp.data_);
            tmp = q.pop();
        }
    }
}
