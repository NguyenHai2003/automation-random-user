package com.automation.tests.user;

import com.automation.base.BaseTest;
import com.automation.domain.user.service.UserService;
import com.automation.models.user.UserRequest;
import com.automation.utils.DbUtils;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class DatabaseValidationTest extends BaseTest {

    private final UserService userService = new UserService();
    private final Faker faker = new Faker();

    private UserRequest buildUser(String email) {
        return UserRequest.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(email)
                .phone(faker.phoneNumber().cellPhone())
                .address(faker.address().fullAddress())
                .avatar("https://randomuser.me/api/portraits/lego/1.jpg")
                .gender(faker.options().option("male", "female"))
                .country(faker.address().country())
                .registered(Instant.now().toString())
                .build();
    }

    private int createUser(UserRequest request) {
        Response response = userService.createUser(request);
        response.then().statusCode(201);
        return response.jsonPath().getInt("data.id");
    }

    @Test(description = "DB_01 - Verify user inserted after POST")
    public void validateUserInsertedAfterPost() throws SQLException {
        String email = "test@user.com";
        DbUtils.executeUpdate("DELETE FROM users WHERE email = ?", email);

        UserRequest request = buildUser(email);
        createUser(request);

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT first_name, last_name, gender, country FROM users WHERE email = ? ORDER BY id DESC LIMIT 1"
             )) {
            DbUtils.bindParams(statement, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assert.assertTrue(resultSet.next(), "Expected inserted row for test@user.com");
                Assert.assertEquals(resultSet.getString("first_name"), request.getFirstName());
                Assert.assertEquals(resultSet.getString("last_name"), request.getLastName());
                Assert.assertEquals(resultSet.getString("gender"), request.getGender());
                Assert.assertEquals(resultSet.getString("country"), request.getCountry());
            }
        }
    }

    @Test(description = "DB_02 - Verify update reflected in DB")
    public void validateUpdateReflectedInDb() throws SQLException {
        UserRequest request = buildUser("db02_" + faker.internet().emailAddress());
        int userId = createUser(request);

        request.setCountry("Brazil");
        Response updateResponse = userService.updateUser(userId, request);
        updateResponse.then().statusCode(200);

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT country, created_at, updated_at FROM users WHERE id = ?"
             )) {
            DbUtils.bindParams(statement, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assert.assertTrue(resultSet.next(), "User row should exist after update");
                Assert.assertEquals(resultSet.getString("country"), "Brazil");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Timestamp updatedAt = resultSet.getTimestamp("updated_at");
                Assert.assertNotNull(createdAt);
                Assert.assertNotNull(updatedAt);
                Assert.assertTrue(updatedAt.after(createdAt), "updated_at should be newer than created_at");
            }
        }
    }

    @Test(description = "DB_03 - Verify delete removed record")
    public void validateDeleteRemovedRecord() throws SQLException {
        UserRequest request = buildUser("db03_" + faker.internet().emailAddress());
        int userId = createUser(request);

        Response deleteResponse = userService.deleteUser(userId);
        deleteResponse.then().statusCode(200);

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(1) AS total FROM users WHERE id = ?"
             )) {
            DbUtils.bindParams(statement, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assert.assertTrue(resultSet.next());
                Assert.assertEquals(resultSet.getInt("total"), 0, "Expected hard delete to remove row");
            }
        }
    }

    @Test(description = "DB_04 - Verify audit_log inserted correctly")
    public void validateAuditLogAfterUpdate() throws SQLException {
        UserRequest request = buildUser("db04_" + faker.internet().emailAddress());
        int userId = createUser(request);

        request.setCountry("Brazil");
        Response updateResponse = userService.updateUser(userId, request);
        updateResponse.then().statusCode(200);

        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT action, user_id FROM audit_logs WHERE user_id = ? ORDER BY timestamp DESC LIMIT 1"
             )) {
            DbUtils.bindParams(statement, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                Assert.assertTrue(resultSet.next(), "Expected audit log row for updated user");
                Assert.assertEquals(resultSet.getString("action"), "UPDATE");
                Assert.assertEquals(resultSet.getInt("user_id"), userId);
            }
        }
    }

    @Test(description = "DB_05 - Verify email uniqueness constraint")
    public void validateEmailUniquenessConstraint() throws SQLException {
        String existingEmail = "existing@email.com";
        DbUtils.executeUpdate("DELETE FROM users WHERE email = ?", existingEmail);

        UserRequest request = buildUser(existingEmail);
        createUser(request);

        String sql = "INSERT INTO users (first_name, last_name, email, gender, country, created_at, updated_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection connection = DbUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            DbUtils.bindParams(statement, "Clone", "User", existingEmail, "Male", "USA");
            statement.executeUpdate();
            Assert.fail("Expected unique constraint violation but insert succeeded");
        } catch (SQLException exception) {
            Assert.assertEquals(exception.getSQLState(), "23505", "Expected PostgreSQL unique violation state 23505");
        }
    }
}
