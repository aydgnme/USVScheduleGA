package com.dm.config;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvLoader {
    public static void load() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        System.setProperty("ORACLE_URL", dotenv.get("ORACLE_URL"));
        System.setProperty("ORACLE_USER", dotenv.get("ORACLE_USER"));
        System.setProperty("ORACLE_PASSWORD", dotenv.get("ORACLE_PASSWORD"));
    }
}