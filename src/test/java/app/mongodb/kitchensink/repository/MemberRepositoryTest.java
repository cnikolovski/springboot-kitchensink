package app.mongodb.kitchensink.repository;

import app.mongodb.kitchensink.model.Member;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataMongoTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.0"));
    private static MongoClient mongoClient;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

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
    void testSaveMember() {
        Member member = Member.builder().name("Sally").email("sally@gmail.com").phoneNumber("0987667890").build();
        Member savedMember = memberRepository.insert(member);

        Optional<Member> foundSavedMember = memberRepository.findById(savedMember.getId());
        assertTrue(foundSavedMember.isPresent());
    }

    @Test
    void testExistsByEmailWhenNotFound() {
        boolean memberExists = memberRepository.existsByEmail("random");
        assertThat(memberExists).isFalse();
    }

    @Test
    void testExistsByEmail() {
        insertTestData();
        boolean memberExists = memberRepository.existsByEmail("john.doe@test.com");
        assertThat(memberExists).isTrue();
    }

    @Test
    void testFindAllOrderedByName() {
        insertTestData();
        List<Member> retrievedMembers = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        assertThat(retrievedMembers.size()).isEqualTo(3);
        assertThat(retrievedMembers.get(0).getName()).isEqualTo("Chris Holiday");
        assertThat(retrievedMembers.get(1).getName()).isEqualTo("John Doe");
        assertThat(retrievedMembers.get(2).getName()).isEqualTo("Sally Smith");
    }

    private void insertTestData() {
        memberRepository.insert(Member.builder().id("0").name("Chris Holiday").email("chris.holiday@test.com").phoneNumber("0987654321").build());
        memberRepository.insert(Member.builder().id("1").name("Sally Smith").email("sally.smith@test.com").phoneNumber("2125551212").build());
        memberRepository.insert(Member.builder().id("2").name("John Doe").email("john.doe@test.com").phoneNumber("1234567890").build());
    }
}
