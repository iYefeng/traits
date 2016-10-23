package algorithm;

/**
 * Created by yefeng on 16/10/23.
 */
public class MergeTwoSubArray {

    public static void findRightPlaceForMid(int[] array, int mid) {
        int tmp;

        for (int i = mid; i < array.length - 2; ++i) {
            if (array[i+1] < array[i]) {
                tmp = array[i];
                array[i] = array[i+1];
                array[i+1] = tmp;
            }
        }
    }
    public static void sort(int[] array, int mid) {
        int tmp;

        for (int i = 0; i < mid; ++i) {

            if (array[i] > array[mid]) {
                tmp = array[i];
                array[i] = array[mid];
                array[mid] = tmp;
                findRightPlaceForMid(array, mid);
            }

        }
    }

    // 1, 5, 6, 7, 9, 2, 4, 6, 8, 13 ,14
    // 1, 2, 6, 7, 9, 5 ,4, 6, 8, 13, 14
    // 1, 2, 6, 7, 9, 4, 5, 6, 8, 13, 14
    // 1, 2, 4, 7, 9, 6, 5, 6, 8, 13, 14
    // 1, 2, 4, 7, 9, 5, 6, 6, 8, 13, 14
    // 1, 2, 4, 5, 9, 7, 6, 6, 8, 13, 14
    // 1, 2, 4, 5, 9, 6, 7, 6, 8, 13, 14
    // 1, 2, 4, 5, 9, 6, 6, 7, 8, 13, 14
    // ...

    public static void main(String[] args) {
        int[] arr = {1,5,6,7,9,2,4,6,8,13,14};
        sort(arr, 5);
        for (int i = 0; i < arr.length; ++i) {
            System.out.println(arr[i]);
        }
    }
}
