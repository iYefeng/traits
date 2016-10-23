package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class ShiftK {
    public static void reverse(int[] array, int start, int end) {
        int tmp;
        while (start < end) {
            tmp = array[start];
            array[start] = array[end];
            array[end] = tmp;
            start ++;
            end --;
        }
    }

    public static void shiftK(int[] array, int k) {
        k = k % array.length;
        reverse(array, 0, k-1);
        reverse(array, k, array.length-1);
        reverse(array, 0, array.length-1);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8};
        shiftK(arr, 6);
        for (int i = 0; i < arr.length; ++i) {
            System.out.println(arr[i]);
        }
    }
}
