package ru.ifmo.ctddev.tenischev.java.archiver;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kris13 on 28.10.15.
 */
public class Archive implements Runnable {
    public static final Integer DEFAULT_COMPRESS = 5;
    private final boolean guiMod;
    private final String[] nameFiles;
    private final String nameDest;
    private int compressLevel = DEFAULT_COMPRESS;

    public Archive(String[] files, String dest, boolean gui) {
        nameFiles = files;
        nameDest = dest;
        guiMod = gui;
    }

    public Archive(String[] files, String dest, int level, boolean gui) {
        this(files, dest, gui);
        compressLevel = level;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = new FileOutputStream(nameDest)) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                if (!guiMod)
                    System.out.println("Start archive files");
                zipOutputStream.setLevel(compressLevel);
                if (!guiMod)
                    System.out.println(String.format("Set compression level to %d", compressLevel));
                for (String fileName : nameFiles) {
                    if (!guiMod)
                        System.out.println(String.format("Add file '%s' to zip", fileName));
                    File file = new File(fileName);
                    try (FileInputStream fileInputStream = new FileInputStream(file)){
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipEntry.setMethod(ZipEntry.DEFLATED);
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            byte[] buf = new byte[1024];
                            int b;
                            try {
                                while ((b = fileInputStream.read(buf)) != -1) {
                                    try {
                                        zipOutputStream.write(buf, 0, b);
                                    } catch (IOException e) {
                                        System.err.println("Error occurred when write into zip");
                                    }
                                }
                            } catch (IOException e) {
                                System.err.println("Error occurred when read of " + fileName);
                            }
                        } catch (IOException e) {
                            System.err.println(String.format("Error occurred when add file '%s' to zip", fileName));
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("Can't find file " + fileName);
                    } catch (IOException e) {
                        System.err.println("Can't close file " + fileName);
                    }
                }
            }
        } catch (FileNotFoundException | RuntimeException e) {
            System.err.println("Incorrect destination file, stop");
        } catch (IOException e) {
            System.err.println("Can't close destination file");
        }
        if (!guiMod)
            System.out.println("Archive complete!");
    }
}
