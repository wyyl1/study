package wyyl1;

import modernjavainaction.chap11.Car;
import modernjavainaction.chap11.OptionalMain;
import modernjavainaction.chap11.Person;
import modernjavainaction.chap11.ReadPositiveIntParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author aoe
 * @date 2021/1/19
 */
public class Chap11Test {

    public static void main(String[] args) {
        OptionalMain om = new OptionalMain();
        Optional<Person> person = Optional.of(new Person());
//        om.getCarInsuranceName(person);

//        List<Person> persons = Arrays.asList(new Person());
//        om.getCarInsuranceNames(persons);

//        om.nullSafeFindCheapestInsurance(person, Optional.ofNullable(null));


        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-3");

        System.out.println(ReadPositiveIntParam.readDurationWithOptional(props, "da"));
    }
}
