package com.branch.qatest;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class BranchTest {
	
	//VARIABLE DECLARATION
	private List<String> employeesName = new ArrayList<String>();
	private Map<String, List<String>> DeptEmpMap = new LinkedHashMap<String, List<String>>();
	private Set<String> dataList = new HashSet<String>(); 
	private Set<String> engineeringList = new HashSet<String>(); 
	private Set<String> marketingList = new HashSet<String>();
	private Set<String> operationsList = new HashSet<String>();	
	private Set<String> partnerGrowthList = new HashSet<String>();
	private Set<String> productList = new HashSet<String>();
	private Set<String> recruitingList = new HashSet<String>();
	private WebDriver driver ;
	private String ssPath = System.getProperty("user.dir")+"\\Reports\\Screenshots\\";
	
	@Parameters({"browser"})
	@Test(priority = 0)//1,2,3
	public void launchAndNavigate(String browserName) throws InterruptedException, IOException, HeadlessException, AWTException, ParseException 
	{
	
		//Creating Log file to log results
		Logs.createLogFile("BranchTest");
		String FFDriverPath = null, ChromeDriverPath = null;
		
		//Check Platform
		String osName = System.getProperty("os.name");
		if(osName.toUpperCase().contains("WINDOWS"))
		{
			Logs.log("ENVIRONMENT : WINDOWS");
			FFDriverPath = "Resources\\geckodriver.exe";
			ChromeDriverPath = "Resources\\chromedriver.exe";
			
		}
		
		if(osName.toUpperCase().contains("MAC"))
		{
			 Logs.log("ENVIRONMENT : MAC");
			 FFDriverPath = "Resources/geckodriver";
			 ChromeDriverPath = "Resources/chromedriver";
		}
		
		
		//Check browser
		if(browserName.equalsIgnoreCase("Firefox"))
		{
			 Logs.log("BROWSER : FIREFOX");
			 System.setProperty("webdriver.gecko.driver", FFDriverPath);
			 driver = new FirefoxDriver();
		}
		
		else if(browserName.equalsIgnoreCase("Chrome"))
		{
			 Logs.log("BROWSER : CHROME");
			 System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
			 driver = new ChromeDriver();
		}
		
		else if(browserName.equalsIgnoreCase("Safari"))
		{
			 Logs.log("BROWSER : SAFARI");
			 driver = new SafariDriver();
		}
		
		
		
		 driver.manage().window().maximize();
		 
		 //Creating Instance of ObjectMap
		 ObjectMap  om = new ObjectMap(driver);
		 PageFactory.initElements(driver, om);
		 driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		 
		 //1. Navigate to google.com and search for branch website
		 try{
		 driver.get("https://www.google.com/");
		 }
		 catch(TimeoutException timeout)
		 {
			 Logs.log("Failed to LOAD URL : ");
			 Logs.log(timeout.getMessage());
		 }
		 
		 boolean chkSearchBox = checkExist("Google Search Box",om.edtSearchBox);
			 Assert.assertTrue(chkSearchBox);
			 om.edtSearchBox.sendKeys("branch");
			 captureScreenShot("Enter Search Keyword as branch");
			 om.edtSearchBox.sendKeys(Keys.ENTER);

		//2. Navigate to branch website
		 boolean chkBranchWebsite =checkExist("Branch.IO",om.lnkBranchIO);
			 Assert.assertTrue(chkBranchWebsite);
			 om.lnkBranchIO.click();

			 Thread.sleep(5000);
				WebDriverWait wait = new WebDriverWait(driver, 20);
				wait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver lDriver) {
						return ((JavascriptExecutor) lDriver).executeScript("return document.readyState").equals("complete");
					}
				});
		 
		 try{
			 if(om.btnAccept.isDisplayed())
			 {
				 captureScreenShot("Button Accept");
				 om.btnAccept.click();
			 }
		 }catch(NoSuchElementException exception)
		 {
			 Logs.log("Accept button is not displayed");
		 }
		 
		 //3. Scroll down to footnotes and click on "Team" link
		 boolean chkTeamLink = checkExist("Team Footer link",om.lnkTeamFooterLink);
		 Assert.assertTrue(chkTeamLink);
		 scrollToElement(om.lnkTeamFooterLink);
		 captureScreenShot("Team link");
		 om.lnkTeamFooterLink.click();

	}


	//Read Employee info from ALL tab and store in HashMap
	@Test(priority = 1)
	public  void readAllTab() throws IOException 
	{
		
		ObjectMap  om = new ObjectMap(driver);
		PageFactory.initElements(driver, om);
		List<WebElement>  employees= driver.findElements(By.xpath(om.xpathForEmployeesName));
		List<WebElement> employeeDept = driver.findElements(By.xpath(om.xpathForEmployeesDept));
		captureScreenShot("All Tab");
		for(int i = 0; i<employees.size() ; i++)
		{
			scrollToElement(employees.get(i));
			String name = employees.get(i).getText();
			employeesName.add(name);
			String dept = employeeDept.get(i).getText().trim().toUpperCase();

			if(DeptEmpMap.containsKey(dept))
				DeptEmpMap.get(dept).add(name);
			else
			{
				ArrayList<String> templist = new ArrayList<String>();
				templist.add(name);
				DeptEmpMap.put(dept.trim().toUpperCase(),templist);
			} 		  
		}

		Logs.log("DEPT EMP MAP :::: " +DeptEmpMap);
		
	}

	//Read Employee info from Each Department tab and store in Respective Lists
	@Test(priority = 2)   
	public void readDepartmentTabs() throws IOException, InterruptedException
	{
		ObjectMap  om = new ObjectMap(driver);
		PageFactory.initElements(driver, om);
		
		dataList = populateDeptLists("Data", om.tabData);
		engineeringList= populateDeptLists("Engineering", om.tabEngineering);
		marketingList = populateDeptLists("Marketing", om.tabMarketing);
		operationsList = populateDeptLists("Operations",om.tabOperations);
		partnerGrowthList = populateDeptLists("Partner Growth",om.tabPartnerGrowth);
		productList = populateDeptLists("Product",om.tabProduct);
		recruitingList = populateDeptLists("Recruiting",om.tabRecruiting);
	}


	//Method to Populate Department Lists
	private Set<String> populateDeptLists(String deptName, WebElement deptTab) throws IOException, InterruptedException 
	{
		Set<String> empSet = new HashSet<String>(); 
		ObjectMap  om = new ObjectMap(driver);
		PageFactory.initElements(driver, om);
		deptTab.click();
		Thread.sleep(2000);
		scrollDown();
		scrollDown();
		scrollDown();
		Thread.sleep(2000);

		captureScreenShot(deptName);
		List<WebElement> empNames = driver.findElements(By.xpath(om.xpathForEmployeesName));
		for(WebElement emp : empNames)
		{	
			scrollToElement(emp);
//			captureScreenShot(deptName+ " " +emp.getText());
			empSet.add(emp.getText());
		}
		Logs.log(deptName +" Employess are : " + empSet);
		return empSet;
	}


	//Method to Check Presence of WebElements
	private boolean checkExist(String elmName, WebElement elm) throws IOException
	{
		boolean flagIsPresent = false; 
		try
		{
			if(elm.isDisplayed()){
				Logs.log("Element " + elmName + " Is Displayed");
				captureScreenShot("Diplay of " + elmName);
				flagIsPresent = true;
			}
		}
		catch(Exception e)
		{
			Logs.log("Element " + elmName + " Is Not Displayed");
			captureScreenShot("Absence of " + elmName);
			flagIsPresent = false;

		}
		return flagIsPresent;
	}


	//Function to scroll Down on the web Page
	private  void scrollDown()
	{
		driver.findElement(By.xpath("//body")).sendKeys(Keys.PAGE_DOWN);
	}
	//Function to scroll to an Element on the web Page
	private void scrollToElement(WebElement element)
	{
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}
	
	//Function to Capture Screenshot
	private void captureScreenShot(String ssFileName) throws IOException
	{
		File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String filePath = ssPath + ssFileName + ".png";
		FileUtils.copyFile(screenshotFile, new File(filePath));
		Logs.log("SCREENSHOT of "+ssFileName+" CAPTURED AT : "+filePath);
	}




	//5. Verify that number of employees match between All tab and sum of other tabs.
	@Test(dependsOnMethods={ "launchAndNavigate", "readAllTab", "readDepartmentTabs"})
	public void checkForSumOfEmployeesAcrossTabs() throws FileNotFoundException
	{

		int sumOfEmployeesAcrossDepts = dataList.size() + engineeringList.size() + marketingList.size() +
				operationsList.size() + partnerGrowthList.size() + productList.size() + recruitingList.size();

		if(employeesName.size() == sumOfEmployeesAcrossDepts)	 
			Logs.log("The Number of Employees in ALL Tab is equal to sum of Employess in Department Tabs : " + sumOfEmployeesAcrossDepts);
		else
			Logs.log("The Number of Employees in All Tab is : "+employeesName.size()+"  is NOT equal to sum of Employess in Department Tabs : " + sumOfEmployeesAcrossDepts);
		
		Assert.assertEquals(employeesName.size(), sumOfEmployeesAcrossDepts);

	}

	//6. Verify that employee names match between All tab and other tabs.	
	@Test(dependsOnMethods={ "launchAndNavigate", "readAllTab", "readDepartmentTabs"})
	public void compareNames() throws FileNotFoundException
	{


		for(String emp : employeesName)
		{
			boolean empNameInDeptTab = dataList.contains(emp)|| engineeringList.contains(emp) || marketingList.contains(emp) || 
					operationsList.contains(emp) || partnerGrowthList.contains(emp) || productList.contains(emp) ||
					recruitingList.contains(emp);
			Assert.assertTrue(empNameInDeptTab);
			if(!empNameInDeptTab)
				Logs.log(emp + " DOES NOT MATCH IN ANY DEPARTMENT");
				
					
		}
	}

	//7. Verify that employee departments are listed correctly between All tab and Department tabs.
	@Test(dependsOnMethods={ "launchAndNavigate", "readAllTab", "readDepartmentTabs"})
	public void compareDepartments() throws FileNotFoundException
	{
		List<String> listKeySet = new ArrayList<String>(DeptEmpMap.keySet());
		for(int i = 0 ; i<listKeySet.size(); i++)
		{
			String dept = listKeySet.get(i);
			List<String> empList = DeptEmpMap.get(dept);
			
				for(String emp : empList)
				{
					if(dataList.contains(emp)){
						Logs.log(emp + " Works for Data Team");
						continue;
					}
					

					else if(engineeringList.contains(emp)){
						Logs.log(emp + " Works for Engineering Team");
						continue;
					}
			

					else if(marketingList.contains(emp)){
						Logs.log(emp + " Works for Marketing Team");
						continue;
					}
				

					else if(operationsList.contains(emp)){
						Logs.log(emp + " Works for Operations Team");
						continue;
					}
					

					else if(partnerGrowthList.contains(emp)){
						Logs.log(emp + " Works for Partner Growth Team");
						continue;
					}
				

					else if(productList.contains(emp)){
						Logs.log(emp + " Works for Product Team");
						continue;
					}

			
					else if(recruitingList.contains(emp)){
						Logs.log(emp + " Works for Recruiting Team");
						continue;
					}
					

					else
						Logs.log(emp.toUpperCase() + " DOES NOT work for ANY Team");

				}
			


		}


	}

	//	8. Come up with 2 more valuable test cases.
	//FIRST TESTCASE - In Every Department Tab atleast One Employee should be listed
	@Test(dependsOnMethods={ "launchAndNavigate", "readAllTab", "readDepartmentTabs"})
	public void checkForOneEmployeeInEveryDepartment() throws FileNotFoundException
	{
		
		Boolean flagNoEmployee = false;
		if(dataList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Data team");
		}


		if(engineeringList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Engineering team");
		}

		if(operationsList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Operations team");
		}

		if(marketingList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Marketing team");
		}

		if(partnerGrowthList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Partner Growth team");
		}

		if(productList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Product team");
		}

		if(recruitingList.size()==0)
		{
			flagNoEmployee  = true;
			Logs.log("There are NO Employees in Recruiting team");
		}

		Assert.assertFalse(flagNoEmployee);
		if(flagNoEmployee==false)
		{
			Logs.log("There is atleast One Employee in Each team");
			Logs.log("There are " +dataList.size() + " Employees in Data team");
			Logs.log("There are " +engineeringList.size() + " Employees in Engineering team");
			Logs.log("There are " +operationsList.size() + " Employees in Operations team");
			Logs.log("There are " +marketingList.size() + " Employees in Marketing team");
			Logs.log("There are " +partnerGrowthList.size() + " Employees in Partner Growth team");
			Logs.log("There are " +productList.size() + " Employees in Product team");
			Logs.log("There are " +recruitingList.size() + " Employees in Recruiting team");

		}

	}
	
	//SECOND TESTCASE : In All tab - Verify every employee has a department listed 
	@Test(priority = 3) 
	public void test_ValidDepartmentNames() throws FileNotFoundException
	{
		 ObjectMap  om = new ObjectMap(driver);
		 PageFactory.initElements(driver, om);
		 om.tabAll.click();
		 WebElement nameObj = null;
		 List<WebElement> employeesInfoBlock = driver.findElements(By.xpath(om.xpathForEmployeeBlock));
		
	 for(int i = 1; i<=employeesInfoBlock.size(); i++)
		{

			try{
			String xpathForEmpName = "("+om.xpathForEmployeeBlock+"/h2)["+i+"]";
			 nameObj = driver.findElement(By.xpath(xpathForEmpName));
			if(nameObj!=null)
			{
				try{
				WebElement DeptObj = driver.findElement(By.xpath(xpathForEmpName+"/following-sibling::h4"));
				Assert.assertTrue(DeptObj.isDisplayed());
				Logs.log("Department is listed for the employee " +nameObj.getText());
				}
				catch(Exception e)
				{ 
					Logs.log("Unable to find Employee Department for " +nameObj.getText());
					 e.printStackTrace();

				}

			}
			}catch(Exception e)
			{
				Logs.log("Unable to find employee" +nameObj.getText());
				 e.printStackTrace();

			}
			
			
		}
	}
	
	//9.Come up with test that will fail and add explanation of failure as part of report
	//FAILURE TESTCASE : Check if every employee has a social account listed in Info block
	@Test(priority = 4) 
	public void test_employeeSocialAccount()
	{
		 ObjectMap  om = new ObjectMap(driver);
		 PageFactory.initElements(driver, om);
		 om.tabAll.click();
		 List<WebElement> employeesInfoBlock = driver.findElements(By.xpath(om.xpathForEmployeeBlock));
		for(int i=0 ; i<=employeesInfoBlock.size(); i++)
		{
			Assert.assertTrue(employeesInfoBlock.get(i).findElement(By.xpath("//a[@class='profile-link']")).isDisplayed(), "Profile Link Present for " +  employeesInfoBlock.get(i).findElement(By.xpath("//h2")).getText());
		}
	}


	@AfterSuite
	public void close()
	{
		driver.quit();
	}


}
