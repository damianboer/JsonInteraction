import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;

public class JsonFileWatcher {

    private final Path watchDir;
    private final Consumer<File> onFileDetected;
    private volatile boolean running = true;

    public JsonFileWatcher(String dirPath, Consumer<File> onFileDetected) {
        this.watchDir = Paths.get(dirPath);
        this.onFileDetected = onFileDetected;
    }

    public void start() throws IOException, InterruptedException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        watchDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
        System.out.println("Watching: " + watchDir.toAbsolutePath());

        while (running) {
            WatchKey key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    File newFile = watchDir.resolve((Path) event.context()).toFile();
                    if (newFile.getName().endsWith(".json")) {
                        System.out.println("Detected: " + newFile.getName());
                        onFileDetected.accept(newFile);
                    }
                }
            }
            key.reset();
        }
    }

    public void stop() { running = false; }
}