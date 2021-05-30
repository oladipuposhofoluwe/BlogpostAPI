package com.example.blog.integrated;

import com.example.blog.model.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PostControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createPost() {
        Post post = new Post("This is a test post");

        HttpEntity<Post> request = new HttpEntity<>(post);

        ResponseEntity<?> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/posts/3", request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getPosts() {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/posts", List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getPost() {
        ResponseEntity<Post> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/posts/1", Post.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getPostsByUser() {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/posts/1/posts", List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void updatePost() {
        Post post = new Post("This is an edited test post");

        HttpEntity<Post> request = new HttpEntity<>(post);

        ResponseEntity<?> response =
                this.restTemplate.exchange("http://localhost:" + port + "/posts/1/1", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void deletePost() {

        HttpEntity<Post> request = null;

        ResponseEntity<?> response =
                this.restTemplate.exchange("http://localhost:" + port + "/posts/1/2", HttpMethod.DELETE, request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }
    @Test
    public void likeOrDislikePost() {
        HttpEntity<Post> request = null;
        ResponseEntity<?> response =
                this.restTemplate.postForEntity("http://localhost:" + port + "/posts/3/like/1", request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getLikes() {
        ResponseEntity<?> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/posts/3/likes", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}
