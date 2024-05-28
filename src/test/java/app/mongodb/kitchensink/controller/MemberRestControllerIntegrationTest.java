package app.mongodb.kitchensink.controller;

import app.mongodb.kitchensink.model.Member;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    @Sql("/test-add-three-members.sql")
    public void testListAllMembers() throws Exception {
        String expected_result = format("""
            [{"id":0,"name":"Chris Holiday","email":"chris.holiday@test.com","phoneNumber":"0987654321"},\
            {"id":2,"name":"John Doe","email":"john.doe@test.com","phoneNumber":"1234567890"},\
            {"id":1,"name":"Sally Smith","email":"sally.smith@test.com","phoneNumber":"2125551212"}]\
            """);

        given().port(port).contentType(ContentType.JSON)
                .when()
                .get(format("/kitchensink/rest/members/"))
                .then()
                .statusCode(is(HttpStatus.OK.value()))
                .body(is(expected_result));
    }

    @Test
    @Sql("/clean-db-before-test.sql")
    public void testRegister() throws Exception {
        Member member = Member.builder()
                .name("Jane Doe")
                .email("jane@mailinator.com")
                .phoneNumber("2125551234").build();

        given().port(port).contentType(ContentType.JSON)
                .body(member)
                .when()
                .post(format("/kitchensink/rest/members/"))
                .then()
                .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    @Sql("/test-add-three-members.sql")
    public void testLookupMemberById() throws Exception {
        String expected_result = format("""
                {"id":0,"name":"Chris Holiday","email":"chris.holiday@test.com","phoneNumber":"0987654321"}\
                """);

        given().port(port).contentType(ContentType.JSON)
                .when()
                .get(format("/kitchensink/rest/members/0"))
                .then()
                .statusCode(is(HttpStatus.OK.value()))
                .body(is(expected_result));
    }

}

