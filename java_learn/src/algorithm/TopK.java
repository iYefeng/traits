package algorithm;

/**
 * Created by yefeng on 16/10/22.
 * 查找最小的 k 个元素
 * 题目: 输入n个整数, 输出其中最小的k个。
 * 例如输入1,2,3,4,5,6,7,8 这8个数字,则最小的4个数字为1,2,3,4.
 */
public class TopK {

    public static class Heap {

        public Heap(int[] array) {
            this.array_ = array;
            this.num_ = array.length;
        }

        public void swap(int i, int j) {
            int tmp;
            tmp = array_[i];
            array_[i] = array_[j];
            array_[j] = tmp;
        }

        public void MaxHeapFixDown(int i, int n) {
            int j, temp;
            temp = array_[i];
            j = 2 * i + 1;
            while (j < n)
            {
                if (j + 1 < n && array_[j + 1] < array_[j]) //在左右孩子中找最小的
                    j++;
                if (array_[j] >= temp)
                    break;
                array_[i] = array_[j];     //把较小的子结点往上移动,替换它的父结点
                i = j;
                j = 2 * i + 1;
            }
            array_[i] = temp;
        }

        public void MakeMinHeap() {
            for (int i = num_ / 2 - 1; i >= 0; i--)
                MaxHeapFixDown(i, num_);
        }

        public Integer pop() {
            Integer ret = null;
            if (num_ <= 0) return ret;
            ret = array_[0];
            swap(0, --num_);
            MaxHeapFixDown(0, num_);
            return ret;
        }

        int[] array_;
        int num_;
    }


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
        // method 1
        //System.out.println(quickSort(arr, 0, arr.length-1, 4));

        // method 2
        Heap heap = new Heap(arr);
        heap.MakeMinHeap();
        for (int i = 0; i < 4; ++i) {
            System.out.println(heap.pop());
        }
    }



}
