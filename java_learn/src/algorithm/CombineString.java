package algorithm;

/**
 * Created by yefeng on 16/10/23.
 */
public class CombineString {

    public static void combine(char[] cArray, int begin, int len, StringBuffer sb) {

        StringBuffer sb2 = new StringBuffer();

        if (len == 0) {
            System.out.println(sb.toString() + " ");
        }

        if (begin == cArray.length) {
            return;
        }


        combine(cArray, begin+1, len-1, sb);
        sb2.append(sb);
        sb2.append(cArray[begin]);
        combine(cArray, begin+1, len-1, sb2);
    }

    public static void main(String[] args) {
        String str = "abc";
        StringBuffer sb = new StringBuffer();
        combine(str.toCharArray(), 0, str.length(), sb);

    }
}
