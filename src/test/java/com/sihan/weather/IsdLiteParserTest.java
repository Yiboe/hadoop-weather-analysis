package com.sihan.weather;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IsdLiteParserTest {
    @Test
    void parsesValidNegativeTemperatureWithoutLosingPrecision() {
        IsdLiteParser.ParseResult result = IsdLiteParser.parse(
                "2024 01 01 00  -227  -261 10191   352    13 -9999 -9999 -9999"
        );

        assertEquals(IsdLiteParser.Status.VALID, result.getStatus());
        assertEquals("2024-01-01", result.getDate());
        assertEquals(-227, result.getTemperatureTenths());
    }

    @Test
    void identifiesMissingTemperature() {
        IsdLiteParser.ParseResult result = IsdLiteParser.parse(
                "2024 01 01 03 -9999 -9999 10197 334 14 -9999 -9999 -9999"
        );

        assertEquals(IsdLiteParser.Status.MISSING_TEMPERATURE, result.getStatus());
    }

    @Test
    void rejectsInvalidDateAndShortLines() {
        assertEquals(
                IsdLiteParser.Status.MALFORMED,
                IsdLiteParser.parse("2024 02 30 00 123").getStatus()
        );
        assertEquals(
                IsdLiteParser.Status.MALFORMED,
                IsdLiteParser.parse("2024 01").getStatus()
        );
    }
}
