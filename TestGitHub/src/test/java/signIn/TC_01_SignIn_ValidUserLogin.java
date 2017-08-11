
package signIn;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.web.HomePage;
import pages.web.SignInPage;
import pages.web.W_BasePage;
import tests.web.W_BaseTest;
public class TC_01_SignIn_ValidUserLogin extends W_BaseTest {

	
	// private String uniqueValue = "WebSignUp001";
	
	private W_BasePage bp;
	private SignInPage sp;
	
	private HomePage hp;
	@Test
	public void verifyHomePage() throws Exception {
		bp = new W_BasePage(wdriver);
		sp = new SignInPage(wdriver);
		hp=sp.continueSignIn("sachin.9738474861@gmail.com", "s9738474861");
		Assert.assertTrue(hp.verifyHomePageText(), "Page text not matching");
	}
}
