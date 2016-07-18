import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YeFeng on 2016/7/19.
 */
public class Test {

    private static Pattern depReg = Pattern.compile("(day|hour|minute|second)\\((0|-\\d+)\\)((-|\\+)\\d+)*,\\s*(\\d+)");

    public static void main(String[] args) {
        Matcher mat = depReg.matcher("day(-3), 12");

        if (mat.find()) {
            System.out.println(mat.group(1));
            System.out.println(mat.group(2));
            System.out.println(mat.group(3));
            System.out.println(mat.group(5));
        }
    }
}
