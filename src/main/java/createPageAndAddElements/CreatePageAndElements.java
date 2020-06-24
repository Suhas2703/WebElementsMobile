package createPageAndAddElements;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.TestOptimize.GenericLib.FileLib;
import com.TestOptimize.GenericLib.Utility;

public class CreatePageAndElements 
{
	public Utility util = new Utility();
	public WebDriver driver;
	public String page = "Login1"; //main page
	FileLib flib=new FileLib();
	public int project_number=Integer.parseInt(flib.getpropertykeyvalue("project_index"));
	//Excel related Variables
	public String excelPath = "./Excel/CreatePageAndElements.xlsx";
	public String sheetName = "AddressSearch;ListViewControls";
	public WebDriverWait wait;

	@BeforeClass
	public void configBC() throws InterruptedException
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(flib.getpropertykeyvalue("url"));
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 5);
		driver.findElement(By.name("username")).sendKeys(flib.getpropertykeyvalue("username"));
		driver.findElement(By.name("password")).sendKeys(flib.getpropertykeyvalue("password"),Keys.ENTER);
		util.selectByName(driver.findElement(By.id("userProject")),project_number );
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[contains(@href,'repository')]")).click();
	}

	@DataProvider
	public Object[][] getExcelDataForGivenPage() throws Exception
	{
		FileInputStream ip = new FileInputStream(excelPath);
		Workbook wb = WorkbookFactory.create(ip);
		Sheet sh = wb.getSheet(sheetName);

		int rowNum = sh.getLastRowNum();
		int cellNum = sh.getRow(1).getLastCellNum();
		Object[][] data = new Object[rowNum][cellNum];
		for(int i=0;i<rowNum;i++)
		{
			for(int j=0;j<cellNum;j++)
			{
				data[i][j] = sh.getRow(i+1).getCell(j).getStringCellValue();
			}
		}		
		return data;
	}

	@Test(dataProvider = "getExcelDataForGivenPage")
	public void addingElements(String parentPage, String newPageName, String WebElementName, String type, 
			String locator1, String value1, String locator2, String value2) throws Exception 
	{
		try
		{
			driver.findElement(By.xpath("//span[text()='"+newPageName+"']"));
			System.out.println(newPageName+" -> Page Already Exist, So Adding webelements");
		}
		catch(Exception e)
		{
			//scroll down to parent page
			//			System.out.println("Page Not found So creating new Page "+newPageName);
			//			try
			//			{
			//				driver.findElement(By.xpath("//span[text()='"+parentPage+"']"));
			//			}
			//			catch(Exception f)
			//			{
			//					util.executeScript(driver, parentPage);
			//					Thread.sleep(1000);
			//					System.out.println("Scrolled");
			//			}
			//			

			if(parentPage.contains("Create Page"))
			{
				//Click on +Create Page button
				driver.findElement(By.xpath("//button[text()=' + Create Screen']")).click();
				Thread.sleep(1000);
				//Select Application Type
				util.selectByValue(driver.findElement(By.xpath("(//select[@formcontrolname='Type'])[1]")), "Mobile");

				//Enter pageName
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@placeholder='Screen Name'])[1]")));
				driver.findElement(By.xpath("(//input[@placeholder='Screen Name'])[1]")).sendKeys(newPageName);

				//Click on create button
				driver.findElement(By.xpath("//button[text()='Create']")).click();
				System.out.println(newPageName + " Page is created --> Pass");
			}
			else
			{
				//right click on parent page
				util.rightClick(driver, driver.findElement(By.xpath("//span[text()='"+parentPage+"']")));
				//Thread.sleep(5000);
				System.out.println("Adding a child page  >>"+newPageName);
				//click on add child page option
				driver.findElement(By.xpath("//div[contains(text(),'Add Child Screen ')]")).click();
				//driver.findElement(By.xpath("//ul[contains(@class,'contextmenu')]/li[@data-command='addChildPage']")).click();

				//Select Application Type
				Thread.sleep(1000);
				util.selectByValue(driver.findElement(By.xpath("(//select[@formcontrolname='Type'])[1]")), "Mobile");				
				//Enter pageName
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//input[@placeholder='Screen Name'])[1]")));
				driver.findElement(By.xpath("(//input[@placeholder='Screen Name'])[1]")).sendKeys(newPageName);

				//Click on create button
				driver.findElement(By.xpath("//button[text()='Create']")).click();

				System.out.println(newPageName + " Page is created --> Pass");

				driver.findElement(By.xpath("//div[contains(text(),'Successfully')]")).click();
				driver.navigate().refresh();
			}
		}

		//Add Elements to newly created page
		//		System.out.println("Clicked on table header -> Page");
		System.out.println("Adding WebElement in to page -> "+newPageName);
		util.executeScript(driver, newPageName);

		//		Thread.sleep(5000);
		//right click on new page
		util.rightClick(driver, driver.findElement(By.xpath("//span[text()='"+newPageName+"']")));


		//click on add AddWebElement option
		//wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Element')]")));
		Thread.sleep(1500);
		Robot r = new Robot();
		r.keyPress(KeyEvent.VK_CONTROL);
		r.keyPress(KeyEvent.VK_X);
		r.keyRelease(KeyEvent.VK_CONTROL);
		r.keyRelease(KeyEvent.VK_X);

		//Enter Element Name
		driver.findElement(By.xpath("//label[contains(text(),'Element Name')]/..//input")).sendKeys(WebElementName);

		//Select Element Type
		WebElement SelectType = driver.findElement(By.xpath("//label[text()='Type']/..//select"));
		util.selectByValue(SelectType, type);

		//Add 2 locators and their values
		for(int i=1;i<=2;i++)
		{
			if(i==1)
			{
				//Select Locator
				//(//div[contains(text(),'1')]/..//select[contains(@formcontrolname,locatorName-0)])[1]
				WebElement locator = driver.findElement(By.xpath("(//option[contains(text(),'id')])["+i+"]/parent::select"));
				util.selectByValue(locator, locator1);

				//Enter Locator value
				WebElement locatorValue = driver.findElement(By.xpath("(//input[contains(@name,'locatorValue')])["+i+"]"));
				//locatorValue.clear();
				locatorValue.sendKeys(value1);
			}

			else
			{
				//Click add locator below
				driver.findElement(By.xpath("//button[@title='Add Locator']")).click();

				//Select Locator
				WebElement locator = driver.findElement(By.xpath("(//option[contains(text(),'id')])["+i+"]/parent::select"));
				util.selectByValue(locator, locator2);

				//Enter Locator value
				WebElement locatorValue = driver.findElement(By.xpath("(//input[contains(@name,'locatorValue')])["+i+"]"));
				//locatorValue.clear();
				locatorValue.sendKeys(value2);

				//click create and close button
				driver.findElement(By.xpath("//button[contains(text(),'Create & Close')]")).click();
				System.out.println("Created Element -> "+WebElementName+" into "+newPageName+" Page");
				//				Thread.sleep(1500);
				driver.findElement(By.xpath("//div[contains(text(),'Successfully')]")).click();
				driver.navigate().refresh();
			}
		}
	}
}
