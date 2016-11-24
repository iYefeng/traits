package algorithm;

/**
 * Created by yefeng on 16/11/24.
 * 题目:输入一个已经按升序排序过的数组和一个数字, 在数组中查找两个数,使得它们的和正好是输入的那个数字。
 * 要求时间复杂度是 O(n)。如果有多对数字的和等于输入的数字,输出任意一对即可.
 * 例如输入数组1、2、4、7、11、15 和数字15。由于4+11=15,因此输出4 和11。
 */
public class CheckArraySum {


    public static void checkArray(int[] array, int k) {
        int i = 0, j = array.length-1;
        while (i < j) {
            if (array[i] + array[j] > k) {
                j--;
            } else if (array[i] + array[j] < k) {
                i++;
            } else {
                System.out.println(array[i] +"--"+ array[j]);
                break;
            }
        }
    }


    public static void main(String[] args) {
        int[] arr = {1,2,4,7,11,15};
        checkArray(arr, 17);
    }
}
