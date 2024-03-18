package org.cris6h16;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Path.of("/home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/Main.java");
        Path path2 = Path.of(new URI("file:///home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/Main.java"));

//        System.out.println(path + "\n" + path2);
//        System.out.println(
//                "\nExists: " + Files.exists(path2) + "\n" +
//                "Readable: " + Files.isReadable(path2) + "\n" +
//                "Writable: " + Files.isWritable(path2) + "\n\n" +
//                "Hidden: " +   Files.isHidden(path2) + "\n" +
//                "Executable: " +     Files.isExecutable(path2) + "\n" +
//                "Content Type Determined: " + Files.probeContentType(path2) + "\n\n" +
//                "All Attributes: " + Files.readAttributes(path2, "*").toString().replace(", ", "\n\t\t") + "\n" +
//                "Read All Lines: " + Files.readAllLines(path2) + "\n" +
//                "Read All Bytes: " + Arrays.toString(Files.readAllBytes(path2)) + "\n" +
//                "Owner: " +          Files.getOwner(path) + "\n" +
//                "Posix File Permissions: " +  Files.getPosixFilePermissions(path) + "\n"
//        );

        // TEMP file
        Path utfFile = Files.createTempFile("some", ".txt");


        // ============== Write =============== \\

        Files.writeString(
                utfFile,
                "this is my string ää öö üü",
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, //.APPEND, etc
                StandardOpenOption.WRITE
                //........
        );

        // write bytes
        Path oneMoreUtf8File = Files.createTempFile("some", ".txt");
        Files.write(
                oneMoreUtf8File,
                "this is my string ää öö üü".getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        System.out.println("oneMoreUtf8File = " + oneMoreUtf8File);


        // Using Writers and OutputStreams |||
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(utfFile)) {// work with characters
            // handle reader
            bufferedWriter.write("this is my string ää öö üü");
            bufferedWriter.newLine();
            bufferedWriter.append("this is my string aa vv cc");
            bufferedWriter.flush();
        }
        try (OutputStream os = Files.newOutputStream(utfFile)) { // Only work with bytes
            // handle outputstream
            os.write("this is my string ää öö üü".getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        // =========== READ =========== \\

        String s = Files.readString(utfFile, StandardCharsets.UTF_8); // otherwise == utf8
        // read Bytes from files
        s = new String(Files.readAllBytes(utfFile), StandardCharsets.UTF_8);

        // Using Readers and InputStreams |||
        try (BufferedReader br = Files.newBufferedReader(utfFile)) { //use behind: Stream
            // handle reader
            String line = br.readLine();
            Stream<String> lines = br.lines();
            int read = br.read(); // read single character

            char[] chars = new char[10]; // for read 10 by 10 chars
                //[ , , , , , , , , , ]

            int numberOfCharacterRead = br.read(chars); //  = 10; read 10
                //chars = [t, h, i, s,  , i, s,  , m, y]

            int numberOfCharacterRead2 = br.read(chars); // = 10; read the next 10
                //chars = [ , s, t, r, i, n, g,  , ä, ä]
                //                               ==> all chars are different from before

            int numberOfCharacterRead3 = br.read(chars); // = 6; read the next 10, but only remain 6
                //chars = [ , ö, ö,  , ü, ü, g,  , ä, ä]
                //                               ==> 4 last chars are same as before

            int numberOfCharacterRead4 = br.read(chars); // = -1; read 0
                //chars = [ , ö, ö,  , ü, ü, g,  , ä, ä]
                //                               ==> all characters are same as before
        }

        try (InputStream is = Files.newInputStream(utfFile)) {
            // handle inputstream

        }





    }
}