package command;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import java.util.concurrent.Callable;

@Command(
        name = "portfolio",
        mixinStandardHelpOptions = true,
        description = "Tracking shares portfolio",
        version = "0.1.0"
)
public class PortfolioCommand implements Callable<Integer> {
    @Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws Exception {
        spec.commandLine().usage(System.out);
        return 0;
    }
}
