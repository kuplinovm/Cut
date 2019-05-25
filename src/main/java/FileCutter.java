import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public final class FileCutter {

    private FileCutter() { }

    /**
     * @param inpFile полный путь к входному  файлу или null - System.in
     * @param outFile полный путь к выходному файлу или null - System.out
     * @param unit   единица измерения (символ или слово) для параметров begPos, endPos
     * @param begPos номер первого    символа/слова (нумерация с 1)
     * @param endPos номер последнего символа/слова (нумерация с 1)
     * @throws IOException
     */
    public static void cutText(String inpFile, String outFile, Unit unit, Integer begPos, Integer endPos) throws IOException {

        if ( inpFile != null && !inpFile.isEmpty() && Files.notExists(Paths.get(inpFile)) )
            throw new FileNotFoundException(inpFile);

        checkPos(begPos, endPos);

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = ( inpFile != null && !inpFile.isEmpty() ) ? new BufferedReader(new FileReader(inpFile)) :
                    new BufferedReader(new InputStreamReader(System.in));
            writer = ( outFile != null && !outFile.isEmpty() ) ? new BufferedWriter(new FileWriter(outFile)) :
                    new BufferedWriter(new OutputStreamWriter(System.out));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(cutSubstr(line, unit, begPos, endPos));
                writer.newLine();
            }
        }
        finally {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        }
    }

    // из строки line вырезается подстрока по фильтру=(unit, begPos, endPos)
    static String cutSubstr(String line, Unit unit, Integer begPos, Integer endPos) {
        checkPos(begPos, endPos);
        String substr;
        if (begPos == null) begPos = 1;
        if (unit == Unit.CHAR) {
            if (endPos == null || endPos > line.length()) endPos = line.length();
            substr = (begPos <= endPos) ? line.substring(begPos-1, endPos) : "";
        }
        else if (unit == Unit.WORD) {
            String[] words = line.split(" ");
            if (endPos == null || endPos > words.length) endPos = words.length;
            substr = (begPos <= endPos) ? String.join(" ", Arrays.copyOfRange(words, begPos-1, endPos)) : "";
        } else {
            throw new IllegalArgumentException("unit="+unit);
        }
        return substr;
    }

    private static void checkPos(Integer begPos, Integer endPos) {
        if (begPos != null && begPos <= 0) throw new IllegalArgumentException("begPos="+begPos+" <= 0");
        if (endPos != null && endPos <= 0) throw new IllegalArgumentException("endPos="+endPos+" <= 0");
        if (begPos != null && endPos != null && begPos > endPos)
            throw new IllegalArgumentException("begPos="+begPos+", endPos="+endPos+", begPos > endPos");
    }

    public enum Unit { CHAR, WORD } // единица измерения (символ или слово)
}