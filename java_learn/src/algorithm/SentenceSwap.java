package algorithm;

/**
 * Created by yefeng on 16/10/23.
 * 转句子中单词的顺序。 题目:输入一个英文句子,翻转句子中单词的顺序,但单词内字符的顺序不变
 * 句子中单词以空格符隔开。为简单起见,标点符号和普通字母一样处理
 * 例如输入“I am a student.”,则输出“student. a am I”。
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
