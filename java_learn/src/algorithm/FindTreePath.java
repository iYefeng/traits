package algorithm;

/**
 * Created by yefeng on 16/11/19.
 * 题目:输入一个整数和一棵二元树。 从树的根结点开始往下访问一直到叶结点所经过的所有结点形成一条路径。 打印出和与输入整数相等的所有路径。
 * 例如输入整数22和如下二元树
 *    10
 *    /\
 *   5 12
 *  /\
 * 4 7
 * 则打印出两条路径:10, 12 和10, 5, 7。
 */
public class FindTreePath {
    static public class BTNode {

        public BTNode(int value, BTNode left, BTNode right) {
            this.val_ = value;
            this.left_ = left;
            this.right_ = right;
        }

        int val_;
        BTNode left_ = null;
        BTNode right_ = null;
    }

    public static void printPath(int[] path, int level) {
        for (int i = 0; i < level; ++i) {
            System.out.print(path[i] + " ");
        }
        System.out.println();
    }

    public static void findPath(BTNode root, int sum, int[] path, int level) {
        path[level++] = root.val_;
        sum -= root.val_;
        if (root.left_ == null && root.right_ == null) {
            if (sum == 0) {
                printPath(path, level);
            }
        } else if (sum > 0){
            if (root.left_ != null) findPath(root.left_, sum, path, level);
            if (root.right_ != null) findPath(root.right_, sum, path, level);
        }
    }


    public static void main(String[] args) {
        BTNode n1 = new BTNode(4, null, null);
        BTNode n2 = new BTNode(7, null, null);
        BTNode n3 = new BTNode(5, n1, n2);
        BTNode n4 = new BTNode(12, null, null);
        BTNode n5 = new BTNode(10, n3, n4);

        int[] path = new int[10];
        findPath(n5, 19, path, 0);
    }
}
