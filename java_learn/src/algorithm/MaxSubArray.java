package algorithm;

/**
 * Created by yefeng on 16/10/22.
 * 求子数组的最大和
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

    public static int maxSubArray2(int[] array) {
        int maxSum = Integer.MIN_VALUE;
        int begin = 0, end = 0;
        int nStart = 0;
        int nSum = 0;
        for (int i = 0; i < array.length; ++i) {
            if (nSum < 0) {
                nSum = array[i];
                nStart = i;
            } else {
                nSum += array[i];
            }
            if (nSum > maxSum) {
                maxSum = nSum;
                begin = nStart;
                end = i;
            }
        }

        System.out.println(String.format("start=%d, end=%d", begin, end));
        return maxSum;

    }

    public static void main(String[] args) {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};
        System.out.println(maxSubArray(arr));
    }
}
