import org.joda.time.LocalDate;

/**
 * Author: Alex Cheng 1/27/2021 3:12 PM
 */


public class TestClass {

    public static void main(String[] args) {
        String date = "2020-09-29";
        LocalDate localDate = LocalDate.parse(date);
        System.out.println(localDate);

        String date2 = localDate.toString("yyyy-MM-dd");
        System.out.println(date2);

        String date3 = localDate.toString("MM/dd/yyyy");
        System.out.println(date3);
    }


}
