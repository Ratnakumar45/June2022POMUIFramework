package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.factory.DriverFactory;

public class ElementUtil {

	private WebDriver driver;
	private Select select;
	private Actions act;
	private JavaScriptUtil jsUtil;
	
    private static final String ELEMENT_NOT_FOUND_ERROR = "element is not available on the page: ";

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		act = new Actions(driver);
        jsUtil = new JavaScriptUtil(driver);
	}

	public WebElement getElement(By locator) {
		
		WebElement ele =  driver.findElement(locator);
		if(Boolean.parseBoolean(DriverFactory.highlight)) {
			jsUtil.flash(ele);
		}
		return ele;
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public void doSendKeys(By locator, String value) {
		WebElement ele = getElement(locator);
		ele.clear();
		ele.sendKeys(value);
	}

	public void doClick(By locator) {
		getElement(locator).click();
	}

	public String doGetText(By locator) {
		return getElement(locator).getText();
	}

	public String doGetAtttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}

	public boolean doEleIsDisplayed(By locator) {
		return getElement(locator).isDisplayed();
	}

	// checks only when single element present on the page
	public boolean isSingleElementPresent(By locator) {
		List<WebElement> list = getElements(locator);
		System.out.println(list.size());
		if (list.size() == 1) {
			System.out.println("single search ele is present on the page");
			return true;

		} else {
			System.out.println("no search or multiple search present on the page");
			return false;
		}
	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public List<String> getElementTextList(By locator) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleTextList = new ArrayList<String>();
		for (WebElement e : eleList) {
			String text = e.getText();
			if (text.length() != 0) {
				eleTextList.add(text);
			}
		}

		return eleTextList;
	}

	// **************Drodown Utils***********//
	// Only when html tag = <select>
	public void doSelectDropdownByIndex(By locator, int index) {
		select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public void doSelectDropdownByVisibleText(By locator, String text) {
		select = new Select(getElement(locator));
		select.selectByVisibleText(text);
	}

	public void doSelectDropdownByValue(By locator, String value) {
		select = new Select(getElement(locator));
		select.selectByValue(value);
	}

	public List<WebElement> getDropDownOptions(By locator) {
		select = new Select(getElement(locator));
		return select.getOptions();
	}

	public int getDropDownOptionsCount(By locator) {
		return getDropDownOptions(locator).size();
	}

	public List<String> getDropDownOptionsList(By locator) {
		Select select = new Select(getElement(locator));
		List<WebElement> optionsList = select.getOptions();
		List<String> optionsTextList = new ArrayList<String>();
		for (WebElement e : optionsList) {
			String text = e.getText();
			if (text.length() != 0) {
				optionsTextList.add(text);
			}
		}

		return optionsTextList;
	}

	// If select class not working, then we have to use this method - custom xpath
	public void doSelectValueFromDropdown(By locator, String value) {
		List<WebElement> optionsList = getElements(locator);

		System.out.println(optionsList.size());

		for (WebElement e : optionsList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

	// Used for any search locator
	public void doSearch(By searchLocator, String searchKey, By suggLocator, String value) throws InterruptedException {
		getElement(searchLocator).sendKeys(searchKey);
		Thread.sleep(3000);

		List<WebElement> suggList = getElements(suggLocator);

		System.out.println(suggList.size() - 1);

		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

	// Another way of clicking the dropdown link
	public void doSearch(String tagName, String text) {
		By suggLocator = By.xpath("//" + tagName + "[text()='" + text + "']");
		getElement(suggLocator).click();
	}

	// Footer links
	public ArrayList<String> getFooterLinksList(By locator) {
		List<WebElement> footerList = getElements(locator);
		System.out.println(footerList.size());
		ArrayList<String> eleTextList = new ArrayList<String>();
		for (WebElement e : footerList) {
			String text = e.getText();
			eleTextList.add(text);
		}

		return eleTextList;
	}

	public Boolean checkElementisMandatory(String jsScript) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		String man_text = js.executeScript(jsScript).toString();

		System.out.println(man_text);

		if (man_text.contains("*")) {
			System.out.println("ele is mandetory field");
			return true;

		} else {
			System.out.println("FN is not a mandetory field");
			return false;
		}

	}

	// **************************Actions Utils***************************//
	public void handleLevel1MenuItems(By parentMenu, By childMenu) throws InterruptedException {
		act.moveToElement(getElement(parentMenu)).build().perform();
		Thread.sleep(3000);
		// getElement(childMenu).click();
		doClick(childMenu);
	}

	public void doActionsClick(By locator) {
		act.click(getElement(locator)).build().perform();
	}

	public void doActionsSendkeys(By locator, String value) {
		act.sendKeys(getElement(locator), value).build().perform();
	}

	public void doDragAndDrop(By draggableLocator, By droppableLocator) {
		WebElement sourceEle = getElement(draggableLocator);
		WebElement targetEle = getElement(droppableLocator);
		act.dragAndDrop(sourceEle, targetEle).build().perform();
	}

	// ****************************Wait utils******************************//

	/**
	 * An expectation for checking that an element is present on the DOM of a page. 
	 * This does notnecessarily mean that the element is visible.
	 * Default polling time - 500ms
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */
	public WebElement waitForElementPresence(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	/**
	 * An expectation for checking that an element is present on the DOM of a page. 
	 * This does notnecessarily mean that the element is visible.
	 * Default polling time - customized
	 * 
	 * @param locator
	 * @param timeout
	 * @return
	 */
	
	public WebElement waitForElementPresence(By locator, int timeout, int pollingTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout), Duration.ofSeconds(pollingTime));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	/**
	 * An expectation for checking that all elements present on the web page that match the locator are visible.
	 * Visibility means that the elements are not only displayed but also have a height and width that is greater than 0.
	 * @param locator
	 * @param timeout
	 * @return
	 */
	
	public List<WebElement> waitForElementsToBeVisible(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public void doSendKeysWithWait(By locator, int timeout, String value) {
		waitForElementPresence(locator, timeout).sendKeys(value);
	}
	
	public void doClickWithWait(By locator, int timeout) {
		waitForElementPresence(locator, timeout).click();
	}
	
	public String getElementTextWithWait(By locator, int timeout) {
		return waitForElementPresence(locator, timeout).getText();		
	}
	
	/*An expectation for checking that an element is present on the DOM of a page and visible.Visibility means that the element is 
	 * not only displayed but also has a height and width that isgreater than 0.
	 * 
	 */
	public WebElement waitForElementVisible(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));	
	}
	
	public void doSendKeysWithVisibleElement(By locator, int timeout, String value) {
		waitForElementPresence(locator, timeout).sendKeys(value);
	}
	
	public void doClickWithVisibleElement(By locator, int timeout) {
		waitForElementPresence(locator, timeout).click();
	}
	
	public String getElementTextWithVisibleElement(By locator, int timeout) {
		return waitForElementPresence(locator, timeout).getText();		
	}
	
	public void clickWhenReady(By locator, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	public Alert waitForAlert(int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		return wait.until(ExpectedConditions.alertIsPresent());	
	}
	
	public String getAlertText(int timeout) {
		return waitForAlert(timeout).getText();
	}
	
	public void acceptAlert(int timeout) {
		waitForAlert(timeout).accept();
	}
	
	public void dismissAlert(int timeout) {
		waitForAlert(timeout).dismiss();
	}
	
	public void alertSendKeys(int timeout, String value) {
		waitForAlert(timeout).sendKeys(value);
	}
	
	public String waitForTitleContains(int timeout, String titleFraction) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		if(wait.until(ExpectedConditions.titleContains(titleFraction))) {
			return driver.getTitle();
		}
		else {
			return null;
		}
	}
	
	public String waitForTitleIs(int timeout, String titleValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		if(wait.until(ExpectedConditions.titleIs(titleValue))) {
			return driver.getTitle();
		}
		else {
			return null;
		}
	}

	public String waitForUrlContains(int timeout, String urlFraction) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		if(wait.until(ExpectedConditions.urlContains(urlFraction))) {
			return driver.getCurrentUrl(); 
		}
		else {
			return null;
		}
	}
	
	public String waitForUrlIs(int timeout, String urlValue) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		if(wait.until(ExpectedConditions.urlToBe(urlValue))) {
			return driver.getCurrentUrl(); 
		}
		else {
			return null;
		}
	}
	
	public void waitForFrame(int timeout, int frameIndex) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}
	
	public void waitForFrame(int timeout, String nameOrId) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
	}
	
	public void waitForFrame(int timeout, WebElement frameElement) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}
	
	public void waitForFrame(int timeout, By frameLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}
	
	public WebElement waitForElementToBeVisibleWithFluentWait(By locator, int timeout, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofSeconds(pollingTime))
                        .ignoring(NoSuchElementException.class)
                             .ignoring(StaleElementReferenceException.class)
                                   .ignoring(ElementNotInteractableException.class)
                                          .withMessage(ElementUtil.ELEMENT_NOT_FOUND_ERROR +locator);
                    
       return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}


}
