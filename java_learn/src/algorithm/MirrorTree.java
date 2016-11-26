package algorithm;

/**
 * Created by yefeng on 16/11/26.
 * 题目:输入一颗二元查找树,将该树转换为它的镜像, 即在转换后的二元查找树中,左子树的结点都大于右子树的结点。 用递归和循环两种方法完成树的镜像转换。
 * 例如输入:
 *  8
 *  /\
 * 6 10
 * /\ /\
 * 5 7 9 11
 * 输出:
 * 8
 * /\
 * 10 6
 * /\ /\
 * 11 9 7 5
 */
public class MirrorTree {

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

    static Node mirrorRoot = null;

    public static void mirrorBuilder(int value, int flag) {
        if (mirrorRoot == null) {
            mirrorRoot = new Node(value, null, null);
            return;
        }
        Node tmp = mirrorRoot;
        while (true) {
            if (tmp.val_ > value) {
                if (tmp.right_ == null) {
                    tmp.right_ = new Node(value, null, null);
                    return;
                } else {
                    tmp = tmp.right_;
                }
            } else {
                if (tmp.left_ == null) {
                    tmp.left_ = new Node(value, null, null);
                    return;
                } else {
                    tmp = tmp.left_;
                }
            }
        }

    }

    public static void mirror(Node root, int flag) {
        if (root == null) return;
        int value = root.val_;
        mirrorBuilder(value, flag);
        mirror(root.left_, flag);
        mirror(root.right_, flag);
    }

    public static void printTree(Node root, int level) {
        if (root != null) {
            System.out.println(root.val_ +"@"+ level);
            printTree(root.left_, level+1);
            printTree(root.right_, level+1);
        }
    }

    public static void swap(Node root){
        Node tmp;
        tmp = root.left_;
        root.left_ = root.right_;
        root.right_ = tmp;
    }

    public static void mirror2(Node root) {
        if (root == null) return;
        swap(root);
        mirror2(root.left_);
        mirror2(root.right_);
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

        mirror(n7, 0);
        System.out.println("-----------");

        printTree(mirrorRoot, 0);

        System.out.println("-----------");

        mirror2(n7);
        printTree(n7, 0);

    }
}
