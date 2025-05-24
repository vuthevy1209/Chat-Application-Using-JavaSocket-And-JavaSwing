package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static String copyFile(String source, String dest, String fileName) {
        try {
            File sourceFile = new File(source);
            if (!sourceFile.exists()) {
                throw new IOException("Source file does not exist: " + source);
            }

            String extension = getExtension(source);

            File destDir = new File(dest);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            Path destPath = Path.of(destDir.getAbsolutePath(), fileName + extension);
            Files.copy(sourceFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            return "public/" + fileName + extension;
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
            return null;
        }
    }

    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    public static String getExtension(String path) {
        File file = new File(path);
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }

    public static boolean existFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void deleteAllFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.delete()) {
                        System.out.println("File deleted: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Error deleting file: " + file.getAbsolutePath());
                    }
                } else if (file.isDirectory()) {
                    deleteAllFilesInDirectory(file.getAbsolutePath());
                    if (file.delete()) {
                        System.out.println("File deleted: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Error deleting file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }


}