package algorithm;

/**
 * Created by yefeng on 16/11/23.
 */
public class LastKInLinkedList {

    public static int lastK(LinkedList root, int k) {
        Node tmp1, tmp2;
        int cnt = 0;
        if (root == null) {
            System.out.println("root is null");
            return 0;
        }
        tmp1 = root.head;
        tmp2 = root.head;
        while (tmp1.next_ != null) {
            tmp1 = tmp1.next_;
            cnt ++;
            if (cnt >= k) {
                tmp2 = tmp2.next_;
            }
        }
        if (cnt >= k) {
            return tmp2.data_;
        } else {
            System.out.println("list.length < k");
            return 0;
        }
    }


    public static void main(String[] args) {
        LinkedList ll = new LinkedList();
        ll.addNode(1);
        ll.addNode(2);
        ll.addNode(3);
        ll.addNode(4);
        ll.addNode(4);
        ll.addNode(5);
        ll.printList();
        System.out.println("--------");
        System.out.println(lastK(ll, 0));


    }
}
