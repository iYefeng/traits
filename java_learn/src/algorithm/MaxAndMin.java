package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class MaxAndMin {
    public static int[] getMaxAndMin(int[] array) {
        int[] maxmin = new int[2];

        if (array.length == 0) {
            maxmin[0] = 0;
            maxmin[1] = 0;
            return maxmin;
        }

        maxmin[0] = array[0];
        maxmin[1] = array[0];

        for (int i = 1; i < array.length; ++i) {
            if (array[i] > maxmin[0]) maxmin[0] = array[i];
            if (array[i] < maxmin[1]) maxmin[1] = array[i];
        }

        return maxmin;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,52,3,-7,2,9,10};
        int[] ret = getMaxAndMin(arr);
        System.out.println(ret[0] + "," + ret[1]);
    }


}
