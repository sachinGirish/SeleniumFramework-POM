
package signIn;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.web.HomePage;
import pages.web.SignInPage;
import pages.web.W_BasePage;
import tests.web.W_BaseTest;
import utils.JavaUtils;
public class TC_02_SignIn_InValidUserLoginFail extends W_BaseTest {

	
	// private String uniqueValue = "WebSignUp001";
	
	private W_BasePage bp;
	private SignInPage sp;
	
	private HomePage hp;
	@Test
	public void verifyHomePage() throws Exception {
		bp = new W_BasePage(wdriver);
		//wdriver = bp.launchWebApp()
		sp = new SignInPage(wdriver);
		hp=sp.continueSignIn("sachin@gmail.com", "grr");
		Assert.assertTrue(hp.verifyHomePageText(), "Page text not matching");
	}
}
