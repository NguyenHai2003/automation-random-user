package com.automation.listeners;

import com.automation.utils.LogUtils;
import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        LogUtils.info("Test Started: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LogUtils.info("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LogUtils.error("Test Failed: " + result.getName());
        saveTextLog(result.getName() + " failed!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LogUtils.warn("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        LogUtils.info("Starting Test Suite: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        LogUtils.info("Finishing Test Suite: " + context.getName());
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }
}
