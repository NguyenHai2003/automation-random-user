package com.automation.domain.user.service;

import com.automation.models.user.UserRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class UserService {

    private static final String API_USERS_ENDPOINT = "/api/users";
    private static final String API_USER_ID_ENDPOINT = "/api/users/{id}";
    private static final String API_RANDOM_USERS_ENDPOINT = "/api/random-users";

    public Response getApiInfo() {
        return RestAssured.given()
                .when()
                .get("/");
    }

    public Response createUser(UserRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(API_USERS_ENDPOINT);
    }
    
    public Response createUserWithoutBody() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .post(API_USERS_ENDPOINT);
    }

    public Response getUser(int id) {
        return RestAssured.given()
                .pathParam("id", id)
                .when()
                .get(API_USER_ID_ENDPOINT);
    }
    
    public Response getUserStringId(String id) {
        return RestAssured.given()
                .pathParam("id", id)
                .when()
                .get(API_USER_ID_ENDPOINT);
    }

    public Response getAllUsers() {
        return RestAssured.given()
                .when()
                .get(API_USERS_ENDPOINT);
    }
    
    public Response getAllUsersWithQueryParams(Map<String, String> queryParams) {
        return RestAssured.given()
                .queryParams(queryParams)
                .when()
                .get(API_USERS_ENDPOINT);
    }
    
    public Response searchUsers(String searchQuery) {
         return RestAssured.given()
                .queryParam("search", searchQuery)
                .when()
                .get(API_USERS_ENDPOINT);
    }

    public Response updateUser(int id, UserRequest request) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(request)
                .when()
                .put(API_USER_ID_ENDPOINT);
    }

    public Response deleteUser(int id) {
        return RestAssured.given()
                .pathParam("id", id)
                .when()
                .delete(API_USER_ID_ENDPOINT);
    }

    public Response getRandomUsers(int results) {
        return RestAssured.given()
                .queryParam("results", results)
                .when()
                .get(API_RANDOM_USERS_ENDPOINT);
    }
    
     public Response getRandomUsersNoParams() {
        return RestAssured.given()
                .when()
                .get(API_RANDOM_USERS_ENDPOINT);
    }
}
