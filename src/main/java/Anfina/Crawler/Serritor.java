package Anfina.Crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.github.peterbencze.serritor.api.BaseCrawler;
import com.github.peterbencze.serritor.api.CrawlRequest;
import com.github.peterbencze.serritor.api.CrawlRequest.CrawlRequestBuilder;
import com.github.peterbencze.serritor.api.HtmlResponse;
import com.github.peterbencze.serritor.api.NonHtmlResponse;
import com.github.peterbencze.serritor.api.UnsuccessfulRequest;

public class Serritor extends BaseCrawler {
	
	public static int count=1;
	private static FirefoxDriver d;
	public Serritor(String url) {
		// Enable offsite request filtering
        config.setOffsiteRequestFiltering(true);

        // Add a crawl seed, this is where the crawling starts
        CrawlRequest request = new CrawlRequestBuilder(url).build();
        config.addCrawlSeed(request);
	}
	@Override
    protected void onResponseComplete(final HtmlResponse response) {
        // Crawl every link that can be found on the page
		
		
        response.getWebDriver().findElements(By.tagName("a"))
                .stream()
                .forEach((WebElement link) -> {
                	System.out.println("Visited: "+ link);
                    CrawlRequest request = new CrawlRequestBuilder(link.getAttribute("href")).build();
                    crawl(request);
                });
    }
	
@Override
protected void onBegin() {
	if(count==1) {
		
		
	}
	
}
    @Override
    protected void onNonHtmlResponse(final NonHtmlResponse response) {
        System.out.println("Received a non-HTML response from: " + response.getCrawlRequest().getRequestUrl());
    }

    @Override
    protected void onUnsuccessfulRequest(final UnsuccessfulRequest request) {
        System.out.println("Could not get response from: " + request.getCrawlRequest().getRequestUrl());
    }

 public static void main(String args[]) {
	 
	  System.setProperty("webdriver.gecko.driver","./resources/geckodriver.exe");
	  d= new FirefoxDriver();
	  d.get("https://portal-qa2.regence.com/");
		d.findElement(By.xpath("//*[@title='Sign in']")).click();
		d.findElement(By.name("USER")).sendKeys("testCISMDuser1");
		d.findElement(By.name("PASSWORD")).sendKeys("password$1",Keys.ENTER);
		count++;
		Serritor s= new Serritor(d.getCurrentUrl().toString());
		s.start(d);
 }
}
	
	

