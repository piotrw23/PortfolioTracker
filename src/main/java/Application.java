import db.DatabaseManager;

public class Application {

    public static void main(String[] args) {
        DatabaseManager dbMgr = new DatabaseManager();

        dbMgr.initializeDatabase();
    }
}
