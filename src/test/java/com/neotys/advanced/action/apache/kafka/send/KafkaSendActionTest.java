package com.neotys.advanced.action.apache.kafka.send;


import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.neotys.advanced.action.apache.kafka.send.KafkaSendActionEngine.KafkaHeaderParser.parseHeadersOption;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KafkaSendActionTest {
    @Test
    void shouldReturnType() {
        final KafkaSendAction action = new KafkaSendAction();
        assertEquals("KafkaSend", action.getType());
    }

    @Test
    void givenNullHeaderWhenParsedThenEmptySet() {
        //given
        String nullHeader = null;
        //when
        Set<Header> headers = parseHeadersOption(nullHeader);
        //then
        assertEquals(0, headers.size());
    }

    @Test
    void givenValidHeaderWhenParsedThenNoneDiscarded() {
        //given
        String[] valids = {
                "1key=value1",
                "2key=value2",
                "3key=value\\,value3",
                "4key=value4",
                "5key\\=value\\,value\\,key=value5",
                "6key=value6"
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", valids));
        //then
        assertEquals(valids.length, headers.size());
        assertTrue(headers.stream().allMatch(this::firstKeyCharMatchesLastValueChar));
    }

    @Test
    void givenValidAndInvalidHeadersWhenParsedThenOnlyValidPreserved() {
        //given
        String[] mixed = {

                "=",//invalid
                "1key=value1",//valid
                "2key=value2",//valid
                "3key=value\\,value3", //valid
                "4key=value4",//valid
                "=Value",//invalid
                "5key\\=value\\,value\\,key=value5", //valid
                "6key=value6", // valid
                "",//invalid
                "7key\\=\\,=value\\=\\,7", // valid
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", mixed));

        //then
        assertEquals(7, headers.size());
        assertTrue(headers.stream().allMatch(this::firstKeyCharMatchesLastValueChar));

    }


    @Test
    void givenRepetitiveKeyWhenParsedThenOnlyLastIsKept() {
        //given
        String expectedValue = "value4";
        String[] valids = {
                "key1=value0",
                "key1=  value1",
                " key1 = value2",
                " key1= value3",
                "key1 = " + expectedValue
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", valids));
        //Then
        assertEquals(1, headers.size());
        assertEquals(expectedValue, new String(headers.iterator().next().value()));
    }

    @Test
    void givenWhiteSpacesInKeyValueWhenParsedThenTrimmed() {
        //given
        String nonEmptyValue = "value";
        String[] valids = {
                "key1=  ",
                " key2 = " + nonEmptyValue + " "
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", valids));
        //Then
        assertEquals(2, headers.stream().filter(header -> header.key().length() == 4).count());
        assertEquals(nonEmptyValue.length(), headers.stream().mapToInt(header -> header.value().length).sum());
    }

    @Test
    void givenValidKeyWithEmptyValueWhenParsedThenValid() {
        //given
        String[] valids = {
                "key1=",
                "key2=  ",
                " key3 =    "
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", valids));
        //Then
        assertEquals(3, headers.size());
    }

    @Test
    void givenInvalidHeaderWhenParsedThenAllDiscarded() {
        //given
        String[] invalids = {
                "=Value",
                "=",
                " = ",
                "   =blankKey",
                "noKeyValueDelimiter",
                ",",
                "===============",
                ",,",
                ",=",
                "==",
                "=,"
        };
        //when
        Set<Header> headers = parseHeadersOption(String.join(",", invalids));
        //Then
        assertEquals(0, headers.size());
    }

    private boolean firstKeyCharMatchesLastValueChar(Header header) {
        return (byte) header.key().charAt(0) == header.value()[header.value().length - 1];
    }
}
