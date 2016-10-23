package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class MinAbsolute {

    public static int getMinAbsolute(int[] array) {
        int n = array.length - 1;
        int begin = 0;
        int end = n;
        int mid = begin + (end - begin) / 2;
        int min = Integer.MAX_VALUE;
        int minVal;

        while(end > begin) {
            mid = begin + (end - begin) / 2;
            if (array[mid] == 0) {
                return 0;
            } else if (array[mid] > 0) {
                if (mid > begin && array[mid-1] > 0) end = mid - 1;
                else if (mid > begin && array[mid-1] == 0) return 0;
                else break;
            } else {
                if (mid < end && array[mid+1] < 0) begin = mid + 1;
                else if (mid < end && array[mid+1] == 0) return 0;
                else break;
            }
        }

        if (array[mid] > 0) {
            if (mid == 0 || Math.abs(array[mid]) < Math.abs(array[mid-1])) {
                return array[mid];
            } else {
                return array[mid-1];
            }
        } else {
            if (mid == n || Math.abs(array[mid]) < Math.abs(array[mid+1])) {
                return array[mid];
            } else {
                return array[mid+1];
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {-10, -5, -2, 1, 7, 15, 20};
        System.out.println(getMinAbsolute(arr));
        int[] arr1 = {2,4,6,8,27};
        System.out.println(getMinAbsolute(arr1));
        int[] arr2 = {-13,-9,-7,-3};
        System.out.println(getMinAbsolute(arr2));
    }
}
