package com.automation.keywords;

import com.automation.globals.ConfigsGlobal;
import com.automation.globals.TokenContext;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class RequestManager {

    private RequestManager() {
    }

    public static RequestSpecification baseSpec() {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(ConfigsGlobal.BASE_URI)
                .setBasePath(ConfigsGlobal.BASE_PATH)
                .setContentType("application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();

        return RestAssured.given().spec(specification);
    }

    public static RequestSpecification authSpec() {
        RequestSpecification requestSpecification = baseSpec();
        if (TokenContext.hasToken()) {
            requestSpecification.cookie("token", TokenContext.getToken());
        }
        return requestSpecification;
    }
}
