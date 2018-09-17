package com.branch.qatest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class ObjectMap 
{
	WebDriver driver;
	
	@FindAll({ @FindBy(xpath = "//input[@title = 'Search']") })
	public WebElement edtSearchBox;
	
	@FindAll({ @FindBy(xpath = "//input[@value = 'Google Search']") })
	public WebElement btnSearch;
	
	@FindAll({ @FindBy(xpath = "//a[contains(@href,'https://branch.io/')]") })
	public WebElement lnkBranchIO;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Team')]") })
	public WebElement lnkTeamFooterLink;
	
	@FindAll({ @FindBy(xpath = "//div[@class = 'image-block']") })
	public WebElement imgPhoto;
	
	@FindAll({ @FindBy(xpath = "//div[@class='info-block']/h2") })
	public WebElement txtName;
	
	@FindAll({ @FindBy(xpath = "//div[@class='info-block']/h4") })
	public WebElement txtDepartmentName;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Accept')]") })
	public WebElement btnAccept;
	
	@FindAll({ @FindBy(xpath = "//a[@href='#all']") })
	public WebElement tabAll;
	
	@FindAll({ @FindBy(xpath = "//a[@href='#data']") })
	public WebElement tabData;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Engineering')]") })
	public WebElement tabEngineering;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Marketing')]") })
	public WebElement tabMarketing;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Operations')]") })
	public WebElement tabOperations;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Partner Growth')]") })
	public WebElement tabPartnerGrowth;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Product')]") })
	public WebElement tabProduct;
	
	@FindAll({ @FindBy(xpath = "//a[contains(text(),'Recruiting')]") })
	public WebElement tabRecruiting;
	
	
	public String xpathForEmployeesName = "//div[contains(@style,'inline')]//div[@class='info-block']/h2";
	
	public String xpathForEmployeeBlock = "//div[contains(@style,'inline')]//div[@class='info-block']";


	public String xpathForEmployeesDept = "//div[contains(@style,'inline')]//div[@class='info-block']/h4";

	
	public ObjectMap(WebDriver driver) 
	{
		this.driver = driver;
	}

}
