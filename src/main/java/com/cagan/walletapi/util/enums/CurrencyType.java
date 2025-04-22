package com.cagan.walletapi.util.enums;

import java.util.Arrays;
import java.util.Objects;

public enum CurrencyType {
    TRY, USD, EUR;

    public static CurrencyType fromValue(String type) {
        if (type == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.toString(), type.toUpperCase()))
                .findFirst().orElse(null);
    }
}
