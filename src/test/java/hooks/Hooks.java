package hooks;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

import Utility.Setup;
import context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks extends Setup {

	@Before
	public void startTest(Scenario scenario) throws Exception {
		System.out.println("Scenario: " + scenario.getName() + " | Thread: " + Thread.currentThread().getId());
		getContext().currentScenarioName = scenario.getName();
		setScenario(scenario);

		Set<String> TotalScenario = ConcurrentHashMap.newKeySet();
		TotalScenario.add(scenario.getId());
		System.out.println("Total Scenarios : " + TotalScenario.size());
		RemoteWebDriver driver = new ChromeDriver();
		setDriver(driver);

	}

	@After
	public void afterScenario(Scenario scenario) throws Throwable {
		Setup.getContext().getSoftAssertions().assertAll();
		if (!ScenarioContext.getStepErrors().isEmpty()) {
			ScenarioContext.saveStepErrorsToFile();
		}

		try {
			BufferedImage image = Shutterbug.shootPage(Setup.getDriver(), Capture.FULL_SCROLL).getImage();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			Setup.getScenario().attach(outputStream.toByteArray(), "image/png", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@AfterAll
	public static void runPostProcessor() throws Exception {
		Utility.PostProcessor.main(new String[] {});
	}
}
