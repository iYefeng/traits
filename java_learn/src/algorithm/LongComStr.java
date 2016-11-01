package algorithm;

/**
 * Created by yefeng on 16/10/30.
 */
public class LongComStr {

    public static void getLCString(String str1, String str2) {
        char[] cStr1, cStr2;
        int len1, len2, minLen;
        int[] c, max, maxIndex;

        if (str1.length() > str2.length()) {
            cStr1 = str1.toCharArray();
            cStr2 = str2.toCharArray();
        } else {
            cStr1 = str2.toCharArray();
            cStr2 = str1.toCharArray();
        }
        len1 = cStr1.length;
        len2 = cStr2.length;
        max = new int[len1];
        maxIndex = new int[len1];
        c = new int[len2];

        max[0] = -1;
        for(int i = 0; i < len2; ++i) {
            c[i] = 0;
        }

        for (int i = 0; i < len1; ++i) {
            for (int j = len2-1; j >= 0; --j) {
                if (cStr1[i] == cStr2[j]) {
                    if ((i == 0) || (j == 0))
                        c[j] = 1;
                    else
                        c[j] = c[j - 1] + 1;
                } else {
                    c[j] = 0;
                }
                if (c[j] > max[0]) {
                    max[0] = c[j];
                    maxIndex[0] = j;
                    for (int k = 1; k < len1; ++k) {
                        max[k] = 0;
                        maxIndex[k] = 0;
                    }
                } else if (c[j] == max[0]){
                    for (int k = 1; k < len1; ++k) {
                        if (max[k] == 0) {
                            max[k] = c[j];
                            maxIndex[k] = j;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < len1; ++i) {
            if (max[i] > 0) {
                System.out.println("第" + (i + 1) + "个公共子串:");
                for (int j = maxIndex[i] - max[i]+1; j <= maxIndex[i]; ++j)
                    System.out.print(cStr2[j]);
                System.out.println(" ");
            }
        }
    }

    public static void main(String[] args) {
        String str1 = "123456abcdddd567";
        String str2 = "234dddabc45678";
        getLCString(str1, str2);
    }
}
