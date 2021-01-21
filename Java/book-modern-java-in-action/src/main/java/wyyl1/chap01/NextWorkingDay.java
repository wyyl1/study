package wyyl1.chap01;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

/**
 * @author aoe
 * @date 2021/1/22
 */
public class NextWorkingDay implements TemporalAdjuster {
    @Override
    public Temporal adjustInto(Temporal temporal) {
        DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 读取当前日期
        int dayToAdd = 1; // 正常情况：增加一天
        if (dow == DayOfWeek.FRIDAY) {
            dayToAdd = 3; // 如果当天是周五，增加三天
        }
        if (dow == DayOfWeek.SATURDAY) {
            dayToAdd = 2; // 如果当天是周六，增加两天
        }
        return temporal.plus(dayToAdd, ChronoUnit.DAYS); // 增加恰当的天数后，返回修改的日期
    }
}
