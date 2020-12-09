import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSorterMain {

    public static int STRING_LENGTH = 5;
    public static int STRING_COUNT = 1000000;

    public static void main(String[] args) {

        int stringLength = STRING_LENGTH;
        int stringCount = STRING_COUNT;
        int tmpFileCount = STRING_COUNT / 10;
        String resultFileName = "result.txt";
        for (int i = 0; i < args.length; i++) {
            switch ((args[i])) {
                case "-strLen":
                    if (++i >= args.length) {
                        throw new RuntimeException("String length not set");
                    }
                    stringLength = Integer.valueOf(args[i]);
                    break;
                case "-strCount":
                    if (++i >= args.length) {
                        throw new RuntimeException("String count not set");
                    }
                    stringCount = Integer.valueOf(args[i]);
                    break;
                case "-resultFileName":
                    if (++i >= args.length) {
                        throw new RuntimeException("Result file name not set");
                    }
                    resultFileName = args[i];
                    break;
                case "-tmpFileCount":
                    if (++i >= args.length) {
                        throw new RuntimeException("Temporary file count not set");
                    }
                    tmpFileCount = Integer.valueOf(args[i]);
                    break;
                default:
                    break;
            }
        }

        System.out.println("String length: " + stringLength);
        System.out.println("String count: " + stringCount);
        System.out.println("Tmp file count: " + tmpFileCount);

        long start = System.currentTimeMillis();
        Path path = Paths.get("file.txt");
        try {

            new FileGenerator(path).generate(stringLength, stringCount);

            FileSorter fileSorter =
                    new FileSorter(path, "tmp", resultFileName, STRING_COUNT / tmpFileCount);

            fileSorter.sortFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


        long end = System.currentTimeMillis();
        System.out.println("----------");
        System.out.println("Time: " + (end - start));
        System.out.println("Result file name: " + resultFileName);

    }
}
