package com.project;

import java.io.*;
import java.util.Properties;

public class Config {

    public static String token;

    public Config() throws IOException {

        Properties properties = new Properties();
        File file = new File("properties.properties");
        if (!file.isFile()){

            System.out.println(file.createNewFile() ? "File with config created!" : "File with config existed!");
            properties.put("token", token);
            properties.store(new FileOutputStream(file), null);
            return;

        }

        properties.load(new FileInputStream(file));
        if(properties.elements() == null){
            properties.put("token", token);
            properties.store(new FileOutputStream(file), null);
            return;
        }
        token = properties.getProperty("token");
    }

    public static void save() throws IOException {

        Properties properties = new Properties();
        File file = new File("properties.properties");
        properties.put("token", token);
        properties.store(new FileOutputStream(file), null);

    }

}
