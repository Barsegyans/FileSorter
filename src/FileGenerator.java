import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class FileGenerator {

    private final Path path;
    private final int leftLimit = 48; // numeral '0'
    private final int rightLimit = 57; // numeral '9'
    private final Random random = new Random();

    public FileGenerator(Path path) {
        this.path = path;
    }

    public void generate(int stringLength, int stringCount) throws IOException {

        FileWriter fw = new FileWriter(path.toString());

        try {
            for (int i = 0; i < stringCount; i++) {
                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(stringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                fw.write(generatedString + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            fw.close();
        }
    }
}
