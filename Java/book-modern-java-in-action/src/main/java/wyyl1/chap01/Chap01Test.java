package wyyl1.chap01;

import java.io.File;

/**
 * @author aoe
 * @date 2020/12/8
 */
public class Chap01Test {

    public static void main(String[] args) {
        File[] hiddenFiles = new File(".").listFiles(File::isHidden);
    }
}
