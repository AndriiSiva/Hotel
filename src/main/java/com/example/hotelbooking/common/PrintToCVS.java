package com.example.hotelbooking.common;

import lombok.AllArgsConstructor;
import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
public class PrintToCVS {

    public static final String PATH = "output/data.csv";

    public final static String HEAD = "name,count\n";

    public void save(String data) {

        try (FileWriter file = new FileWriter(PATH)) {

            file.write(HEAD + System.lineSeparator() + data + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
