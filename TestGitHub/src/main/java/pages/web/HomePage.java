package pages.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

public class HomePage extends W_BasePage {

	private By headerPageTxt = By.cssSelector("#js-pjax-container > div.shelf.intro-shelf.js-notice > div > div > h2");
	public HomePage(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
		// TODO Auto-generated constructor stub
	}
	
public boolean verifyHomePageText() {
	WebElement element = wdriver.findElement(headerPageTxt);
	String pageText ="Learn Git and GitHub without any code!";
	Reporter.log(element.getText(), true);
	return element.getText().contains(pageText);
}
}