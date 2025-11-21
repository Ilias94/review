package com.ilias.review.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    /**
     * Reads a JSON file from src/test/resources and returns it as a String.
     *
     * @param path path relative to src/test/resources (e.g. "api/books/book_valid_1.json")
     * @return JSON file content
     */
    public static String readJson(String path) {
        try {
            InputStream is = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(path);

            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }

            return new String(is.readAllBytes(), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Cannot read JSON file: " + path, e);
        }
    }
}
