package app.mongodb.kitchensink.controller;

import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.repository.MemberRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.0"));
    private static MongoClient mongoClient;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    public static void setUpAll() {
        mongoDBContainer.start();
        String mongoUri = mongoDBContainer.getConnectionString();
        mongoClient = MongoClients.create(mongoUri);
    }

    @AfterAll
    public static void cleanup() {
        mongoClient.close();
        mongoDBContainer.stop();
    }

    @BeforeEach
    public void setUp() {
        mongoClient.getDatabase("kitchensink").drop();
    }

    @Test
    public void testListAllMembers() throws Exception {
        insertTestData();
        String expected_result = format("""
                [{"id":"0","name":"Chris Holiday","email":"chris.holiday@test.com","phoneNumber":"0987654321"},\
                {"id":"2","name":"John Doe","email":"john.doe@test.com","phoneNumber":"1234567890"},\
                {"id":"1","name":"Sally Smith","email":"sally.smith@test.com","phoneNumber":"2125551212"}]\
                """);

        given().port(port).contentType(ContentType.JSON)
                .when()
                .get(format("/kitchensink/rest/members"))
                .then()
                .statusCode(is(HttpStatus.OK.value()))
                .body(is(expected_result));
    }

    @Test
    public void testRegister() throws Exception {
        Member member = Member.builder()
                .name("Jane Doe")
                .email("jane@mailinator.com")
                .phoneNumber("2125551234").build();

        given().port(port).contentType(ContentType.JSON)
                .body(member)
                .when()
                .post(format("/kitchensink/rest/members"))
                .then()
                .statusCode(is(HttpStatus.OK.value()));
    }

    @Test
    public void testLookupMemberById() throws Exception {
        insertTestData();
        String expected_result = format("""
                {"id":"0","name":"Chris Holiday","email":"chris.holiday@test.com","phoneNumber":"0987654321"}\
                """);

        given().port(port).contentType(ContentType.JSON)
                .when()
                .get(format("/kitchensink/rest/members/0"))
                .then()
                .statusCode(is(HttpStatus.OK.value()))
                .body(is(expected_result));
    }

    private void insertTestData() {
        memberRepository.insert(Member.builder().id("0").name("Chris Holiday").email("chris.holiday@test.com").phoneNumber("0987654321").build());
        memberRepository.insert(Member.builder().id("1").name("Sally Smith").email("sally.smith@test.com").phoneNumber("2125551212").build());
        memberRepository.insert(Member.builder().id("2").name("John Doe").email("john.doe@test.com").phoneNumber("1234567890").build());
    }

}
