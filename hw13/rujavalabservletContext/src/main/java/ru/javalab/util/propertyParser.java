package ru.javalab.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class propertyParser {
    Scanner reader;

    public propertyParser(String path) throws FileNotFoundException {
        reader = new Scanner(new File(path));
    }

    public Map<String,String> propertyParse(){
        Map<String,String> map = new HashMap<>();

        while (reader.hasNextLine()) {
            String[] mas = reader.nextLine().split(": ");
            map.put(mas[0], mas[1]);
        }
        return map;
    }
}
