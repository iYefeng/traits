package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class TopK {
    public static  int quickSort(int[] array, int low, int high, int k) {
        int i, j;
        int index;
        i = low;
        j = high;
        index = array[i];
        while(i < j) {
            while (i < j && array[j] >= index) j--;
            if (i < j) array[i++] = array[j];
            while (i < j && array[i] < index) i++;
            if (i < j) array[j--] = array[i];
        }
        array[i] = index;
        if (i == k-1) {
            return index;
        } else if (i > k-1){
            return quickSort(array, low, i-1, k);
        } else {
            return quickSort(array, i+1, high, k);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1,5,2,6,8,0,6};
        System.out.println(quickSort(arr, 0, arr.length-1, 4));
    }



}
