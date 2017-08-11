package pages.web;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

public class SignInPage extends W_BasePage {

	public SignInPage(WebDriver wdriver) {
		super(wdriver);
		PageFactory.initElements(wdriver, this);
		// TODO Auto-generated constructor stub
	}

	@FindBy(name = "commit")
	private WebElement signIn_Button;

	@FindBy(name = "password")
	private WebElement password_TB;

	@FindBy(name = "login")
	private WebElement login_TB;

	@FindBy(css = "#js-flash-container > div > div")
	private WebElement errorMsg_Txt;

	public HomePage continueSignIn(String user, String password) throws IOException {

		waitUntilElementclickable(signIn_Button);

		// providing login information
		login_TB.clear();
		login_TB.sendKeys(user);
		password_TB.clear();
		password_TB.sendKeys(password);
		signIn_Button.click();
		return new HomePage(wdriver);
	}

	public String getErrorMessage() {
		String strErrorMsg = null;

		if (errorMsg_Txt.isDisplayed() && errorMsg_Txt.isEnabled())
			strErrorMsg = errorMsg_Txt.getText();
		Reporter.log(strErrorMsg, true);
		return strErrorMsg;
	}
}
