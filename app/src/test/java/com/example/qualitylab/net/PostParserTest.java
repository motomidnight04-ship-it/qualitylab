package com.example.qualitylab.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.List;

public class PostParserTest {

    @Test
    public void parsesArrayOfPosts() throws Exception {
        String json = "[{\"userId\":1,\"id\":1,\"title\":\"Первый\",\"body\":\"Текст\"},"
                + "{\"userId\":1,\"id\":2,\"title\":\"Второй\",\"body\":\"Ещё\"}]";

        List<Post> posts = PostParser.parse(json);

        assertEquals(2, posts.size());
        assertEquals("Первый", posts.get(0).title);
        assertEquals(2, posts.get(1).id);
    }

    @Test
    public void emptyArray_isEmptyList() throws Exception {
        assertTrue(PostParser.parse("[]").isEmpty());
    }

    @Test
    public void brokenJson_throwsApiException() {
        try {
            PostParser.parse("это не json {{");
            fail("Ожидалось ApiException");
        } catch (ApiException e) {
            assertEquals(-2, e.getCode());
        }
    }

    @Test
    public void objectInsteadOfArray_throwsApiException() {
        try {
            PostParser.parse("{\"id\":1}");
            fail("Ожидалось ApiException");
        } catch (ApiException e) {
            assertEquals(-2, e.getCode());
        }
    }

    @Test
    public void nullJson_throwsApiException() {
        try {
            PostParser.parse("null");
            fail("Ожидалось ApiException");
        } catch (ApiException e) {
            assertEquals(-2, e.getCode());
        }
    }
}
