import com.google.common.collect.Ordering;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sparrow on 04.02.2017.
 */
public class Exercise09 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL;
    private String username = "admin";
    private String password = "admin";

    @Before
    public void setUp() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 4);
        baseURL = "http://localhost:4040/litecart";
        performLogin();
    }

    @Test
    public void countriesSortingTest() {

        driver.navigate().to(baseURL + "/admin/?app=countries&doc=countries");

        Assert.assertEquals("Page Title not equal to 'Countries'",
                driver.findElement(By.cssSelector("h1")).getText(),
                "Countries");

        ArrayList<List<String>> countries = (ArrayList<List<String>>) ((JavascriptExecutor) driver).executeScript(
                "cr = [];" +
                        "table = document.querySelector('table.dataTable');" +
                        "rows = table.querySelectorAll('tr.row');" +
                        "for (var i = 1; i < rows.length; ++i) {" +
                        "   cells = rows[i].querySelectorAll('td');" +
                        "   cr.push([cells[4].textContent, cells[5].textContent]);" +
                        "}; return cr;");

        List<String> listOfAllCountries = new ArrayList<String>();
        List<String> listOfCountriesWithZones = new ArrayList<String>();

        for (int row = 0; row < countries.size(); row++) {
            //Ordering in app is case insensitive so need to make same case for comparing
            listOfAllCountries.add(countries.get(row).get(0));
            if (Integer.parseInt(countries.get(row).get(1)) > 0) {
                listOfCountriesWithZones.add(countries.get(row).get(0));
            }
        }

        Assert.assertTrue("List of countries is not ordered",
                Ordering.from(String.CASE_INSENSITIVE_ORDER).isOrdered(listOfAllCountries));

        for (String zonesCountry : listOfCountriesWithZones) {

            driver.navigate().to(baseURL + "/admin/?app=countries&doc=countries");
            driver.findElement(By.xpath("//tr[@class='row']/td/a[contains(text(),'" + zonesCountry + "')]")).click();

            By table = By.cssSelector("#table-zones");
            By rows = By.cssSelector("tr:not(.header)");

            List<WebElement> zones = driver.findElement(table).findElements(rows);
            zones.remove(zones.size() - 1); //remove last row (input control for adding new zone)

            List<String> listOfAllZones = new ArrayList<String>();

            for (WebElement zone : zones) {
                listOfAllZones.add(zone.findElement(By.xpath("./td[3]/input")).getAttribute("value"));
            }

            Assert.assertTrue("List of zones is not ordered for " + zonesCountry,
                    Ordering.from(String.CASE_INSENSITIVE_ORDER).isOrdered(listOfAllZones));
        }
    }

    @After
    public void shutDown() {
        driver.quit();
        driver = null;
    }

    private void performLogin() {
        By sidebar = By.id("sidebar");

        driver.navigate().to(baseURL + "/admin");
        if (isElementPresent(sidebar)) return;
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("login")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(sidebar));
    }

    private boolean isElementPresent(By element) {
        return driver.findElements(element).size() > 0;
    }
}
