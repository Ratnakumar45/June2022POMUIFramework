package com.qa.opencart.tests;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Epic - 200: Open cart application accounts page design")
@Story("US - 101: Design accounts page features")
public class AccountsPageTest extends BaseTest {

	@BeforeClass
	public void accSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Description("accPageTitleTest -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.MINOR)
	@Test(priority = 1)
	public void accPageTitleTest() {
		String actAccPageTitle = accPage.getAccPageTitle();
		Assert.assertEquals(actAccPageTitle, AppConstants.ACC_PAGE_TITLE);
	}
	
	@Description("accPageUrlTest -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 2)
	public void accPageUrlTest() {
		Assert.assertTrue(accPage.getAccPageUrl());
	}
	
	@Description("Acc page search test -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 3)
	public void searchExistTest() {
		Assert.assertTrue(accPage.isSearchExist());
	}
	
	@Description("Acc page logout link exist test -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 4)
	public void logoutLinkExistTest() {
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}
	
	@Description("Acc page header test -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.TRIVIAL)
	@Test(priority = 5)
	public void accPageHeadersTest() {
		ArrayList<String> actHeaderList = accPage.getAccSecHeadersList();
		System.out.println("Actual AccPage Headers: " +actHeaderList);
		Assert.assertEquals(actHeaderList, AppConstants.ACC_PAGE_SECTIONS_HEADERS);
	}
	
	@DataProvider
	public Object[][] getProductKey() {
		return new Object[][] {
				{"MacBook"},
				{"iMac"},
				{"Samsung"},
				};
		}
	
	@Description("Acc page product search check test -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.CRITICAL)
	@Test(dataProvider = "getProductKey", priority = 6)
	public void searchCheckTest(String productKey) {
		searchResultsPage = accPage.performSearch(productKey);
		Assert.assertTrue(searchResultsPage.isSearchSuccessful());
	}
	
	@DataProvider
	public Object[][] getProductData() {
		return new Object[][] {
				{"MacBook", "MacBook Pro"},
				{"MacBook", "MacBook Air"},
				{"iMac", "iMac"},
				{"Samsung", "Samsung SyncMaster 941BW"},
				{"Samsung", "Samsung Galaxy Tab 10.1"}
				};
		}	

	@Description("Acc page product search test -- Dev Name: @Ratna Kumar")
	@Severity(SeverityLevel.BLOCKER)
	@Test(dataProvider = "getProductData", priority = 7)
	public void searchTest(String searchKey, String mainProductName) {
		searchResultsPage = accPage.performSearch(searchKey);
        if(searchResultsPage.isSearchSuccessful()) {
        	productInfoPage = searchResultsPage.selectProduct(mainProductName);
        	String actualProductHeader = productInfoPage.getProductHeader(mainProductName);
        	Assert.assertEquals(actualProductHeader, mainProductName);
        }
	}
	
}
