package ru.ifmo.ctddev.tenischev.java.archiver;


/**
 * Created by kris13 on 27.10.15.
 */
public class Starter {
    public static void main(String[] args) {
        if (args.length != 0) {
            if ("--no-gui".equals(args[0]) && args.length > 1) {
                String[] files = args[1].split(";");
                String dest = "./archive.zip";
                Integer level;
                if (args.length > 2)
                    if ("-q".equals(args[2])) {
                        try {
                            level = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            System.err.println("");
                        }
                        if (args.length > 4 && "-o".equals(args[4]))
                            dest = args[5];
                    } else if ("-o".equals(args[2]))
                        dest = args[3];

                (new Archive(files, dest, false)).run();
            } else {
                printHelp();
            }
        } else {
            new GUI();
        }
    }

    private static void printHelp() {
        System.out.println("This program create ZIP archive from files");
        System.out.println("Possible use:");
        System.out.println("GUI  : java -jar MyArchiver.jar");
        System.out.println("NoGUI: java -jar MyArchiver.jar [--help] | [--no-gui FILE... [-q 123456789] [-o DEST]]");
        System.out.println("--help - print this help");
        System.out.println("--no-gui - active console mod");
        System.out.println("FILE... - list files separate by semicolon(;) e.g. ./myPhoto.jpg;./myMusic.ogg");
        System.out.println("-q 123456789 - set compress level for archive, default value 5");
        System.out.println("-o DEST - choose destination file, default value './archive.zip'");
    }
}
