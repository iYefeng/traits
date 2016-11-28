package algorithm;

/**
 * Created by yefeng on 16/11/28.
 * 输入一颗二元树,从上往下按层打印树的每个结点,同一层中按照从左往右的顺序打印。
 * 例如输入
 *     8
 *    / \
 *   6  10
 *  /\  /\
 * 5 7 9 11
 * 输出 8 6 10 5 7 9 11。
 */
public class PrintTree {

    public static class Node {
        public Node(int value, Node left, Node right) {
            this.val_ = value;
            this.left_ = left;
            this.right_ = right;
        }
        int val_;
        Node left_;
        Node right_;
    }

    public static void printTree(Node root, int level) {
        if (root != null) {
            System.out.println(root.val_ +"@"+ level);
            printTree(root.left_, level+1);
            printTree(root.right_, level+1);
        }
    }

    private static java.util.Queue<Node> queue = new java.util.LinkedList<Node>();

    public static void printTree2(Node root) {
        queue.add(root);
        Node tmp = null;
        while (!queue.isEmpty()) {
            tmp = queue.poll();
            if (tmp.left_ != null) queue.add(tmp.left_);
            if (tmp.right_ != null) queue.add(tmp.right_);
            System.out.println(tmp.val_);
        }
    }

    public static void main(String[] args) {
        Node n1 = new Node(5, null, null);
        Node n2 = new Node(7, null, null);
        Node n3 = new Node(9, null, null);
        Node n4 = new Node(11, null, null);

        Node n5 = new Node(6, n1, n2);
        Node n6 = new Node(10, n3, n4);

        Node n7 = new Node(8, n5, n6);

        printTree(n7, 0);

        System.out.println("----------");

        printTree2(n7);

    }
}
