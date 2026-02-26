package com.automation.utils;

import com.automation.globals.ConfigsGlobal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DbUtils {

    private DbUtils() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                ConfigsGlobal.DB_URL,
                ConfigsGlobal.DB_USERNAME,
                ConfigsGlobal.DB_PASSWORD
        );
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindParams(statement, params);
            return statement.executeUpdate();
        }
    }

    public static void bindParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int index = 0; index < params.length; index++) {
            statement.setObject(index + 1, params[index]);
        }
    }
}
