package com.aserbekov.kuanyshgali;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Converter implements DatesToCronConverter {
    @Override
    public String convert(List<String> dates) throws DatesToCronConvertException {
        StringBuilder result = new StringBuilder();
        LinkedList<String> copyDates = new LinkedList<>(dates);
        ArrayList<ArrayList<Integer>> parts = partition(dates);

        for (int i = 0; i < parts.get(0).size(); i++) {
            int delta = 0;
            boolean flagEqual = true;
            boolean flagAlter = true;
            boolean flagAlter1 = true;
            boolean flagSeq = true;
            boolean flagRepeat = true;
            LinkedList<Integer> lst = new LinkedList<>();
            int counter = 0;

            for (int j = 0; j < parts.size(); j++) {
                if (j < parts.size() - 1)
                    if (parts.get(j).get(i) != parts.get(j + 1).get(i)) flagEqual = false;
                if (j > 0 && j < parts.size() - 1)
                    if (delta != Math.abs(parts.get(j).get(i) - parts.get(j + 1).get(i)))
                        flagAlter = false;
                if (j < parts.size() - 1)
                    delta = Math.abs(parts.get(j).get(i) - parts.get(j + 1).get(i));
                if (!flagAlter1) {
                    if (lst.get(counter++) != parts.get(j).get(i)) flagRepeat = false;
                    if (counter == lst.size()) {
                        counter = 0;
                    }
                }
                if (j < parts.size() - 1) {
                    String date = copyDates.get(j);
                    String date1 = copyDates.get(j + 1);
                    DateFormat oldDateFormat = new SimpleDateFormat(DATE_FORMAT);
                    try {
                        Date tmp = oldDateFormat.parse(date);
                        Date tmp1 = oldDateFormat.parse(date1);
                        if (tmp.getTime() > tmp1.getTime() && flagSeq)
                            flagSeq = false;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (parts.get(j).get(i) <= parts.get(j + 1).get(i) && flagAlter1) {
                        lst.add(parts.get(j).get(i));
                    } else if (flagAlter1) {
                        flagAlter1 = false;
                        lst.add(parts.get(j).get(i));
                    }
                }
            }
            if (flagEqual) {
                int tmp = parts.get(0).get(i);
                if (i == parts.get(0).size() - 1)
                    result.append(DayFromInt(tmp));
                else
                    result.append(tmp);
                result.append(" ");
            } else if (flagAlter) {
                int tmp = parts.get(0).get(i);
                if (i == parts.get(0).size() - 1)
                    result.append(DayFromInt(tmp));
                else
                    result.append(tmp);
                result.append("/");
                result.append(delta);
                result.append(" ");
            } else if (flagRepeat && !flagAlter1) {
                int tmp = lst.getFirst();
                if (i == parts.get(0).size() - 1)
                    result.append(DayFromInt(tmp));
                else
                    result.append(tmp);
                result.append("-");
                int tmp1 = lst.getLast();
                if (i == parts.get(0).size() - 1)
                    result.append(DayFromInt(tmp1));
                else
                    result.append(tmp1);
                result.append(" ");
            } else if (flagSeq) {
                result.append("* ");
            } else throw new DatesToCronConvertException();
        }

        System.out.println(result);
        return result.toString();
    }

    private ArrayList<ArrayList<Integer>> partition(List<String> dates) throws DatesToCronConvertException {
        for (int i = 0; i < dates.size(); i++) {
            dates.set(i, changeFormat(dates.get(i)));
        }

        ArrayList<ArrayList<Integer>> parts = new ArrayList<>();
        for (String i : dates) {
            ArrayList<Integer> row = new ArrayList<>();
            int startIndex = 0;
            int endIndex = -1;
            while (startIndex != i.length() - 2) {
                endIndex = i.indexOf(" ", startIndex) + 1;
                row.add(Integer.parseInt(i.substring(startIndex, endIndex).trim()));
                startIndex = endIndex;
            }
            row.add(Integer.parseInt(i.substring(startIndex)));

            parts.add(row);
        }

        return parts;
    }

    private String changeFormat(String date) throws DatesToCronConvertException {
        isValid(date, DATE_FORMAT);

        DateFormat oldDateFormat = new SimpleDateFormat(DATE_FORMAT);
        DateFormat newDateFormat = new SimpleDateFormat("ss mm HH dd MM E");
        String res = null;
        try {
            Date tmp = oldDateFormat.parse(date);
            res = newDateFormat.format(tmp);
            String daysOnWeek = res.substring(15);
            res = res.replaceAll(daysOnWeek, DayToInt(daysOnWeek));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean isValid(String value, String dateFormat) throws DatesToCronConvertException {

        if (value == null || dateFormat == null || dateFormat.length() <= 0) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);

        try {
            formatter.parse(value);
        } catch (ParseException e) {
            throw new DatesToCronConvertException();
        }
        return true;
    }

    private String DayToInt(String day) {
        String result = null;
        switch (day) {
            case "Пн":
                result = "01";
                break;
            case "Вт":
                result = "02";
                break;
            case "Ср":
                result = "03";
                break;
            case "Чт":
                result = "04";
                break;
            case "Пт":
                result = "05";
                break;
            case "Сб":
                result = "06";
                break;
            case "Вс":
                result = "07";
                break;
            default:
                break;
        }
        return result;
    }

    private String DayFromInt(int day) {
        String result = null;
        switch (day) {
            case 1:
                result = "MON";
                break;
            case 2:
                result = "TUE";
                break;
            case 3:
                result = "WED";
                break;
            case 4:
                result = "THU";
                break;
            case 5:
                result = "FRI";
                break;
            case 6:
                result = "SUT";
                break;
            case 7:
                result = "SUN";
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public String getImplementationInfo() {
        return "Асербеков Куанышгали Олегович " + getClass().getSimpleName() + " " + getClass().getPackage().getName()
                + " " + "https://github.com/Kuanyshgali/DatesToCronConverter";
    }
}
