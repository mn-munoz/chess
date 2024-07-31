package dataaccess;

import java.sql.SQLException;

public class DatabaseConnection {

    DatabaseConnection() throws DataAccessException{
        configureDatabase();
    }

    private final String[] createStatements = {
    """
    CREATE TABLE IF NOT EXISTS users_table (
    `username` varchar(256) UNIQUE NOT NULL,
    `userData` JSON NOT NULL,
    PRIMARY KEY (`username`),
    INDEX(username)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    """,
    """
    CREATE TABLE IF NOT EXISTS auth_table (
    `authtoken` varchar(100) NOT NULL,
    `username` varchar(256) NOT NULL,
    PRIMARY KEY (`authtoken`),
    FOREIGN KEY (`username`) REFERENCES users_table(`username`)
    ON UPDATE CASCADE
    ON DELETE CASCADE
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
