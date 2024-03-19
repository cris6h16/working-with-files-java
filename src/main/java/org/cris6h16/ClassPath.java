package org.cris6h16;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;


public class ClassPath {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Path path = Path.of("/home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/Path.java");
        Path path2 = Path.of(new URI("file:///home/cristian/personal/intellij/IdeaProjects/Files/src/main/java/org/cris6h16/ClassPath.java"));

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
                        "Owner: " + Files.getOwner(path2) + "\n" +
                        "Posix File Permissions: " + Files.getPosixFilePermissions(path2) + "\n" +
                        "Absolute Path: " + path2.toAbsolutePath() + "\n"
        );

        // TEMP file
        Path utfFile = Files.createTempFile("some", ".txt");
        // ============== Write =============== \\

        Files.writeString(
                utfFile,
                "this is my string ää öö üü",
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,//.APPEND, etc
                StandardOpenOption.WRITE
                //........
        );

        // write bytes
        Path oneMoreUtf8File = Files.createTempFile("some",
                ".txt");
        Files.write(
                oneMoreUtf8File,
                "this is my string ää öö üü".getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        System.out.println("oneMoreUtf8File = " + oneMoreUtf8File);


        // Using Writers and OutputStreams |<|>|

        // BufferedWriter works with CHARACTERS || Strings
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(utfFile)) {
            bufferedWriter.write("this is my string ää öö üü");
            bufferedWriter.newLine();
            bufferedWriter.append("this is my string aa vv cc");
            bufferedWriter.flush();
        }

        // OutputStream works only with ==> bytes
        try (OutputStream os = Files.newOutputStream(utfFile)) {
            os.write("this is my string ää öö üü"
                    .getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        // =========== READ =========== \\

        String s = Files.readString(
                utfFile,
                StandardCharsets.UTF_8);
        // read file which contains bytes
        s = new String(Files.readAllBytes(utfFile),
                StandardCharsets.UTF_8);


        // Using Readers and InputStreams |||

        //use behind: Stream
        try (BufferedReader br = Files.newBufferedReader(utfFile)) {
            String line = br.readLine();
            Stream<String> lines = br.lines();
            // read single character
            int read = br.read();

            // for read 10 by 10 chars
            char[] chars = new char[10];
            //[ , , , , , , , , , ]

            //  = 10; read 10 \\
            int numberOfCharacterRead = br.read(chars);
            //chars = [t, h, i, s,  , i, s,  , m, y]

            // = 10; read the next 10 \\
            int numberOfCharacterRead2 = br.read(chars);
            //chars = [ , s, t, r, i, n, g,  , ä, ä]
            //      ==> all chars are different from before

            // = 6; read the next 10, but only remain 6 \\
            int numberOfCharacterRead3 = br.read(chars);
            //chars = [ , ö, ö,  , ü, ü, g,  , ä, ä]
            //       ==> 4 last chars are same as before

            // = -1; read 0 \\
            int numberOfCharacterRead4 = br.read(chars);
            //chars = [ , ö, ö,  , ü, ü, g,  , ä, ä]
            //      ==> all characters are same as before
        }

        // read in bytes
        try (InputStream is = Files.newInputStream(utfFile)) {
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

        Path source = Files
                .createTempFile("helloword2", ".txt");

        // Files.move(source, destination, options);
        Files.move(
                source,
                Path
                        // without use .resolve: DirectoryNotEmptyException
                        .of("/home/cristian/Desktop")
                        .resolve("helloword2.txt")
                ,
                StandardCopyOption.REPLACE_EXISTING // .ATOMIC_MOVE
        );

        // delete
        try {
            Files.delete(source);
        } catch (DirectoryNotEmptyException | NoSuchFileException e) {
            e.printStackTrace();
        }

        // get all files in directory
        try (Stream<Path> walk = Files.walk(source)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path1 -> {
                try {
                    Files.delete(path1);
                } catch (IOException e) {
                    // something could not be deleted..
                    e.printStackTrace();
                }
            });
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        }

        source = Path.of("/home/cristian/Downloads/");

        try (Stream<Path> files = Files.list(source)) {
            files.forEach(System.out::println);
        }
        // list only .txt files
        try (DirectoryStream<Path> files = Files
                .newDirectoryStream(source, "*.txt")) {
            files.forEach(System.out::println);
        }


        // =========== Watching Files & Directories =========== \\

        /* Create a WatchService instance for monitoring
            file system events */
        java.nio.file.WatchService watcher = FileSystems
                .getDefault()
                .newWatchService();

        // Define the directory path to watch
        Path pathToWatch = Path.of("/home/cristian/Downloads");

        // Register events to watch for in the specified directory
        pathToWatch.register(
                watcher,
                // Watch for file creation events
                StandardWatchEventKinds.ENTRY_CREATE,
                // Watch for file deletion events
                StandardWatchEventKinds.ENTRY_DELETE,
                // Watch for file modification events
                StandardWatchEventKinds.ENTRY_MODIFY
        );

        /* Infinite loop to continuously monitor for
           file system events */
//        for (; ; ) {
//            WatchKey key;
//            try {
//            /* Wait/Block until a key representing file system
//            events is available */
//                key = watcher.take();
//            } catch (InterruptedException x) {
//                return; // Exit the loop if interrupted
//            }
//            // Process events in the key
//            for (WatchEvent<?> event : key.pollEvents()) {
//                WatchEvent.Kind<?> kind = event.kind();
//                /* Skip OVERFLOW event ->
//                    events was lost or discarded */
//                if (kind == StandardWatchEventKinds.OVERFLOW) {
//                    continue;
//                }
//                /* Cast the event to WatchEvent<Path> to get
//                        the context (filename) */
//                WatchEvent<Path> ev = (WatchEvent<Path>) event;
//                Path filename = ev.context();
//
//                /* Resolve the full path of the changed file
//                    within the watched directory */
//                Path changedFile = pathToWatch.resolve(filename);
//
//                /* Perform actions based on the type of event
//                    (e.g., create, delete, modify)
//                    Example: Print the event type and the path
//                    of the changed file
//                */
//                System.out.println(kind + ": " + changedFile);
//            }
//
//            /* Reset the key for the next set of events
//                and continue watching */
//            boolean valid = key.reset();
//            if (!valid) {
//                /*  Exit the loop if the key is no longer
//                    valid (e.g., directory deleted) */
//                break;
//            }
//
//            System.out.println("Watching for file system events..");
//        }


        // Watcher use Apache Commons IO

        /*
           <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.15.1</version>
           </dependency>
         */
        try {
            FileAlterationObserver observer =
                    new FileAlterationObserver("/home/cristian/Downloads");

            FileAlterationMonitor monitor =
                    new FileAlterationMonitor(5000);

            observer.addListener(new FileAlterationListenerAdaptor() {
                @Override
                public void onFileCreate(File file) {
                    System.out.println("File created: " + file.getName());
                }

                @Override
                public void onFileDelete(File file) {
                    System.out.println("File deleted: " + file.getName());
                }

                @Override
                public void onFileChange(File file) {
                    System.out.println("File changed: " + file.getName());
                }

                @Override
                public void onDirectoryChange(File directory) {
                    System.out.println("Directory changed: " + directory.getName());
                }

                @Override
                public void onDirectoryCreate(File directory) {
                    System.out.println("Directory created: " + directory.getName());
                }

                @Override
                public void onDirectoryDelete(File directory) {
                    System.out.println("Directory deleted: " + directory.getName());
                }

                @Override
                public void onStart(FileAlterationObserver observer) {
//                    System.out.println("Monitoring started");
                }

                @Override
                public void onStop(FileAlterationObserver observer) {
//                    System.out.println("Monitoring stopped\n");
                }
            });

            monitor.addObserver(observer);
            monitor.start();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }



    }

}


