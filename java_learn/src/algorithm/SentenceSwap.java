package algorithm;

/**
 * Created by yefeng on 16/10/23.
 */
public class SentenceSwap {

    public static void swap(char[] cArray, int front, int end) {
        char tmp;
        while (front < end) {
            tmp = cArray[front];
            cArray[front] = cArray[end];
            cArray[end] = tmp;
            front ++;
            end --;
        }
    }

    public static String swapWords(String s) {
        char[] cArray = s.toCharArray();
        swap(cArray, 0, cArray.length-1);
        int begin = 0;
        for (int i = 0; i < cArray.length; ++i) {
            if (cArray[i] == ' ') {
                swap(cArray, begin, i-1);
                begin = i + 1;
            }
        }
        swap(cArray, begin, cArray.length-1);
        return new String(cArray);
    }

    public static void main(String[] args) {
        String str = "how are you";
        System.out.println(swapWords(str));
    }
}
