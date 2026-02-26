package com.company.framework.helpers;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class SystemHelpers {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String makeSlug(String input) {
        if (input == null)
            throw new IllegalArgumentException();

        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("_");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
     * @return Get the path to your source directory with a / at the end
     */
    public static String getCurrentDir() {
        String current = System.getProperty("user.dir") + File.separator;
        return current;
    }

    public static boolean createFolder(String path) {
        try {
            File folder = new File(path);

            // Kiểm tra xem đã tồn tại và có phải là folder không
            if (folder.exists() && folder.isDirectory()) {
                System.out.println("Folder đã tồn tại: " + path);
                return false;
            }

            // Tạo folder và các thư mục cha
            boolean created = folder.mkdirs();

            if (created) {
                System.out.println("Tạo folder thành công: " + path);
            } else {
                System.out.println("Tạo folder thất bại: " + path);
            }

            return created;
        } catch (Exception e) {
            System.out.println("Lỗi khi tạo folder: " + e.getMessage());
            return false;
        }
    }

    /**
     * @param str        string to be split based on condition
     * @param valueSplit the character to split the string into an array of values
     * @return array of string values after splitting
     */
    public static ArrayList<String> splitString(String str, String valueSplit) {
        ArrayList<String> arrayListString = new ArrayList<>();
        for (String s : str.split(valueSplit, 0)) {
            arrayListString.add(s);
        }
        return arrayListString;
    }

    public static boolean checkValueInListString(String expected, String listValues[]) {
        boolean found = false;

        for (String s : listValues) {
            if (s.equals(expected)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean checkValueInListString(String expected, List<String> listValues) {
        boolean found = false;

        for (String s : listValues) {
            if (s.equals(expected)) {
                found = true;
                break;
            }
        }
        return found;
    }
}

