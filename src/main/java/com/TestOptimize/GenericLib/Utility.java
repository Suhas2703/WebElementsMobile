package com.TestOptimize.GenericLib;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Utility 
{
	public void selectByName(WebElement element, int projectIndex)
	{
		Select sel = new Select(element);
		sel.selectByIndex(projectIndex);

	}

	public void selectByValue(WebElement element, String value)
	{
		Select sel = new Select(element);
		sel.selectByValue(value);

	}

	public void doubleClick(WebDriver driver, WebElement element)
	{
		Actions act = new Actions(driver);
		act.doubleClick(element).perform();
	}

	public void rightClick(WebDriver driver, WebElement element)
	{
		Actions act = new Actions(driver);
		act.contextClick(element).perform();
	}

	public String getSelectedOption(WebElement element)
	{
		Select sel = new Select(element);
		return sel.getFirstSelectedOption().getText().trim();
	}
	public void executeScript(WebDriver driver,String parentPage)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;		
		WebElement element = driver.findElement(By.xpath("//span[text()='"+parentPage+"']"));
		int y=element.getLocation().getY();
		js.executeScript("window.scrollTo(0,"+y+")", element);
		System.out.println("Page Scrolled Till -> "+parentPage);
	}
	
}
