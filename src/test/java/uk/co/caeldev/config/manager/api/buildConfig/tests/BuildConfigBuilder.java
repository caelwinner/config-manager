package uk.co.caeldev.config.manager.api.buildConfig.tests;

import uk.co.caeldev.config.manager.api.buildConfig.BuildConfig;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import java.util.Map;

import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.string;

public class BuildConfigBuilder {

    private String environment = string(5, CharacterSetFilter.LettersOnly).next();
    private Map<String, String> attributes = map(string(15, CharacterSetFilter.LettersOnly), string(15, CharacterSetFilter.LettersOnly)).next();

    BuildConfigBuilder() {
    }

    public static BuildConfigBuilder buildConfigBuilder() {
        return new BuildConfigBuilder();
    }

    public BuildConfig build() {
        BuildConfig buildConfig = new BuildConfig();
        buildConfig.setAttributes(attributes);
        buildConfig.setEnvironment(environment);
        return buildConfig;
    }

    public BuildConfigBuilder environment(String environment) {
        this.environment = environment;
        return this;
    }

    public BuildConfigBuilder attributes(final Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }
}
