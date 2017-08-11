package tests;

import org.openqa.selenium.WebDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class Driver {

	static AppiumDriver<MobileElement> mobDriver;
	
	static WebDriver webDriver;
	
	public static WebDriver getWebDriver() {
		return webDriver;
	}

	public static void setWebDriver(WebDriver driver) {
		webDriver = driver;
	}

	public static void setDriver(AppiumDriver<MobileElement> driver){
		mobDriver = driver;
	}
	
	public static AppiumDriver<MobileElement> getDriver(){
		return mobDriver;
	}

}
