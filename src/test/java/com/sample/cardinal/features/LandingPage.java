package com.sample.cardinal.features;


import com.sample.cardinal.locator.WebAppLocator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.*;

public class LandingPage {
    private WebDriver driver;


    @Before
    public void setup() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            if (WebAppLocator.shouldRunHeadless())
                options.setHeadless(true);
            else
                options.setHeadless(false);

            driver = new ChromeDriver(options);
        }

    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Given("navigate to landing page")
    public void navigate_to_landing_page() {
        driver.navigate().to(WebAppLocator.locateWebAppURL());
    }

    @Then("page should be visible")
    public void page_should_be_visible() {
        assertThat(driver.getTitle(), is("Hello World"));
    }

}

