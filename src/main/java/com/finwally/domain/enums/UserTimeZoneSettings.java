package com.finwally.domain.enums;

import lombok.Getter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum UserTimeZoneSettings {
    UTC("UTC", "en", "USD"),
    EUROPE_MOSCOW("Europe/Moscow", "ru", "RUB"),
    EUROPE_LONDON("Europe/London", "en", "GBP"),
    EUROPE_BERLIN("Europe/Berlin", "de", "EUR"),
    EUROPE_PARIS("Europe/Paris", "fr", "EUR"),
    EUROPE_ROME("Europe/Rome", "it", "EUR"),
    EUROPE_MADRID("Europe/Madrid", "es", "EUR"),
    EUROPE_WARSAW("Europe/Warsaw", "pl", "PLN"),
    EUROPE_KIEV("Europe/Kiev", "uk", "UAH"),
    AMERICA_NEW_YORK("America/New_York", "en", "USD"),
    AMERICA_CHICAGO("America/Chicago", "en", "USD"),
    AMERICA_DENVER("America/Denver", "en", "USD"),
    AMERICA_LOS_ANGELES("America/Los_Angeles", "en", "USD"),
    AMERICA_TORONTO("America/Toronto", "en", "CAD"),
    AMERICA_MEXICO_CITY("America/Mexico_City", "es", "MXN"),
    AMERICA_SAO_PAULO("America/Sao_Paulo", "pt", "BRL"),
    AMERICA_ARGENTINA_BUENOS_AIRES("America/Argentina/Buenos_Aires", "es", "ARS"),
    ASIA_TOKYO("Asia/Tokyo", "ja", "JPY"),
    ASIA_SHANGHAI("Asia/Shanghai", "zh", "CNY"),
    ASIA_HONG_KONG("Asia/Hong_Kong", "zh", "HKD"),
    ASIA_SINGAPORE("Asia/Singapore", "en", "SGD"),
    ASIA_SEOUL("Asia/Seoul", "ko", "KRW"),
    ASIA_DUBAI("Asia/Dubai", "ar", "AED"),
    ASIA_KOLKATA("Asia/Kolkata", "hi", "INR"),
    ASIA_ALMATY("Asia/Almaty", "kk", "KZT"),
    ASIA_BANGKOK("Asia/Bangkok", "th", "THB"),
    ASIA_ISTANBUL("Europe/Istanbul", "tr", "TRY"),
    AUSTRALIA_SYDNEY("Australia/Sydney", "en", "AUD"),
    AUSTRALIA_MELBOURNE("Australia/Melbourne", "en", "AUD"),
    AFRICA_JOHANNESBURG("Africa/Johannesburg", "en", "ZAR"),
    AFRICA_CAIRO("Africa/Cairo", "ar", "EGP"),
    AFRICA_LAGOS("Africa/Lagos", "en", "NGN");

    private final String timezone;
    private final String locale;
    private final String baseCurrencyCode;

    UserTimeZoneSettings(String timezone, String locale, String baseCurrencyCode) {
        this.timezone = timezone;
        this.locale = locale;
        this.baseCurrencyCode = baseCurrencyCode;
    }

    private static final Map<String, UserTimeZoneSettings> BY_TIMEZONE = Arrays.stream(values())
            .collect(Collectors.toMap(UserTimeZoneSettings::getTimezone, Function.identity()));

    public static UserTimeZoneSettings fromTimezone(String timezone) {
        return BY_TIMEZONE.getOrDefault(timezone, UTC);
    }
}
