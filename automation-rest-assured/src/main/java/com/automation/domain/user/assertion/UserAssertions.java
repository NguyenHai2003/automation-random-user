package com.automation.domain.user.assertion;

import com.automation.models.user.ApiMessageResponse;
import com.automation.models.user.UserRequest;
import com.automation.models.user.UserDataResponse;
import com.automation.models.user.UserListResponse;
import com.automation.models.user.UserResponse;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import java.io.File;

public class UserAssertions {

    public static UserResponse assertCreateUserSuccess(Response response, UserRequest request, File schemaFile) {
        response.then().statusCode(201);
        if (schemaFile != null && schemaFile.exists()) {
             response.then().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
        }
        UserDataResponse wrapper = response.as(UserDataResponse.class);
        Assert.assertTrue(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=true");
        Assert.assertNotNull(wrapper.getData(), "Create response data should not be null");
        UserResponse userResponse = wrapper.getData();
        Assert.assertNotNull(userResponse.getId(), "User ID should not be null");
        Assert.assertEquals(userResponse.getFirstName(), request.getFirstName(), "First name mismatch");
        Assert.assertEquals(userResponse.getEmail(), request.getEmail(), "Email mismatch");
        return userResponse;
    }

    public static void assertGetUserSuccess(Response response, UserRequest expectedUser) {
        response.then().statusCode(200);
        UserDataResponse wrapper = response.as(UserDataResponse.class);
        Assert.assertTrue(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=true");
        Assert.assertNotNull(wrapper.getData(), "Get response data should not be null");
        UserResponse userResponse = wrapper.getData();
        Assert.assertEquals(userResponse.getFirstName(), expectedUser.getFirstName(), "First name mismatch");
        Assert.assertEquals(userResponse.getEmail(), expectedUser.getEmail(), "Email mismatch");
    }

    public static void assertUpdateUserSuccess(Response response, UserRequest request) {
        response.then().statusCode(200);
        UserDataResponse wrapper = response.as(UserDataResponse.class);
        Assert.assertTrue(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=true");
        Assert.assertNotNull(wrapper.getData(), "Update response data should not be null");
        UserResponse userResponse = wrapper.getData();
        Assert.assertEquals(userResponse.getFirstName(), request.getFirstName(), "Updated First name mismatch");
        Assert.assertEquals(userResponse.getEmail(), request.getEmail(), "Updated Email mismatch");
    }

    public static void assertDeleteUserSuccess(Response response) {
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Delete should return 200");
        ApiMessageResponse wrapper = response.as(ApiMessageResponse.class);
        Assert.assertTrue(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=true");
        Assert.assertEquals(wrapper.getMessage(), "User deleted successfully", "Delete message mismatch");
    }

    public static void assertErrorResponse(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
        ApiMessageResponse wrapper = response.as(ApiMessageResponse.class);
        Assert.assertFalse(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=false for error responses");
        Assert.assertNotNull(wrapper.getMessage(), "Error message should not be null");
    }
    
    public static void assertGetUsersSuccess(Response response, int expectedMaxSize) {
        response.then().statusCode(200);
        UserListResponse wrapper = response.as(UserListResponse.class);
        Assert.assertTrue(Boolean.TRUE.equals(wrapper.getSuccess()), "Expected success=true");
        Assert.assertNotNull(wrapper.getData(), "Users data should not be null");
        Assert.assertTrue(wrapper.getData().size() <= expectedMaxSize, "Users list exceeded maximum size");
        Assert.assertNotNull(wrapper.getPage(), "Page should not be null");
        Assert.assertNotNull(wrapper.getSize(), "Size should not be null");
        Assert.assertNotNull(wrapper.getTotal(), "Total should not be null");
    }
}
