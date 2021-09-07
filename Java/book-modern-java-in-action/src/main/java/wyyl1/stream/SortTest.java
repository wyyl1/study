package wyyl1.stream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @author aoe
 * @date 2021/4/15
 */
public class SortTest {

    public static void main(String[] args) {
        List<Task> list = Arrays.asList(new Task("完成注册登录", 1),
                new Task("完成注册登录", 1),
                new Task("更换头像", 0),
                new Task("编辑昵称", 0),
                new Task("观看视频5分钟", 2)
        );

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println(now.format(DateTimeFormatter.ISO_DATE));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(now));
    }

    private static class Task{
        String name;
        // 0：未完成，1：已完成，2：领取任务奖励，-1：失效
        int status;

        public Task(String name, int status) {
            this.name = name;
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
