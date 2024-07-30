package dataaccess;

import java.sql.SQLException;

public class DatabaseConnection {

    DatabaseConnection() throws DataAccessException{
        configureDatabase();
    }

    private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS users_table (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(256) UNIQUE NOT NULL,
    `userData` JSON NOT NULL,
    PRIMARY KEY (`id`),
    INDEX(username)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database: " + e.getMessage());
        }
    }

}
