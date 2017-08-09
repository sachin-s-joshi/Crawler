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

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

public class BasicWebCrawler {
	private HashSet<String> links;
	//private String[] exclude = { "google", "twitter", "linkedin", "youtube","apple","itunes" };
	private Map<String, String> loginCookies;
	private Response res;
	private Fillo fillo;
	private Connection connection;
	private static String baseURL;
	static int count = 1;

	public BasicWebCrawler() {

			links = new HashSet<String>();
			baseURL = "https://portal-qa2.regence.com";
			//Now you can set table start row and column
			/*System.setProperty("ROW", "0");//Table start row
			System.setProperty("COLUMN", "0");*/
		

	}

	public void getPageLinks(String URL) {
		// 4. Check if you have already crawled the URLs

		
		if (!links.contains(URL) && count<2) {
			try {
				// 4. (i) If not add it to the index
				fillo = new Fillo();
				connection = fillo.getConnection("./resources/results.xlsx");
				if (links.add(URL)) {
					try {
						
						System.out.println(URL);
						//String strQuery="insert into Sheet1(URL,status) values('url','pass')";
						String strQuery = "insert into Sheet1(URL,status) values("+"\'"+URL+"\'"+",'Pass')";
						connection.executeUpdate(strQuery);
						connection.close();
						

						
					} catch (FilloException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				// 2. Fetch the HTML code
				if (count == 1) {
					res = (Response) Jsoup
							.connect(URL)
							.data("USER", "testCISMDuser1", "PASSWORD","password$1").method(Method.POST).execute();
					count++;
					// This will get you cookies
					loginCookies = res.cookies();
				}
				
				// And this is the easiest way I've found to remain in session
				Document document = Jsoup.connect(URL).cookies(loginCookies)
						.get();

				// 3. Parse the HTML to extract links to other URLs
				Elements linksOnPage = document.select("a[href]");

				// 5. For each extracted URL... go back to Step 4.
				int i = 0;
				for (Element page : linksOnPage) {
					String newLink = page.attr("abs:href");
					if (newLink.contains(baseURL.split("\\.")[0]) && !newLink.contains("#")) {
							
							getPageLinks(newLink);
					}

				}
			} 
			catch (Exception e) {
				try {
					
					String strQuery = "insert into Sheet1 values("+"\'"+URL+"\'"+","+e.getMessage()+")";
					connection.executeUpdate(strQuery);
					connection.close();
					System.err.println("For '" + URL + "': " + e.getMessage());
				} catch (FilloException ex) {
				
					ex.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		// 1. Pick a URL from the frontier

		BasicWebCrawler bw=new BasicWebCrawler();
		bw.getPageLinks(baseURL);
		//bw.connection.close();
		System.out.println("Connection closed" );
		
	}

}
