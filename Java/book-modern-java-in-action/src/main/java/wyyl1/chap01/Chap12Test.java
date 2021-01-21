package wyyl1.chap01;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

/**
 * @author aoe
 * @date 2021/1/21
 */
public class Chap12Test {

    public static void main(String[] args) {
//        useTemporalAdjuster();
//        nextWorkingDay();
        useDateFormatter();
    }

    private static void useDateFormatter(){
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18

        System.out.println(s1);
        System.out.println(s2);
    }


    private static void nextWorkingDay() {
        TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 读取当前日期
            int dayToAdd = 1; // 正常情况：增加一天
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3; // 如果当天是周五，增加三天
            }
            if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2; // 如果当天是周六，增加两天
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS); // 增加恰当的天数后，返回修改的日期
        });
        LocalDate date1 = LocalDate.of(2021, 1, 22); // 2021-01-22
        LocalDate date2 = date1.with(nextWorkingDay); // 2021-01-25

        System.out.println(date1);
        System.out.println(date2);
    }

    private static void useTemporalAdjuster(){
        LocalDate date1 = LocalDate.of(2014, 3, 18); // 2014-03-18
        LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY)); // 2014-03-23
        LocalDate date3 = date2.with(lastDayOfMonth()); // 2014-03-31

        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
    }

    private static void test3(){
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.plusWeeks(1); // 2017-09-28
        LocalDate date3 = date2.minusYears(6); // 2011-09-28
        LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS); // 2012-03-28

        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        System.out.println(date4);
    }

    private static void test2(){
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.withYear(2011); // 2011-09-21
        LocalDate date3 = date2.withDayOfMonth(25); // 2011-09-25
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2); // 2011-02-25

        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        System.out.println(date4);
    }

    private static void test1(){
        /*Period tenDays = Period.between(LocalDate.of(2017, 9, 11),
                LocalDate.of(2017, 9, 21));
        System.out.println(tenDays.getDays());*/

        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeDays = Duration.of(3, ChronoUnit.DAYS);
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period towYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }
}
