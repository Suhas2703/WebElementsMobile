package com.WebElement;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Sample 
{

	public static void main(String[] args) throws Exception
	{
		FileInputStream ip = new FileInputStream("./src\\main\\java\\com\\WebElement\\testData\\Tree Structure.xlsx");
		Sheet sh = WorkbookFactory.create(ip).getSheet("Sheet1");
		int rowNum = sh.getLastRowNum();
		
		for(int i=0;i<=rowNum;i++)
		{
			int cellNum = sh.getRow(i).getLastCellNum();
			System.out.println(cellNum);
		}
	}
}
