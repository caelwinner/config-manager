package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class BuildConfigServiceTest {

    private BuildConfigService buildConfigService;

    @Mock
    private BuildConfigRepository buildConfigRepository;

    @Before
    public void testee() {
        buildConfigService = new BuildConfigService(buildConfigRepository);
    }

    @Test
    public void shouldGetOneBuildConfig() throws Exception {
        //Given
        final String env = string().next();

        //And
        Optional<BuildConfig> expectedBuildConfig = Optional.of(buildConfigBuilder()
                .environment(env)
                .build());
        given(buildConfigRepository.findOne(env)).willReturn(expectedBuildConfig);

        //When
        final Optional<BuildConfig> result = buildConfigService.getOne(env);

        //Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get()).isEqualTo(expectedBuildConfig.get());
    }

    @Test
    public void shouldNotFoundBuildConfigWhenEnvironmentIsNotValid() throws Exception {
        //Given
        final String env = string().next();

        //And
        given(buildConfigRepository.findOne(env)).willReturn(Optional.empty());

        //When
        final Optional<BuildConfig> result = buildConfigService.getOne(env);

        //Then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldCloneBuildConfig() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();

        //And
        final BuildConfig sourceBuildConfig = buildConfigBuilder().build();
        given(buildConfigRepository.findOne(sourceEnv)).willReturn(Optional.of(sourceBuildConfig));

        //And
        given(buildConfigRepository.findOne(targetEnv)).willReturn(Optional.empty());

        //When
        final BuildConfig targetBuildConfig = buildConfigService.cloneBuildConfig(sourceEnv, targetEnv);

        //Then
        sourceBuildConfig.setEnvironment(targetEnv);
        assertThat(targetBuildConfig).isNotNull();
        assertThat(targetBuildConfig).isEqualTo(sourceBuildConfig);
        verify(buildConfigRepository).save(sourceBuildConfig);
    }
}