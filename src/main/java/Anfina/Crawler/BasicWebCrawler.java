package Anfina.Crawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BasicWebCrawler {
	 private HashSet<String> links;
	private Map<String,String> loginCookies;
	private Response res;
	 static int count =1;
	    public BasicWebCrawler() {
	        links = new HashSet<String>();
	    }

	    public void getPageLinks(String URL) {
	        //4. Check if you have already crawled the URLs
	        //(we are intentionally not checking for duplicate content in this example)
	        if (!links.contains(URL)) {
	            try {
	                //4. (i) If not add it to the index
	                if (links.add(URL)) {
	                    System.out.println(URL);
	                }

	                //2. Fetch the HTML code
	                if(count==1) {
	                		res = (Response) Jsoup
	            		    .connect(URL)
	            		    .data("USER", "testCISMDuser1", "PASSWORD", "password$1")
	            		    .method(Method.POST)
	            		    .execute();
	                		count++;

	            		//This will get you cookies
	            		 loginCookies = res.cookies();

	                }	//And this is the easiest way I've found to remain in session
	            		Document document = Jsoup.connect(URL)
	            		      .cookies(loginCookies)
	            		      .get();
	            		
	               
	                //3. Parse the HTML to extract links to other URLs
	                Elements linksOnPage = document.select("a[href]");

	                //5. For each extracted URL... go back to Step 4.
	                for (Element page : linksOnPage) {
	                	if(page.attr("abs:href").contains("regence"))
	                		getPageLinks(page.attr("abs:href"));
	                }
	            } catch (IOException e) {
	                System.err.println("For '" + URL + "': " + e.getMessage());
	            }
	        }
	    }

	    public static void main(String[] args) {
	        //1. Pick a URL from the frontier
	        new BasicWebCrawler().getPageLinks("https://portal-qa2.regence.com");
	    }


}
