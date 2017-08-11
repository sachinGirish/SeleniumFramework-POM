package tests.web;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import pages.web.W_BasePage;

public class W_BaseTest 
{

public WebDriver wdriver;
	
	W_BasePage basepage;

	//ATUTestRecorder recorder;
	
	@BeforeClass
	public void setUp() throws IOException{
		String dir = System.getProperty("user.dir");
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		if(!new File(dir+"\\script-videos").exists())
			new File(dir+"\\script-videos").mkdirs();
	//	recorder = new ATUTestRecorder(dir+"\\script-videos\\", this.getClass().getSimpleName()+ "-" + dateFormat.format(date), false);
		// To start video recording.
		basepage = new W_BasePage(wdriver);
//		recorder.start();
		wdriver = basepage.launchWebApp();
	}
	
	@AfterMethod
	public void catchExceptions(ITestResult result) throws IOException, InterruptedException 
	{    
		String dir = System.getProperty("user.dir");
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		if(!result.isSuccess()){            
			File scrFile = ((TakesScreenshot)wdriver).getScreenshotAs(OutputType.FILE);
			String file =dir+"/screenshots/"+this.getClass().getSimpleName()+"-"+dateFormat.format(date)+".png";
			try {
				FileUtils.copyFile(scrFile, new File(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@AfterClass
	public void tearDown() {
		wdriver.close();
	//	recorder.stop();
	}
	
}
