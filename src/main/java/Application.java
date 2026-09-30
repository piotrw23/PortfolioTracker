import command.AddCommand;
import command.ListCommand;
import command.PortfolioCommand;
import db.DatabaseManager;
import db.TransactionRepository;
import picocli.CommandLine;

public class Application {

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager(DatabaseManager.defaultPath());
        TransactionRepository repository = new TransactionRepository(dbManager);

        try {
            dbManager.initializeDatabase();
            CommandLine cli = new CommandLine(new PortfolioCommand())
                    .addSubcommand(new AddCommand(repository))
                    .addSubcommand(new ListCommand(repository));

            System.exit(cli.execute(args));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
