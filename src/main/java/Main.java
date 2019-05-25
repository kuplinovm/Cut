import org.apache.commons.cli.*;
import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        CmdParams prm = parseArgs(args);
        FileCutter.cutText(prm.inpFile, prm.outFile, prm.unit, prm.begPos, prm.endPos);
        //Test1();
    }

    static class CmdParams {
        public final String inpFile;
        public final String outFile;
        public final FileCutter.Unit unit;
        public final Integer begPos;
        public final Integer endPos;

        public CmdParams(String inpFile, String outFile, FileCutter.Unit unit, Integer begPos, Integer endPos) {
            this.inpFile = inpFile;
            this.outFile = outFile;
            this.unit = unit;
            this.begPos = begPos;
            this.endPos = endPos;
        }
    }

    static CmdParams parseArgs(String[] args) throws ParseException {
        if (!args[0].equalsIgnoreCase("cut"))
            throw new IllegalArgumentException("args[0] != cut"); // первый аргумент всегда равен "cut"

        // эти значения извлекаются из командной строки args
        String inpFile = null, outFile = null;
        FileCutter.Unit unit = null;
        Integer begPos = null, endPos = null;

        // разбор диапазона - последний аргумент командной строки
        // диапазон может иметь вид "-5" : такой формат библиотека apache.cli считает ключом, но любой ключ надо предварительно определить
        String range = args[args.length-1];
        Pattern pattern = Pattern.compile("^(?<begPos>\\d*)-(?<endPos>\\d*)$");
        Matcher m = pattern.matcher(range);
        if (m.find()) {
            String begPosStr = m.group("begPos");
            begPos = !begPosStr.isEmpty() ? Integer.parseInt(begPosStr) : null;
            String endPosStr = m.group("endPos");
            endPos = !endPosStr.isEmpty() ? Integer.parseInt(endPosStr) : null;
        } else {
            throw new IllegalArgumentException("последний аргумент командной строки = '"+range+"' не соответствует формату диапазона");
        }

        // разбор остальных аргументов командной строки
        Options opts = new Options();

        Option charOpt = new Option("c",false,"char");
        charOpt.setRequired(false);
        Option wordOpt = new Option("w",false,"word");
        wordOpt.setRequired(false);
        OptionGroup unitOptGrp = new OptionGroup(); // должен быть задан ровно один ключ -c или -w
        unitOptGrp.addOption(charOpt);
        unitOptGrp.addOption(wordOpt);
        unitOptGrp.setRequired(true);
        opts.addOptionGroup(unitOptGrp);

        Option outFileOpt = new Option("o", true, "output file");
        outFileOpt.setRequired(false);
        opts.addOption(outFileOpt);

        CommandLine cmd = new DefaultParser().parse(
                opts,
                Arrays.copyOfRange(args,0, args.length-1) /*удаляем последний аргумент - диапазон*/
        );
        String[] nonRecognArgs = cmd.getArgs(); // неразобранные аргументы: "cut", inpFile если есть
        inpFile = (nonRecognArgs.length >= 2) ? nonRecognArgs[1] : null;
        outFile = cmd.hasOption("o") ? cmd.getOptionValue("o") : null;
        unit = cmd.hasOption("c") ? FileCutter.Unit.CHAR : FileCutter.Unit.WORD;
        CmdParams prm = new CmdParams(inpFile, outFile, unit, begPos, endPos);
        return prm;
    }

    private static void Test1() throws IOException {
        // Ctrl+D для выхода из консольного input
        FileCutter.cutText(
                "g:\\DiskG\\Books\\Java\\Projects\\IdeaProjects\\FileCutterProject\\input.txt",
                "g:\\DiskG\\Books\\Java\\Projects\\IdeaProjects\\FileCutterProject\\out.txt",
                FileCutter.Unit.CHAR, 1, 3
        );
    }
}
