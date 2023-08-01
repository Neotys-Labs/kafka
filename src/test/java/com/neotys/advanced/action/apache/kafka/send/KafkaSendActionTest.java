package com.neotys.advanced.action.apache.kafka.send;


import org.apache.kafka.common.header.Header;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.neotys.advanced.action.apache.kafka.send.KafkaSendActionEngine.KafkaHeaderParser.parseHeadersOption;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KafkaSendActionTest {
    @Test
    public void shouldReturnType() {
        final KafkaSendAction action = new KafkaSendAction();
        assertEquals("KafkaSend", action.getType());
    }


    @Test
    public void givenValidHeaderWhenParsedThenNoneDiscarded() {
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
    public void givenValidAndInvalidHeaderWhenParsedThenNoneDiscarded() {
        //given
        String[] mixed = {

                "=",//invalid
                "1key=value1",//valid
                "2key=value2",//valid
                "key=",//invalid
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
    public void givenInvalidHeaderWhenParsedThenAllDiscarded() {
        //given
        String[] invalids = {
                "key=",
                "=Value",
                "="
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
