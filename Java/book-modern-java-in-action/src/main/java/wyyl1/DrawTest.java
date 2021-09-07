package wyyl1;

import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author aoe
 * @date 2021/5/26
 */
public class DrawTest {

    @Test
    public void draw(){
        int size = 10;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                RandomGenerator random = new JDKRandomGenerator();
                int num = random.nextInt(2);
                System.out.print(num % 2 == 0 ? "." : " ");
            }
            System.out.println();
        }
    }

    @Test
    public void showDate(){
        LocalDate now = LocalDate.now();
        System.out.println(now.format(DateTimeFormatter.BASIC_ISO_DATE));
    }
}
