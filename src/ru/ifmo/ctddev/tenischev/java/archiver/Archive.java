package ru.ifmo.ctddev.tenischev.java.archiver;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by kris13 on 28.10.15.
 */
public class Archive implements Runnable {
    public static final Integer DEFAULT_COMPRESS = 5;
    public static final String DEFAULT_DEST = "./archive.zip";
    private final boolean guiMod;
    private final String[] nameFiles;
    private final String nameDest;
    private int compressLevel = DEFAULT_COMPRESS;
    private ArchiveGUI gui;

    public Archive(String[] files, String dest, boolean gui) {
        nameFiles = files;
        nameDest = dest;
        guiMod = gui;
    }

    public Archive(String[] files, String dest, int level, boolean gui) {
        this(files, dest, gui);
        if (0 > level || level > 9) {
            System.err.println("Incorrect compression level, default value set");
            level = DEFAULT_COMPRESS;
        }
        compressLevel = level;
    }

    @Override
    public void run() {
        if (guiMod)
            gui = ArchiveGUI.create(Thread.currentThread());
        try (OutputStream outputStream = new FileOutputStream(nameDest)) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                updateStatus("Start archive files");
                zipOutputStream.setLevel(compressLevel);
                updateStatus(String.format("Set compression level to %d", compressLevel));
                for (String fileName : nameFiles) {
                    updateStatus(String.format("Add file '%s' to zip", fileName));
                    File file = new File(fileName);
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipEntry.setMethod(ZipEntry.DEFLATED);
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            byte[] buf = new byte[1024];
                            int b;
                            try {
                                while ((b = fileInputStream.read(buf)) != -1 && !Thread.interrupted()) {
                                    try {
                                        zipOutputStream.write(buf, 0, b);
                                    } catch (IOException e) {
                                        notifyStat("Error occurred when write into zip");
                                    }
                                }
                            } catch (IOException e) {
                                notifyStat("Error occurred when read of " + fileName);
                            }
                        } catch (IOException e) {
                            notifyStat(String.format("Error occurred when add file '%s' to zip", fileName));
                        }
                    } catch (FileNotFoundException e) {
                        notifyStat("Can't find file " + fileName);
                    } catch (IOException e) {
                        notifyStat("Can't close file " + fileName);
                    }
                    if (Thread.interrupted())
                        break;
                }
                updateStatus("Archive complete!");
            }
        } catch (FileNotFoundException | RuntimeException e) {
            notifyStat("Incorrect destination file, stop");
        } catch (IOException e) {
            notifyStat("Can't close destination file");
        }
    }

    private void notifyStat(String str) {
        if (!guiMod)
            System.err.println(str);
        else {
            gui.addMessage(str);
        }
    }

    private void updateStatus(String str) {
        if (!guiMod)
            System.out.println(str);
        else {
            gui.verifyStatus(str);
            gui.addMessage(str);
        }
    }
}
