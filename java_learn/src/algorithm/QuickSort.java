package algorithm;

/**
 * Created by yefeng on 16/10/22.
 */
public class QuickSort {

    public static void sort(int[] array, int low, int hight) {
        if (low >= hight) return;
        int i = low;
        int j = hight;

        int index = array[i];
        while(i < j) {
            while(i < j && array[j] >= index) j--;
            if (i < j) array[i++] = array[j];
            while(i < j && array[i] <= index) i++;
            if (i < j) array[j--] = array[i];
        }
        array[i] = index;
        sort(array, 0, i-1);
        sort(array, i+1, hight);

    }

    public static void quickSort(int[] array) {
        sort(array, 0, array.length-1);
    }

    public static void main(String[] args) {
        int[] arr = {5,4,9,8,7,3,6,0,1,3,2};
        quickSort(arr);
        for(int i = 0; i < arr.length; ++i) {
            System.out.println(arr[i]);
        }
    }
}
