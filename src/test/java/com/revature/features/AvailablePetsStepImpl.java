package com.revature.features;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AvailablePetsStepImpl {
	public static WebDriver driver;
	public String adoptedPetId;
	
	@BeforeAll
	public static void setupDriver() {
		File file = new File("src/test/resources/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		
		driver = new ChromeDriver();
	}
	
	@Given("the user is on the available pets page")
	public void the_user_is_on_the_available_pets_page() {
	    driver.get("C:\\Users\\SierraNicholes\\Documents\\2111-uta\\2111-nov15-uta\\pet-apps\\pet-app-1\\front-end\\pets.html");
	}

	@Given("the user is logged in")
	public void the_user_is_logged_in() {
		WebElement loginLink = driver.findElement(By.id("login"));
		loginLink.click();
		WebElement usernameInput = driver.findElement(By.id("username"));
		WebElement passwordInput = driver.findElement(By.id("password"));
		usernameInput.sendKeys("rerazo");
		passwordInput.sendKeys("pass");
		WebElement loginBtn = driver.findElement(By.id("loginBtn"));
		loginBtn.click();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(50));
		wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.id("loginForm"), 1));
	}
	
	@When("the available pets page is loaded")
	public void the_available_pets_page_is_loaded() {
	    // worst way: Thread.sleep(5000);
		// bad way: "implicit wait" - WebDriver timeout: driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// ok way: "explicit wait" - WebDriver explicit wait
//		WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(5));
//		explicitWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("tr"), 1));
		
		// best way: "fluent wait"
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(50));
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("tr"), 1));
		
	}

	@Then("the table has pets in it")
	public void the_table_has_pets_in_it() {
	    List<WebElement> tableData = driver.findElements(By.tagName("td"));
	    String text = tableData.get(1).getText();
	    System.out.println(text);
	    
	    assertFalse(tableData.isEmpty());
	}

	@When("the user clicks the first adopt button")
	public void the_user_clicks_the_first_adopt_button() {
	    WebElement adoptBtn = driver.findElement(By.xpath("//*[@id=\"availablePets\"]/tr[1]/td[6]/button"));
	    adoptedPetId = adoptBtn.getAttribute("id");
	    System.out.println("ADOPTED PET ID: " + adoptedPetId);
	    adoptBtn.click();
	}

	@Then("the pet is removed from the table")
	public void the_pet_is_removed_from_the_table() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(50));
		wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.id(adoptedPetId), 1));
	    assertThrows(NoSuchElementException.class, () -> {
	    	driver.findElement(By.id(adoptedPetId));
	    });
	}
	
	@AfterAll
	public static void closeDriver() {
		driver.quit();
	}
}
