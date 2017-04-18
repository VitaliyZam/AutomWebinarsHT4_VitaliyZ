package main.utils;

import main.models.ProductData;
import main.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private ProductData product;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
    }
    public WebDriverWait explicitWait(int sec) {
        return new WebDriverWait(this.driver, sec);
    }
    public WebDriverWait explicitWait() {
        return (WebDriverWait) new WebDriverWait(this.driver, 20).ignoring(StaleElementReferenceException.class);
    }

    public WebElement waitElementVisibility(By locator) {
        return explicitWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public WebElement waitElementPresence(By locator) {
        return explicitWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    public WebElement waitElementToBeClickable(By locator) {
        return explicitWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */
    public void login(String login, String password) {
        driver.navigate().to(Properties.getBaseAdminUrl());
        driver.manage().deleteAllCookies();
        waitElementVisibility(By.id("email")).sendKeys(login);
        waitElementVisibility(By.id("passwd")).sendKeys(password);
        waitElementVisibility(By.name("submitLogin")).submit();
        waitElementVisibility(By.id("main"));
    }

    public void createProduct(ProductData newProduct) {
        this.product = newProduct;
        // TODO implement product creation scenario
        waitForContentLoad();
        WebElement catalogueTab = driver.findElement(By.id("subtab-AdminCatalog"));
        mouseOver(catalogueTab);
        waitElementToBeClickable(By.id("subtab-AdminProducts")).click();
        waitForContentLoad();
        waitElementToBeClickable(By.xpath("//a[@id]/i[text()='add_circle_outline']")).click();
        waitForPageLoad();
        WebElement productName = explicitWait().until(ExpectedConditions.
                presenceOfElementLocated((By.xpath("//input[@id='form_step1_name_1']"))));
        productName.clear();
        productName.sendKeys(newProduct.getName());
        WebElement productQty = driver.findElement(By.id("form_step1_qty_0_shortcut"));
        productQty.clear();
        productQty.sendKeys(newProduct.getQty() + "");
        WebElement productPrice = driver.findElement(By.id("form_step1_price_shortcut"));
        productPrice.clear();
        productPrice.sendKeys(newProduct.getPrice());
        waitElementToBeClickable(By.className("switch-input")).click();
        waitElementToBeClickable(By.className("growl-close")).click();
        explicitWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id("growls")));
        driver.findElement(By.xpath("//button/span[text()='Сохранить']")).submit();
        waitElementToBeClickable(By.className("growl-close")).click();
        explicitWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id("growls")));
    }

    public void newProductIsCreated() {
        boolean isCreated = false;
        driver.navigate().to(Properties.getBaseUrl());
        waitElementPresence(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']/i")).click();
        List<WebElement> paginator = explicitWait().until(ExpectedConditions.
                presenceOfAllElementsLocatedBy(By.xpath("//li/a[@rel='nofollow' and contains(@class,'search')]")));
        for (int i = 0; i < paginator.size(); i++) {
            List<WebElement> updatedPaginator = explicitWait().until(ExpectedConditions.
                    presenceOfAllElementsLocatedBy(By.xpath("//li/a[@rel='nofollow' and contains(@class,'search')]")));
            if (i != 0) updatedPaginator.get(i).click();
            waitForContentLoad();
            waitForPageLoad();
            
            //New commit: fix StaleElementReferenceException
            explicitWait().until(jQueryAJAXCallsAreCompleted());
            //---
            
            List<WebElement> articles = new ArrayList<>(explicitWait().until(ExpectedConditions.
                    visibilityOfAllElementsLocatedBy(By.xpath("//div/h1/a"))));
            int j = 0;
            while (!isCreated && j < articles.size()) {
                if (articles.get(j).getText().contains(BaseTest.product.getName())) {
                    isCreated = true;
                    articles.get(j).click();
                    waitElementVisibility(By.xpath("//body[@id='product']//div[@id='content-wrapper']"));
                    Assert.assertTrue(driver.findElement(By.xpath("//h1[@itemprop]")).
                            getText().toLowerCase().contains(product.getName().toLowerCase()), "Product name doesn't match");
                    Assert.assertTrue(driver.findElement(By.xpath("//div[@class='product-quantities']/span")).
                            getText().toLowerCase().contains(product.getQty()+"".toLowerCase()), "Product quantity doesn't match");
                    Assert.assertTrue(driver.findElement(By.xpath("//span[@itemprop='price']")).
                            getText().toLowerCase().contains(product.getPrice().toLowerCase()), "Product price doesn't match");
                }
                j++;
            }
            if (isCreated) break;
        }
        Assert.assertTrue(isCreated, "Failure: product not found");
    }

    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        // TODO implement generic method to wait until page content is loaded
        explicitWait().until(ExpectedConditions.invisibilityOfElementLocated(By.id("ajax_running")));
    }
    
    public ExpectedCondition<Boolean> jQueryAJAXCallsAreCompleted() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public void mouseOver(WebElement element) {
        String code = "var fireOnThis = arguments[0];"
                + "var evObj = document.createEvent('MouseEvents');"
                + "evObj.initEvent( 'mouseover', true, true );"
                + "fireOnThis.dispatchEvent(evObj);";
        ((JavascriptExecutor) driver).executeScript(code, element);
        CustomReporter.log("Hovering over the \"" + element.getTagName() + "\" tab");
    }

    public void waitForPageLoad() {
        explicitWait(10).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        });
    }
}
