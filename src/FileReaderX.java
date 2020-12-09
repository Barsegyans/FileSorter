import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderX implements Comparable<FileReaderX> {

    private final BufferedReader reader;

    private String line = null;
    private boolean fileIsEmpty = false;

    public FileReaderX(FileReader fileReader) {
        this.reader = new BufferedReader(fileReader);
    }

    public String readLine() {
        try {
            this.line = reader.readLine();
            if (this.line == null) {
                fileIsEmpty = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return this.line;
    }

    public boolean isFileNotEmpty() {
        return !fileIsEmpty;
    }

    public String getLine() {
        return line;
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(FileReaderX o) {
        return this.line.compareTo(o.getLine());
    }
}
