package algorithm;

/**
 * Created by yefeng on 16/11/30.
 * 在一个字符串中找到第一个只出现一次的字符。如输入abaccdeff, 则输出b。
 */
public class FirstSingleWord {

    public static char findFirstSingleWord(String text) {
        int[] wordMap = new int[255];
        byte[] cs = text.getBytes();

        for (int i = 0; i < cs.length; ++i) {
            wordMap[cs[i]] ++;
        }

        for (int i = 0; i < cs.length; ++i) {
            if (wordMap[cs[i]] == 1) {
                return (char)cs[i];
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String str = "abaccdeff";

        System.out.println(findFirstSingleWord(str));
    }

}
