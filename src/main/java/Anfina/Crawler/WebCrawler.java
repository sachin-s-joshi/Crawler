package Anfina.Crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebCrawler {

	private HashSet<String> links;

	public WebCrawler() {
		links = new HashSet<String>();
	}

	public static void main(String[] args) {

		System.setProperty("webdriver.gecko.driver", "./resources/geckodriver.exe");
		WebDriver d = new FirefoxDriver();
		d.get("https://portal-qa2.regence.com/");

		d.findElement(By.xpath("//*[@title='Sign in']")).click();
		d.findElement(By.name("USER")).sendKeys("testCISMDuser1");
		d.findElement(By.name("PASSWORD")).sendKeys("password$1", Keys.ENTER);

		new WebCrawler().getPageLinks(d.getCurrentUrl(), d);
	}

	public void getPageLinks(String URL, WebDriver d) {
		// 4. Check if you have already crawled the URLs
		// (we are intentionally not checking for duplicate content in this example)
		if (!links.contains(URL)) {
			try {
				// 4. (i) If not add it to the index
				if (links.add(URL)) {
					System.out.println(URL);
				}

				// 2. Fetch the HTML code
				List<WebElement> a = d.findElements(By.xpath("//a[@href!='#' and @href != '/' and @href!= '.png']"));

				// 5. For each extracted URL... go back to Step 4.
				for (WebElement page : a) {
					
					getPageLinks(page.getAttribute("href"), d);
				}
				}
		catch (Exception e) {
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}

	}

}
