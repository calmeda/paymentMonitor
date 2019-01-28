package ph.almeda.payment.monitor.view;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupHttpPost {

	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:65.0) Gecko/20100101 Firefox/65.0";
	private final String urlPost = "https://api.semaphore.co/api/v4/messages";

	// main class
	public static void main(String[] args) throws Exception {
		JSoupHttpPost http = new JSoupHttpPost();
		http.sendPost();
		System.out.println("JSoupHttpPost ");
	}

	// HTTP Post request
	private void sendPost() throws Exception {

		Map<String, String> postData = new HashMap<String, String>();
		postData.put("apikey", "7ec6d99734d54fecd26d441cf6738481");
		postData.put("number", "09209382483");
		postData.put("message", "JSoupPost");

		Document doc = Jsoup.connect(urlPost).ignoreContentType(true).userAgent(USER_AGENT).data(postData).post();
		System.out.println("JSoupHttpPost " + doc.getAllElements());
	}
}