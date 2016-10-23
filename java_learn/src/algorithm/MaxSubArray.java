package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class MaxSubArray {

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int maxSubArray(int[] array) {
        int nEnd = array[0];
        int nAll = array[0];
        for (int i = 0; i < array.length; ++i) {
            nEnd = max(nEnd + array[i], array[i]);
            nAll = max(nEnd, nAll);
        }
        return nAll;
    }

    public static void main(String[] args) {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};
        System.out.println(maxSubArray(arr));
    }
}
