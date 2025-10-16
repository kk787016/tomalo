package com.jjp.tomalo.domain.profiles;

public enum IncomeRange {
    RANGE_1("~3000만원"),
    RANGE_2("3000~5000만원"),
    RANGE_3("5000~7000만원"),
    RANGE_4("7000~1억원"),
    RANGE_5("1억원 이상");

    private final String description;

    IncomeRange(String description) {
        this.description = description;
    }
}