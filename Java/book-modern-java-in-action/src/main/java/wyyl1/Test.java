package wyyl1;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * @author aoe
 * @date 2021/4/12
 */
public class Test {

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("123");
        BigDecimal b = new BigDecimal("10");
        String c = a.divide(b).setScale(0, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();
        System.out.println(c);

        System.out.println(LocalDate.now().minusDays(1).toString());
    }
}
