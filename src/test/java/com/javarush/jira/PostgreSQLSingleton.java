package com.javarush.jira;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLSingleton {

    private static PostgreSQLContainer<?> instance;

    private PostgreSQLSingleton() {}

    public static synchronized PostgreSQLContainer<?> getInstance() {
        if (instance == null) {
            instance = new PostgreSQLContainer<>("postgres:15");
            instance.start();
        }
        return instance;
    }
}
