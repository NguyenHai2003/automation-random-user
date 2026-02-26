package com.automation.tests.user;

import com.automation.base.BaseTest;
import com.automation.domain.user.assertion.UserAssertions;
import com.automation.domain.user.service.UserService;
import com.automation.models.user.UserRequest;
import com.automation.utils.LogUtils;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UserApiTest extends BaseTest {

    private final UserService userService = new UserService();
    private final Faker faker = new Faker();
    private static final File USER_SCHEMA_FILE = new File("src/test/resources/schemas/user_schema.json");

    private UserRequest generateRandomUserRequest(String emailPrefix) {
        return UserRequest.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(emailPrefix + faker.internet().emailAddress())
                .phone(faker.phoneNumber().cellPhone())
                .address(faker.address().fullAddress())
                .avatar("https://randomuser.me/api/portraits/lego/1.jpg")
                .gender(faker.options().option("male", "female"))
                .country(faker.address().country())
                .registered(Instant.now().toString())
                .build();
    }

    @Test(description = "API_01, API_16, API_03, API_04 - CRUD Flow")
    public void testUserCrudFlow() {
        LogUtils.info("Starting User CRUD Flow");
        UserRequest request = generateRandomUserRequest("crud_");

        // Create
        LogUtils.info("Creating user: " + request.getEmail());
        Response createResponse = userService.createUser(request);
        int userId = UserAssertions.assertCreateUserSuccess(createResponse, request, USER_SCHEMA_FILE).getId();

        // Read (Get User by ID - API_16)
        LogUtils.info("Getting user by ID: " + userId);
        Response getResponse = userService.getUser(userId);
        UserAssertions.assertGetUserSuccess(getResponse, request);

        // Update (API_03)
        LogUtils.info("Updating user ID: " + userId);
        request.setFirstName("UpdatedName");
        request.setEmail("updated_" + faker.internet().emailAddress());
        Response updateResponse = userService.updateUser(userId, request);
        UserAssertions.assertUpdateUserSuccess(updateResponse, request);

        // Delete (API_04)
        LogUtils.info("Deleting user ID: " + userId);
        Response deleteResponse = userService.deleteUser(userId);
        UserAssertions.assertDeleteUserSuccess(deleteResponse);
    }

    @Test(description = "API_02 - Get Users with pagination")
    public void testGetUsers() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "1");
        queryParams.put("size", "10");
        Response response = userService.getAllUsersWithQueryParams(queryParams);
        UserAssertions.assertGetUsersSuccess(response, 10);
    }

    @Test(description = "API_05 - Duplicate Email Registration")
    public void testDuplicateEmail() {
        String staticEmail = "duplicate_" + faker.internet().emailAddress();
        UserRequest request = generateRandomUserRequest("");
        request.setEmail(staticEmail);

        // First creation
        userService.createUser(request).then().statusCode(201);

        // Second creation with same email
        Response conflictResponse = userService.createUser(request);
        LogUtils.info("Duplicate email creation status: " + conflictResponse.getStatusCode());
        UserAssertions.assertErrorResponse(conflictResponse, 400);
    }

    @Test(description = "API_06 - Invalid Email")
    public void testInvalidEmail() {
        UserRequest request = generateRandomUserRequest("");
        request.setEmail("invalid-email-format");
        Response response = userService.createUser(request);
        UserAssertions.assertErrorResponse(response, 400);
    }

    @Test(description = "API_07 - Missing Fields")
    public void testMissingFields() {
        UserRequest request = new UserRequest();
        request.setEmail(faker.internet().emailAddress()); // No first/last name
        Response response = userService.createUser(request);
        UserAssertions.assertErrorResponse(response, 400);
    }

    @Test(description = "API_08 - Pagination Low Boundary")
    public void testPaginationLowBoundary() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "0");
        Response response = userService.getAllUsersWithQueryParams(queryParams);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 400, "Expected 200 or 400 for page=0, but got: " + statusCode);
        if (statusCode == 200) {
            UserAssertions.assertGetUsersSuccess(response, 10);
        } else {
            UserAssertions.assertErrorResponse(response, 400);
        }
    }

    @Test(description = "API_09 - Pagination Max Boundary")
    public void testPaginationMaxBoundary() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "1");
        queryParams.put("size", "1000");
        Response response = userService.getAllUsersWithQueryParams(queryParams);
        int statusCode = response.getStatusCode();
        Assert.assertTrue(statusCode == 200 || statusCode == 400, "Expected 200 or 400 for size=1000, but got: " + statusCode);
        if (statusCode == 200) {
            UserAssertions.assertGetUsersSuccess(response, 1000);
        } else {
            UserAssertions.assertErrorResponse(response, 400);
        }
    }

    @Test(description = "API_10 - SQL Injection in Search")
    public void testSqlInjectionSearch() {
        Response response = userService.searchUsers("' OR 1=1 --");
        response.then().statusCode(200);
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true for sanitized search");
    }

    @Test(description = "API_11 - Non-existing ID")
    public void testGetNonExistingId() {
        Response response = userService.getUser(9999999);
        UserAssertions.assertErrorResponse(response, 404);
    }

    @Test(description = "API_12 - External Connect")
    public void testExternalConnect() {
        Response response = userService.getRandomUsers(1);
        response.then().statusCode(200);
        Assert.assertTrue(response.jsonPath().getBoolean("success"), "Expected success=true");
        Assert.assertEquals(response.jsonPath().getList("data").size(), 1, "Expected exactly 1 random user");
    }

    @Test(description = "API_13 - External Failure")
    public void testExternalFailure() {
        Response response = userService.getRandomUsersNoParams();
        int statusCode = response.getStatusCode();

        if (statusCode == 200) {
            throw new SkipException("External provider is healthy. Enable failure mock/stub to validate API_13.");
        }

        Assert.assertTrue(statusCode == 500 || statusCode == 502 || statusCode == 503,
                "Expected 500/502/503 when external API fails, but got: " + statusCode);
        String message = response.jsonPath().getString("message");
        Assert.assertNotNull(message, "Error message should be present");
        Assert.assertFalse(message.isBlank(), "Error message should not be blank");
    }

    @Test(description = "API_14 - Performance")
    public void testPerformance() {
        Response response = userService.getAllUsers();
        response.then().statusCode(200);
        long time = response.getTime();
        LogUtils.info("Response time: " + time + "ms");
        Assert.assertTrue(time < 500, "Performance degraded: " + time + "ms");
    }

    @Test(description = "API_15 - Get API Info (Root)")
    public void testGetApiInfo() {
        Response response = userService.getApiInfo();
        response.then().statusCode(200);
        String message = response.jsonPath().getString("message");
        Assert.assertNotNull(message, "Root message should not be null");
        Assert.assertTrue(message.toLowerCase().contains("api"), "Root message should mention API");
    }

    @Test(description = "API_17 - Invalid Data Type (Registered date)")
    public void testInvalidDataType() {
        UserRequest request = generateRandomUserRequest("invalid_date_");
        request.setRegistered("not-a-date-string");
        Response response = userService.createUser(request);
        UserAssertions.assertErrorResponse(response, 400);
    }
}
