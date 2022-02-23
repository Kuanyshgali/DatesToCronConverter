import com.aserbekov.kuanyshgali.Converter;
import com.digdes.school.DatesToCronConvertException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Converter convert = new Converter();
        List<String> dates = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("input"));
            while (in.hasNext()) {
                dates.add(in.nextLine().replaceAll("\"", ""));
            }
            convert.convert(dates);
        } catch (FileNotFoundException | DatesToCronConvertException e) {
            e.printStackTrace();
        }
    }
}
