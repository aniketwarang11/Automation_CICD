package Utility;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.remote.RemoteWebDriver;

import context.ScenarioContext;
import io.cucumber.java.Scenario;

public class Setup {
	
	private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
	private static final ThreadLocal<ScenarioContext> context = ThreadLocal.withInitial(ScenarioContext::new);
	private static ThreadLocal<Scenario> scenario = new ThreadLocal<>();
	
	

    public static ScenarioContext getContext() {
        return context.get();
    }
    
    public static Scenario getScenario() {
        return scenario.get();
    }
    
    public static void setScenario(Scenario currentScenario) {
    	scenario.set(currentScenario);
    }

    public static void removeContext() {
        context.remove();
    }

    public static RemoteWebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(RemoteWebDriver webDriver) {
        driver.set(webDriver);
    }

    public static void quitDriver() {
        driver.get().quit();
        driver.remove();
    }

}
