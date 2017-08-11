package pages.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.google.common.collect.ImmutableMap;

import utils.JavaUtils;

public class W_SuperBasePage extends JavaUtils{

	public WebDriver wdriver;

	public W_SuperBasePage(WebDriver wdriver) {
		this.wdriver = wdriver;
	}

	public WebDriver launchWebApp() throws IOException
	{
		String dir = System.getProperty("user.dir");
		String domain = getPropValue("domain");
		String url = null;
		if(domain.equalsIgnoreCase("test"))
			url = getPropValue("testAppUrl");
		else if(domain.equalsIgnoreCase("dev"))
			url = getPropValue("devAppUrl");
		
		String browser = getPropValue("browser");
		if (browser.equalsIgnoreCase("ff")) 
		{
			wdriver = new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("headlesschrome"))
		{
			ChromeDriverService service = new ChromeDriverService.Builder()
			        .usingDriverExecutable((new File("./drivers/chromedriver.exe")))
			        .usingPort(1131)
			        .withEnvironment(ImmutableMap.of("DISPLAY", ":1"))
			        .build();
			service.start();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--remote-debugging-port=1131");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability(ChromeOptions.CAPABILITY,options);
			wdriver = new RemoteWebDriver(service.getUrl(),caps);
		}
		else if (browser.equalsIgnoreCase("chrome")) 
		{
			System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("chrome.switches","--disable-extensions");
			options.addArguments("chrome.switches","--disable-geolocation");
			wdriver = new ChromeDriver(options);
		}else if (browser.equalsIgnoreCase("phantomjs"))
		{
//			String userAgent = "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1";
//			System.setProperty("phantomjs.page.settings.userAgent", userAgent);
			DesiredCapabilities caps = new DesiredCapabilities();
	        caps.setJavascriptEnabled(true);
	        caps.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
	        caps.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
	        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
	                "--web-security=false",
	                "--ssl-protocol=any",
	                "--ignore-ssl-errors=true",
	                "--webdriver-loglevel=INFO"
	            });
			System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "./drivers/phantomjs.exe");
			wdriver = new PhantomJSDriver(caps);
		}

			wdriver.get(url+"/login");
			wdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			wdriver.manage().window().maximize();
			Reporter.log("Launched Url: "+wdriver.getCurrentUrl(), true);
			return wdriver;
		}

		public void writePagesourceToFile(String text) throws IOException{
			if(!new File("./pagesource.html").exists())
				new File("./pagesource.html").delete();
			FileOutputStream fos = new FileOutputStream(new File("./pageSource.html"));
			fos.write(text.getBytes());
			fos.close();
		}

		public void waitForAngularRequestToFinish(){
			WebDriverWait wait = new WebDriverWait(wdriver, 60);
			wait.until(angularHasFinishedProcessing());
		}

		public ExpectedCondition<Boolean> angularHasFinishedProcessing() {
			return new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return Boolean.valueOf(((JavascriptExecutor) driver).executeScript("return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)").toString());
				}
			};
		}

		public void takeScreenShot(String name){
			String dir = System.getProperty("user.dir");
			DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
			Date date = new Date();
			File scrFile = ((TakesScreenshot)wdriver).getScreenshotAs(OutputType.FILE);
			String file =dir+"/screenshots/"+name+"-"+dateFormat.format(date)+".png";
			try {
				FileUtils.copyFile(scrFile, new File(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		public void mouseHoverOnElement(WebDriver driver,WebElement element){
			Actions act = new Actions(driver);
			act.moveToElement(element).build().perform();
		}

		public void waitandAcceptAlert(){
			WebDriverWait wait = new WebDriverWait(wdriver, 30);
			wait.until(ExpectedConditions.alertIsPresent());
			try{
				wdriver.switchTo().alert().accept();
				Reporter.log("Accepted Alert", true);
			}catch(Exception e){
				Reporter.log("Alert not present", true);
			}
		}


		public void closeBrowser(){

			wdriver.close();
			Reporter.log("*******Browser closed Successfully********", true);
		}


		public void waitUntilElementAppears(WebElement element){

			WebDriverWait wait = new WebDriverWait(wdriver, 60);
			wait.until(ExpectedConditions.visibilityOf(element));
		}


		public void waitUntilElementclickable(WebElement element){

			WebDriverWait wait = new WebDriverWait(wdriver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}



		public void selectDropdownText(WebElement element, String value)
		{

			Select dropdown = new Select(element);
			dropdown.selectByVisibleText(value);
		}

		public void selectDropdownValue(WebElement element, String value)
		{

			Select dropdown = new Select(element);
			dropdown.selectByValue(value);
		}


		public  void scrollToElementViaJavascript(WebElement element) 
		{        
			((JavascriptExecutor) wdriver).executeScript("arguments[0].scrollIntoView();", element);     
		}
		public  void scrollup(String xValue) 
		{    
			String parameter="scroll(" +xValue+ ",0)"; 
			JavascriptExecutor jse = (JavascriptExecutor)wdriver;
			jse.executeScript(parameter); //x value '500' can be altered
		}



		public void refreshPage()
		{
			wdriver.navigate().refresh();
			Reporter.log("Refresing Page", true);
		}

	}
