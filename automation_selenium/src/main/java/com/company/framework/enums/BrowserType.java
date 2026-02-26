package com.company.framework.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BrowserType {
    /**
     * Chrome browser
     */
    CHROME("Chrome"),

    /**
     * Firefox browser
     */
    FIREFOX("Firefox"),

    /**
     * Edge browser
     */
    EDGE("Edge"),

    /**
     * Safari browser
     */
    SAFARI("Safari");

    private final String value;

    public static BrowserType fromString(String browserType) {
        if (browserType == null) {
            throw new IllegalArgumentException("BrowserType cannot be null");
        }
        for (BrowserType type : BrowserType.values()) {
            if (type.value.equalsIgnoreCase(browserType.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("BrowserType không hợp lệ: " + browserType);
    }
}

