package com.automation.keywords;

import com.automation.utils.LogUtils;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

public class ApiKeyword {

    @Step("Send GET request to {0}")
    public static Response get(String path) {
        LogUtils.info("GET Request: " + path);
        return RequestManager.baseSpec().get(path);
    }

    @Step("Send GET request to {0} with params {1}")
    public static Response get(String path, Map<String, Object> params) {
        LogUtils.info("GET Request: " + path + " with params: " + params);
        return RequestManager.baseSpec().queryParams(params).get(path);
    }

    @Step("Send POST request to {0} with body")
    public static Response post(String path, Object body) {
        LogUtils.info("POST Request: " + path);
        return RequestManager.baseSpec().body(body).post(path);
    }

    @Step("Send POST request with auth to {0} with body")
    public static Response postWithAuth(String path, Object body) {
        LogUtils.info("POST Request (auth): " + path);
        return RequestManager.authSpec().body(body).post(path);
    }

    @Step("Send PUT request to {0} with body")
    public static Response put(String path, Object body) {
        LogUtils.info("PUT Request: " + path);
        return RequestManager.authSpec().body(body).put(path);
    }

    @Step("Send PATCH request to {0} with body")
    public static Response patch(String path, Object body) {
        LogUtils.info("PATCH Request: " + path);
        return RequestManager.authSpec().body(body).patch(path);
    }

    @Step("Send DELETE request to {0}")
    public static Response delete(String path) {
        LogUtils.info("DELETE Request: " + path);
        return RequestManager.authSpec().delete(path);
    }
}
