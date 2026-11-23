package com.example.qualitylab.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

/** Задание 8: тест сетевого слоя через MockWebServer. */
public class PostsApiTest {

    private MockWebServer server;
    private PostsApi api;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
                .build();
        api = new PostsApi(server.url("/").toString(), client);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void success_returnsPosts_andRequestIsCorrect() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[{\"userId\":1,\"id\":1,\"title\":\"Hello\",\"body\":\"World\"}]"));

        List<Post> posts = api.fetchPosts(10);

        assertEquals(1, posts.size());
        assertEquals("Hello", posts.get(0).title);
        RecordedRequest request = server.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/posts?_limit=10", request.getPath());
    }

    @Test
    public void notFound_throwsApiExceptionWith404() {
        server.enqueue(new MockResponse().setResponseCode(404));
        assertErrorCode(404);
    }

    @Test
    public void serverError_throwsApiExceptionWith500() {
        server.enqueue(new MockResponse().setResponseCode(500));
        assertErrorCode(500);
    }

    @Test
    public void timeout_throwsApiExceptionWithNetworkCode() {
        server.enqueue(new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE));
        assertErrorCode(-1);
    }

    @Test
    public void invalidJson_throwsApiExceptionWithParseCode() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("<html>не json</html>"));
        assertErrorCode(-2);
    }

    @Test
    public void emptyList_isOk() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("[]"));
        assertTrue(api.fetchPosts(5).isEmpty());
    }

    private void assertErrorCode(int expectedCode) {
        try {
            api.fetchPosts(10);
            fail("Ожидалось ApiException");
        } catch (ApiException e) {
            assertEquals(expectedCode, e.getCode());
        }
    }
}
