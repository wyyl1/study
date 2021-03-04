package wyyl1;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

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
//        useDateFormatter();
//        getWeekStart();
        zoneIdTest();
    }

    private static void zoneIdTest(){
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");

        LocalDate date = LocalDate.of(2021, 1, 27);
        ZonedDateTime zdt1 = date.atStartOfDay(shanghaiZone);
        // 2021-01-27T00:00+08:00[Asia/Shanghai]
        System.out.println(zdt1);

        LocalDateTime dateTime = LocalDateTime.of(2021, 1, 27, 18, 13, 45);
        ZonedDateTime zdt2 = dateTime.atZone(shanghaiZone);
        // 2021-01-27T18:13:45+08:00[Asia/Shanghai]
        System.out.println(zdt2);

        Instant instant = Instant.now();
        ZonedDateTime zdt3 = instant.atZone(shanghaiZone);
        // 2021-01-27T23:47:02.923793+08:00[Asia/Shanghai]
        System.out.println(zdt3);

        Long time = 2021012716L;
        System.out.println(time.toString().substring(8));


        long createTime = Long.parseLong("1612160235338");
        long selTime = Long.parseLong("1612160235378");
        long c = selTime - createTime;
        System.out.println("时差 " + c);

        List<String> cities = Arrays.asList("Milan",
                "London",
                "New York",
                "San Francisco");
        String citiesCommaSeparated = String.join(",", cities);
        System.out.println(citiesCommaSeparated);

        String d = "20210127";
        System.out.println(d.substring(0, 4));
        System.out.println(d.substring(4, 6));
        System.out.println(d.substring(6, 8));

        int[] nobleLevels = IntStream.rangeClosed(1, 7).toArray();
        System.out.println(Arrays.toString(nobleLevels));

        LocalDate today = LocalDate.now();
        System.out.println(today.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(today.format(DateTimeFormatter.ISO_DATE));

        long duration = 1612494659192L - 1612494659124L;
        System.out.println(duration);

        String s = "packageName: $packagePrefix.model.db";
        System.out.println(s.replaceAll(Matcher.quoteReplacement("$")  + "packagePrefix" + "", "sss"));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime tldt = LocalDateTime.parse("2021-02-18 01:56:45", df);
        System.out.println(tldt);
    }

    private static void useDateFormatter(){
        /*LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18

        System.out.println(s1);
        System.out.println(s2);

        LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(date1);
        System.out.println(date2);

        LocalDate today = LocalDate.now();
        System.out.println(today);
        DayOfWeek dow = today.getDayOfWeek();
        System.out.println(DayOfWeek.MONDAY.getValue());
        System.out.println(DayOfWeek.SUNDAY.getValue());

        System.out.println(dow.getValue());

//        9294409891907 1611542488078 1612060888079
//        9294409891907 1611553434001 1612071834001
        Instant instant = Instant.ofEpochMilli(1611553434001L);
        System.out.println(Date.from(Instant.ofEpochMilli(1611542488078L)));
        System.out.println(Date.from(Instant.ofEpochMilli(1611553434001L)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateILike = LocalDate.of(2021, 1, 27);
        String formattedDate = dateILike.format(formatter);
        LocalDate dateULike = LocalDate.parse("2021/01/27", formatter);

        System.out.println(formattedDate);
        System.out.println(dateULike);*/

        LocalDate date = LocalDate.of(2021, 1, 27);
        DateTimeFormatter chinaFormatter = DateTimeFormatter.ofPattern("yyyy MMMM d", Locale.CHINA);
        String formattedDate = date.format(chinaFormatter);
        System.out.println(formattedDate);
    }

    private static void getWeekStart(){
        // 2021-01-25
        LocalDate today = LocalDate.of(2021, 1, 26);
        DayOfWeek dow = today.getDayOfWeek();
        int todayValue = dow.getValue();
        int monday = DayOfWeek.MONDAY.getValue();
        int sunday = DayOfWeek.SUNDAY.getValue();

        int diff = todayValue - monday;
        if (diff == 0) {
            System.out.println("今天星期一");
            return;
        }

        LocalDate day = today.minusDays(diff);
        LocalDateTime ldt = LocalDateTime.of(day.getYear(), day.getMonth(), day.getDayOfMonth(),0 , 0, 0);
        System.out.println(ldt);
        System.out.println(Timestamp.valueOf(ldt));

        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().getId()));
        System.out.println(zdt.toInstant().toEpochMilli());
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
