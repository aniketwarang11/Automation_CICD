package StepDef;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import Utility.Setup;
import context.ScenarioContext;
import io.cucumber.java.en.Given;

public class stepDefinations extends Setup {
	RemoteWebDriver driver ;
	
    @Given("this is a placeholder")
    public void this_is_a_placeholder() throws InterruptedException {
    	Thread.sleep(2000);
        getDriver().manage().window().maximize();
        getDriver().get("https://cucumber.io/docs");
    }
    
    @Given("verify the header {string}")
    public void verifyTheHeader( String header) throws InterruptedException {
    	Thread.sleep(2000);
        WebElement element = getDriver().findElement(By.xpath("//h2[@id='what-is-cucumber']"));
        String actual = element.getText();
        String expected = header;

        getContext().getSoftAssertions().assertThat(actual)
                              .as("Home tab Header mismatch")
                              .isEqualTo(expected);
        
        try {
            Setup.getContext().getSoftAssertions().assertAll();
        } catch (Throwable t) {
            byte[] screenshot = ((TakesScreenshot) Setup.getDriver()).getScreenshotAs(OutputType.BYTES);
            Setup.getScenario().attach(screenshot, "image/png", "Soft Assertion Failure");
//            throw t; // or continue based on logic
        }
    }	

    @Given("click on learn tab")
    public void clickOnLearnTab() throws InterruptedException {
    	Thread.sleep(2000);
    	WebElement element  = getDriver().findElement(By.xpath("//a[text()='Learn']"));
    	element.click();
    }
    
    @Given("verify the learn tab header")
    public void verifyTheLearnTabHeader() throws InterruptedException {
    	Thread.sleep(2000);
    	//getContext().currentStepName = "verify the learn tab header";  // <-- Manually set step name
        WebElement element = getDriver().findElement(By.xpath("//h1"));
        String actual = element.getText();
        String expected = "Learn BDD and Cucumber";

        getContext().getSoftAssertions().assertThat(actual)
                              .as("Learn Tab Header mismatch")
                              .isEqualTo(expected);
        
        try {
        Setup.getContext().getSoftAssertions().assertAll();
    } catch (Throwable t) {
        byte[] screenshot = ((TakesScreenshot) Setup.getDriver()).getScreenshotAs(OutputType.BYTES);
        Setup.getScenario().attach(screenshot, "image/png", "Soft Assertion Failure");
//        throw t; // or continue based on logic
    }
    }

    @Given("click on Blog")
    public void clickOnBlog() throws Throwable {
    	Thread.sleep(2000);
    	WebElement element  = getDriver().findElement(By.xpath("//a[text()='Blog']"));
    	element.click();
//    	throw new Exception("failed");
    }
}
