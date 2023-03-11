package com.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

public class ProductPageTest extends BaseTest {

	@BeforeClass
	public void prodInfoSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@DataProvider
	public Object[][] getProductData() {
		return new Object[][] {
				{"MacBook", "MacBook Pro"},
				{"MacBook", "MacBook Air"},
				{"iMac", "iMac"},
				};
		}	

	
	@Test(dataProvider = "getProductData")
	public void productHeaderTest(String searchKey, String mainProductName) {
		searchResultsPage = accPage.performSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(mainProductName);
		String actProdHeader = productInfoPage.getProductHeader(mainProductName);
		Assert.assertEquals(actProdHeader, mainProductName);
	}
	
	@DataProvider
	public Object[][] getProductInfoData() {
		return new Object[][] {
				{"MacBook", "MacBook Pro", AppConstants.MACBOOK_PRO_IMAGES_COUNT},
				{"MacBook", "MacBook Air", AppConstants.MACBOOK_AIR_IMAGES_COUNT},
				{"iMac", "iMac", AppConstants.IMAC_IMAGES_COUNT},
				};
		}	
	
	@Test(dataProvider = "getProductInfoData")
	public void productImagesCountTest(String searchKey, String mainProductName, int imagesCount) {
		searchResultsPage = accPage.performSearch(searchKey);
		productInfoPage = searchResultsPage.selectProduct(mainProductName);
		int actProductImages = productInfoPage.getProductImagesCount();
		System.out.println("Actual Product images: " +actProductImages);
		Assert.assertEquals(actProductImages, imagesCount);
	}
	
	@Test
	public void productMetaDataTest() {
		searchResultsPage = accPage.performSearch("Macbook");
		productInfoPage = searchResultsPage.selectProduct("MacBook Pro");
		Map<String, String> actMetaDataMap = productInfoPage.getProductMetadata();
		Assert.assertEquals(actMetaDataMap.get("Brand"), "Apple");
		Assert.assertEquals(actMetaDataMap.get("Product Code"), "Product 18");
		Assert.assertEquals(actMetaDataMap.get("Reward Points"), "800");
		Assert.assertEquals(actMetaDataMap.get("Availability"), "In Stock");
	}
}
