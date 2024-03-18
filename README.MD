CODE
```java
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
```

OUTPUT
```bash
# Delete a file
ENTRY_DELETE: /home/cristian/Downloads/ANGEL.xlsx
Watching for file system events...
# copy a file into
ENTRY_CREATE: /home/cristian/Downloads/capture1 (copy).png
Watching for file system events...
# alter content on file
ENTRY_CREATE: /home/cristian/Downloads/.goutputstream-SH6RK2
ENTRY_MODIFY: /home/cristian/Downloads/.goutputstream-SH6RK2
Watching for file system events...
ENTRY_MODIFY: /home/cristian/Downloads/.goutputstream-SH6RK2
Watching for file system events...
ENTRY_MODIFY: /home/cristian/Downloads/.goutputstream-SH6RK2
Watching for file system events...
ENTRY_DELETE: /home/cristian/Downloads/.goutputstream-SH6RK2
ENTRY_CREATE: /home/cristian/Downloads/comH.modificado
Watching for file system events...
# rename
ENTRY_DELETE: /home/cristian/Downloads/comH.txt
ENTRY_CREATE: /home/cristian/Downloads/comH.modificado
Watching for file system events...

```