package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.config.manager.api.BaseIntegrationTest;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringRunner.class)
public class BuildConfigApiTest extends BaseIntegrationTest {

    @Autowired
    private BuildConfigRepository buildConfigRepository;

    @Test
    public void shouldGetOneBuildConfigForEnvironment() {
        given()
            .port(serverPort)
        .when()
            .get("/buildconfigs/as")
        .then()
            .assertThat()
                .statusCode(equalTo(NOT_FOUND.value()));
    }
        
    @Test
    public void shouldGetOneBuildConfig() throws Exception {
        //Given
        final BuildConfig buildConfig = buildConfigBuilder().build();
        buildConfigRepository.save(buildConfig);

        given()
            .port(serverPort)
        .when()
            .get(String.format("/buildconfigs/%s", buildConfig.getEnvironment()))
        .then()
            .assertThat()
                .statusCode(equalTo(OK.value()));
    }

    @Test
    public void shouldCloneBuildConfig() throws Exception {
        //Given
        final BuildConfig buildConfig = buildConfigBuilder().build();
        buildConfigRepository.save(buildConfig);

        given()
            .port(serverPort)
            .body(ImmutableMap.of(BuildConfigController.SOURCE_ENV_PARAM, buildConfig.getEnvironment(), BuildConfigController.TARGET_ENV_PARAM, string(15, CharacterSetFilter.LettersAndDigits).next()))
            .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
            .post("/buildconfigs/clone")
        .then()
            .assertThat()
                .statusCode(equalTo(CREATED.value()));
    }
}
