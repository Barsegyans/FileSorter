import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileWriterX {

    public final Path path;

    public FileWriterX(Path path) {
        this.path = path;
    }

    public void writeLines(List<String> lines) {
        try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void writeLinesToTheEnd(List<String> lines) {
        try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
