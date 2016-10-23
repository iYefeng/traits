package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class MaxGap {

    public static int getMax(int[] array) {
        int n = array.length - 1;
        int maxSell = array[n];
        int income = 0;
        int maxIncome = 0;
        for (int i = n - 1 ; i >= 0; --i) {
            income = maxSell - array[i];
            if (income > maxIncome) maxIncome = income;
            if (array[i] > maxSell) maxSell = array[i];
        }

        return maxIncome;
    }

    public static void main(String[] args) {
        int[] arr = {1,4,17,3,2,9};
        System.out.println(getMax(arr));
        int[] arr1 = {9,2,3,17,4,1};
        System.out.println(getMax(arr1));
    }
}
