package algorithm;

/**
 * Created by yefeng on 16/11/21.
 * 判断整数序列是不是二元查找树的后序遍历结果
 * 题目:输入一个整数数组,判断该数组是不是某二元查找树的后序遍历的结果。
 * 如果是返回 true,否则返回 false。
 * 例如输入5、7、6、9、11、10、8,由于这一整数序列是如下树的后序遍历结果:
 *    8
 *   /\
 *  6 10
 *  /\/\
 * 5 7 9 11
 * 因此返回 true。
 * 如果输入7、4、6、5,没有哪棵树的后序遍历的结果是这个序列,因此返回 false。
 */
public class PostOrderJudge {

    public static boolean isPostOrder(int[] array, int begin, int end) {
        boolean ret1, ret2;
        if (begin==end) return true;
        int i = begin;
        while(array[i] < array[end]) {
            i++;
        }
        while(array[i] > array[end]) {
            i++;
        }

        if (i != end) return false;

        if (!isPostOrder(array, begin, i - 1)) return false;
        return isPostOrder(array, i, end);
    }

    public static void main(String[] args) {
        int[] arr = {5, 7, 6, 9, 11, 10, 8};
        System.out.println(isPostOrder(arr, 0, arr.length-1));

        int[] arr1 = {7,4,6,5};
        System.out.println(isPostOrder(arr1, 0, arr1.length-1));

    }
}
