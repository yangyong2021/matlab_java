import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用于测试字符串相关操作
 * @author Yangyong
 * 2023-05-29 19:45
 */

public class stringTest {
    public static void main(String[] args) {
        String str = "flags";
        String firstChar = str.substring(0,1).toUpperCase();

        String remainingChars  = str.substring(1);

        String result = firstChar + remainingChars;

        System.out.println(result);
    }
}
