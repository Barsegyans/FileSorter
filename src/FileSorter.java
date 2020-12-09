import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  serbar
 */
public class FileSorter {

    private final Path file;
    private final String tmpDirectory;
    private final String resultFileName;
    private final int stringCountPerFile;

    public FileSorter(Path file, String tmpDirectory, String resultFileName, int stringCountPerFile) {
        this.file = file;
        this.tmpDirectory = tmpDirectory;
        this.resultFileName = resultFileName;
        this.stringCountPerFile = stringCountPerFile;
    }

    /**
     * Sort input file by k-way merge sort.
     *
     * First, we split the file into temporary files, where the lines are stored in an ordered state,
     * then we merge them one line by line, keeping the ordered state.
     * @throws IOException
     */
    public void sortFile() throws IOException {
        generateSortedSplitedFiles();

        File directoryFile = new File(tmpDirectory);

        File[] files = directoryFile.listFiles();
        if (files == null) {
            return;
        }

        List<FileReaderX> fileReaders = Arrays.stream(files)
                .map(file -> {
                    try {
                        return new FileReaderX(new FileReader(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        File tmpResultFile = new File(resultFileName);
        if (tmpResultFile.exists()) {
            tmpResultFile.delete();
            tmpResultFile.createNewFile();
        }

        FileWriterX fileWriter = new FileWriterX(tmpResultFile.toPath());
        fileReaders.forEach(FileReaderX::readLine);
        fileReaders = fileReaders.stream().filter(FileReaderX::isFileNotEmpty).collect(Collectors.toList());
        while (!fileReaders.isEmpty()) {
            List<String> lines = new ArrayList<>(stringCountPerFile);
            for (int i = 0; i < stringCountPerFile && !fileReaders.isEmpty(); i++) {
                FileReaderX readerX = fileReaders.stream().min(FileReaderX::compareTo).get();
                lines.add(readerX.getLine());
                readerX.readLine();
                fileReaders.forEach(fileReaderX -> {
                    if (!fileReaderX.isFileNotEmpty()) {
                        fileReaderX.close();
                    }
                });
                fileReaders = fileReaders.stream().filter(FileReaderX::isFileNotEmpty).collect(Collectors.toList());
            }
            fileWriter.writeLinesToTheEnd(lines);
        }
    }

    /**
     * Generates temporary files by spliting input file and sort each tmp file.
     * @throws FileNotFoundException
     */
    private void generateSortedSplitedFiles() throws FileNotFoundException {
        File directoryFile = new File(tmpDirectory);
        if (directoryFile.exists() && !directoryFile.isDirectory()) {
            throw new RuntimeException("Directory for tmp files can not be created");
        }
        directoryFile.mkdirs();

        File[] files = directoryFile.listFiles();
        if (files != null) {
            Arrays.asList(files).forEach(File::delete);
        }

        BufferedReader r = new BufferedReader(new FileReader(file.toFile()));
        try {

            String line = r.readLine();
            int fileNumber = 1;
            while (line != null) {
                List<String> lines = new ArrayList<>(stringCountPerFile);
                for (int i = 0; i < stringCountPerFile && line != null; i++) {
                    lines.add(line);
                    line = r.readLine();
                }
                FileWriterX fileWriter =
                        new FileWriterX(new File(directoryFile, "tmp" + fileNumber + ".txt").toPath());
                lines.sort(String::compareTo);
                fileWriter.writeLines(lines);
                fileNumber++;
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
