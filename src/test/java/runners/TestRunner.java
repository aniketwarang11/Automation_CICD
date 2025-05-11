package runners;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import CapabilityManager.CapabilityManager;
import CapabilityOptions.CapabilityOptions;
import Utility.PropertyReader;

import static io.cucumber.junit.platform.engine.Constants.*;

import java.util.Properties;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "StepDef,hooks")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,json:target/cucumber-reports/Cucumber.json,Utility.ITestStepFinished")
@ConfigurationParameter(key = PARALLEL_CONFIG_FIXED_PARALLELISM_PROPERTY_NAME, value = "2")
@ConfigurationParameter(key = PARALLEL_CONFIG_FIXED_MAX_POOL_SIZE_PROPERTY_NAME, value = "2")
@ConfigurationParameter(key = PARALLEL_EXECUTION_ENABLED_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@smoke11 or @smoke12")
@ConfigurationParameter(key = SNIPPET_TYPE_PROPERTY_NAME, value = "camelcase")
@CapabilityOptions(configName = "config-web-chrome")

public class TestRunner {

	@BeforeAll
	public static void setupAll() {
		CapabilityManager.setCapabilities(TestRunner.class);
	}

	@Test
	public void testSomething() {
		Properties props = PropertyReader.getProperties();
//        System.out.println("Browser: " + props.getProperty("browser"));
	}

}
