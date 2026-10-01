package com.blazedemo.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    /** Saves a screenshot to test-output/screenshots and returns its path, or null on failure. */
    public static String capture(WebDriver driver, String name) {
        if (driver == null) {
            return null;
        }
        try {
            Path dir = Path.of("test-output", "screenshots");
            Files.createDirectories(dir);
            Path target = dir.resolve(name.replaceAll("[^a-zA-Z0-9._-]", "_") + ".png");
            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            return target.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String toBase64(String path) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(Path.of(path)));
        } catch (Exception e) {
            return "";
        }
    }
}
