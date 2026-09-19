package com.sihan.weather;

import java.time.DateTimeException;
import java.time.LocalDate;

final class IsdLiteParser {
    static final int MISSING_TEMPERATURE = -9999;

    private IsdLiteParser() {
    }

    static ParseResult parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            return ParseResult.malformed();
        }

        String[] fields = line.trim().split("\\s+");
        if (fields.length < 5) {
            return ParseResult.malformed();
        }

        try {
            int year = Integer.parseInt(fields[0]);
            int month = Integer.parseInt(fields[1]);
            int day = Integer.parseInt(fields[2]);
            int hour = Integer.parseInt(fields[3]);
            int temperatureTenths = Integer.parseInt(fields[4]);

            if (hour < 0 || hour > 23) {
                return ParseResult.malformed();
            }
            if (temperatureTenths == MISSING_TEMPERATURE) {
                return ParseResult.missingTemperature();
            }

            LocalDate date = LocalDate.of(year, month, day);
            return ParseResult.valid(date.toString(), temperatureTenths);
        } catch (NumberFormatException | DateTimeException exception) {
            return ParseResult.malformed();
        }
    }

    enum Status {
        VALID,
        MISSING_TEMPERATURE,
        MALFORMED
    }

    static final class ParseResult {
        private final Status status;
        private final String date;
        private final int temperatureTenths;

        private ParseResult(Status status, String date, int temperatureTenths) {
            this.status = status;
            this.date = date;
            this.temperatureTenths = temperatureTenths;
        }

        static ParseResult valid(String date, int temperatureTenths) {
            return new ParseResult(Status.VALID, date, temperatureTenths);
        }

        static ParseResult missingTemperature() {
            return new ParseResult(Status.MISSING_TEMPERATURE, null, 0);
        }

        static ParseResult malformed() {
            return new ParseResult(Status.MALFORMED, null, 0);
        }

        Status getStatus() {
            return status;
        }

        String getDate() {
            return date;
        }

        int getTemperatureTenths() {
            return temperatureTenths;
        }
    }
}
