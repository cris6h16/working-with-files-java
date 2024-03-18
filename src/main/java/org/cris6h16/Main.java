package org.cris6h16;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;


public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Path.of("/home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/Main.java");
        Path path2 = Path.of(new URI("file:///home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/Main.java"));

        System.out.println(path + "\n" + path2);
        System.out.println(
                "\nExists: " + Files.exists(path2) + "\n" +
                        "Readable: " + Files.isReadable(path2) + "\n" +
                        "Writable: " + Files.isWritable(path2) + "\n\n" +
                        "Hidden: " + Files.isHidden(path2) + "\n" +
                        "Executable: " + Files.isExecutable(path2) + "\n" +
                        "Content Type Determined: " + Files.probeContentType(path2) + "\n\n" +
                        "All Attributes: " + Files.readAttributes(path2, "*").toString().replace(", ", "\n\t\t") + "\n" +
                        "Read All Lines: " + Files.readAllLines(path2) + "\n" +
                        "Read All Bytes: " + Arrays.toString(Files.readAllBytes(path2)) + "\n" +
                        "Owner: " + Files.getOwner(path) + "\n" +
                        "Posix File Permissions: " + Files.getPosixFilePermissions(path) + "\n" +
                        "Absolute Path: " + path.toAbsolutePath() + "\n"
        );

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
            int read = is.read(); // read single byte
            byte[] bytes = new byte[10]; // for read 10 by 10 bytes
            int numberOfBytesRead = is.read(bytes); // = 10
            int numberOfBytesRead2 = is.read(bytes); // = 10
            int numberOfBytesRead3 = is.read(bytes); // = 10
            int numberOfBytesRead4 = is.read(bytes); // = 1
            int numberOfBytesRead5 = is.read(bytes); // = -1
            int numberOfBytesRead6 = is.read(bytes); // = -1
        }

        // =========== Moving, Deleting & Listing Files =========== \\

        Path p1 = Files.createTempFile("some", ".txt");

        /*
        it does not move a file to a designated directory (which you might expect).
         */
        try {
            Files.move(p1, Path.of("c:\\dev"));  // this is wrong!
        } catch (FileAlreadyExistsException e) {
            // welp, that din't work!
        }
        // options to move
        Path utfFile2 = Files.createTempFile("some", ".txt");
        Files.move(
                utfFile2,
                Path.of("/home/cristian/Desktop").resolve(utfFile.getFileName().toString()),
                StandardCopyOption.REPLACE_EXISTING // .ATOMIC_MOVE
        );

        // delete
        try {
            Files.delete(utfFile2);
        } catch (DirectoryNotEmptyException | NoSuchFileException e){
            e.printStackTrace();
        }

        try (Stream<Path> walk = Files.walk(utfFile2)) { // get all files in directory
            walk.sorted(Comparator.reverseOrder()).forEach(path1 -> {
                try {
                    Files.delete(path1);
                } catch (IOException e) {
                    // something could not be deleted..
                    e.printStackTrace();
                }
            });
        }catch (NoSuchFileException e){
            e.printStackTrace();
        }

        utfFile2 = Path.of("/home/cristian/Downloads/");
        // ======= List files in directory ========= \\
        try (Stream<Path> files = Files.list(utfFile2)) {
            files.forEach(System.out::println);
        }

        try (DirectoryStream<Path> files = Files.newDirectoryStream(utfFile2, "*.txt")) { // list only .txt files
            files.forEach(System.out::println);
        }


        // =========== Watching Files & Directories =========== \\

        // Create a WatchService instance for monitoring file system events
        WatchService watcher = FileSystems.getDefault().newWatchService();

        // Define the directory path to watch
        Path pathToWatch = Path.of("/home/cristian/Downloads");

        // Register events to watch for in the specified directory
        pathToWatch.register(
                watcher,
                StandardWatchEventKinds.ENTRY_CREATE,    // Watch for file creation events
                StandardWatchEventKinds.ENTRY_DELETE,    // Watch for file deletion events
                StandardWatchEventKinds.ENTRY_MODIFY     // Watch for file modification events
        );

        // Infinite loop to continuously monitor for file system events
        for (; ; ) {
            WatchKey key;
            try {
                // Wait until a key representing file system events is available
                key = watcher.take(); // Blocking method
            } catch (InterruptedException x) {
                return; // Exit the loop if interrupted
            }

            // Process events in the key
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // Skip OVERFLOW event
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                // Cast the event to WatchEvent<Path> to get the context (filename)
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();

                // Resolve the full path of the changed file within the watched directory
                Path changedFile = pathToWatch.resolve(filename);

                // Perform actions based on the type of event (e.g., create, delete, modify)
                // Example: Print the event type and the path of the changed file
                System.out.println(kind + ": " + changedFile);
            }

            // Reset the key for the next set of events and continue watching
            boolean valid = key.reset();
            if (!valid) {
                break; // Exit the loop if the key is no longer valid (e.g., directory deleted)
            }

            System.out.println("Watching for file system events...");
        }
    }


}
