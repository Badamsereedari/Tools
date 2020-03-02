package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpRequest {

	private static void print(String msg) {
		System.out.println(msg);
	}

	public static void getHttpRequest(String loginName, String loginPassword) throws IOException {
		String ret = "";
		String cookie = "__RequestVerificationToken=JY8a2RvGwEdtufllyLZV1ssVQTQY0JUGDP7C0yB5NrJi76jKeNSrnu5-xFrjtYxCEs7tODx9UYW_gQ1_7GtG8eB2wCTryT1EbXbjASACYAU1; ASP.NET_SessionId=nzorrwmyd2am0ogcwqck2lg3";
		String checkUrl = "http://202.131.242.158:8080/Tasks/Login1";
		URL url = new URL(checkUrl);

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Accept", "application/json");
		httpConn.setRequestMethod("GET");
		httpConn.setRequestProperty("Cookie", cookie);

		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader((httpConn.getInputStream())));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				ret = ret + tmp;
			}
		}
		Document doc = Jsoup.parse(ret);
		Elements newsHeadlines = doc.select("input");
		String RVT = "";
		for (Element n : newsHeadlines) {
			Attributes attr = n.attributes();
			String name = attr.get("name");
			String value = attr.get("value");
			if (name != null && name.equals("__RequestVerificationToken")) {
				RVT = value;
				break;
			}
		}

		Map<String, String> arguments = new HashMap<>();
		arguments.put("__RequestVerificationToken", RVT);
		arguments.put("LoginName", loginName);
		arguments.put("LoginPass", loginPassword);
		StringJoiner sj = new StringJoiner("&");
		for (Map.Entry<String, String> entry : arguments.entrySet())
			sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setFixedLengthStreamingMode(length);
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Cookie", cookie);
		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpCon.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		httpCon.setRequestMethod("POST");

		try (OutputStream os = httpCon.getOutputStream()) {
			os.write(out);
		}

		responseCode = httpCon.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader((httpCon.getInputStream())));
			String tmp;
			ret = "";
			while ((tmp = br.readLine()) != null) {
				ret = ret + tmp;
			}
			String resp = ret.split("<div class=\"col-md-9\" style=\"color: red\">")[1]
					.split("                </div>")[0];
			print("userName: " + loginName + ", password: " + loginPassword + ", response: " + resp);
		} else {
			ret = "";
			print("responseCode: " + Func.toString(responseCode) + ", userName: " + loginName + ", password: "
					+ loginPassword);
		}
	}

}