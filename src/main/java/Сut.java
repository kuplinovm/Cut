import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class Сut {
    @Option(name = "-o")
    private String outputName;
    @Option(name = "-c", forbids = {"-w"})
    private boolean s = false;
    @Option(name = "-w", forbids = {"-c"})
    private boolean w = false;
    @Argument(required = true, index = 0)
    String inputName;
    @Argument(required = true, metaVar = "range", usage = "input range", index =1)
    private String range="";


    public static void main(String[] args) throws IOException {
        new Сut().start(args);
    }
    private void start(String[] args)throws IOException {
        CmdLineParser parseString = new CmdLineParser(this);
        try {
            parseString.parseArgument(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println("java -jar spliter.jar -d -l|-c|-n num -o basicOutputName inputFileName");
            parseString.printUsage(System.err);
        }
    }
}
