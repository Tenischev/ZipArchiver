package ru.ifmo.ctddev.tenischev.java.archiver;

import java.io.File;
import java.util.zip.ZipEntry;

/**
 * Created by kris13 on 28.10.15.
 */
public class Archive implements Runnable {
    private final boolean guiMod;
    private final String[] nameFiles;
    private final String nameDest;
    /**
     * Level compressed Zip file, by default set 5
     */
    private final int compressLevel;

    public Archive(String[] files, String dest, boolean gui) {
        this(files, dest, 5, gui);
    }

    public Archive(String[] files, String dest, int level, boolean gui) {
        nameFiles = files;
        nameDest = dest;
        guiMod = gui;
        compressLevel = level;
    }

    @Override
    public void run() {

    }
}
