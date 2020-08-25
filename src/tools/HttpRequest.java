package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpRequest {

	private static void print(String msg) {
		System.out.println(msg);
	}

	public static String getToken() throws IOException {
		String token = "";
		String ret = "";
		String cookie = "_pk_ref.1.b5c5=%5B%22%22%2C%22%22%2C1588772809%2C%22https%3A%2F%2Fwww.google.com%2F%22%5D; _pk_id.1.b5c5=12403fabb834030e.1588757233.2.1588772844.1588772809.; csrf_token=PeEkwhAo38wG08isGj242EaYLR4oZhSVkvekoAiFv10=; _pk_ses.2.b5c5=1; _pk_id.2.b5c5=cb84eb9631632d1f.1588930209.1.1588930713.1588930209.";
		String checkUrl = "https://www.chimege.mn/tts";
		URL url = new URL(checkUrl);

		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Cookie", cookie);
		httpConn.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		httpConn.setRequestMethod("GET");

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
		for (Element n : newsHeadlines) {
			Attributes attr = n.attributes();
			String name = attr.get("name");
			String value = attr.get("value");
			if (name != null && name.equals("csrf_token")) {
				token = value;
				break;
			}
		}

		return token;
	}

	public static Object[] getHttpRequest(String text, String token) throws IOException {
		Object[] result = new Object[2];
		String ret = "";
		disableSSL();

		String cookie = "_pk_ref.1.b5c5=%5B%22%22%2C%22%22%2C1588772809%2C%22https%3A%2F%2Fwww.google.com%2F%22%5D; _pk_id.1.b5c5=12403fabb834030e.1588757233.2.1588772844.1588772809.; csrf_token=PeEkwhAo38wG08isGj242EaYLR4oZhSVkvekoAiFv10=; _pk_ses.2.b5c5=1; _pk_id.2.b5c5=cb84eb9631632d1f.1588930209.1.1588930713.1588930209.";

		Map<String, String> arguments = new HashMap<>();
		arguments.put("csrf_token", token);
		arguments.put("txt", text);
		arguments.put("voice", "1");
		StringJoiner sj = new StringJoiner("&");
		for (Map.Entry<String, String> entry : arguments.entrySet())
			sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
		int length = out.length;

		URL url = new URL("https://www.chimege.mn/synth");
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setFixedLengthStreamingMode(length);
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Cookie", cookie);
		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpCon.setRequestProperty("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		httpCon.setRequestMethod("POST");

		try (OutputStream os = httpCon.getOutputStream()) {
			os.write(out);
		}

		int responseCode = httpCon.getResponseCode();

		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader((httpCon.getInputStream())));
			String tmp;
			ret = "";
			while ((tmp = br.readLine()) != null) {
				ret = ret + tmp;
			}

			Document doc = Jsoup.parse(ret);
			Elements newsHeadlines = doc.select("audio");
			String base64 = "";
			for (Element n : newsHeadlines) {
				Attributes attr = n.attributes();
				base64 = attr.get("src");
			}

			newsHeadlines = doc.select("input");
			for (Element n : newsHeadlines) {
				Attributes attr = n.attributes();
				String name = attr.get("name");
				String value = attr.get("value");
				if (name != null && name.equals("csrf_token")) {
					token = value;
					break;
				}
			}

			result[0] = base64.replaceAll("data:audio/wav;base64,", "");
			result[1] = token;
		} else {
			ret = "";
			print("responseCode: " + Func.toString(responseCode));
		}

		return result;
	}

	public static void disableSSL() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// Do nothing
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// Do nothing
				}
			} };

			HostnameVerifier hv = (hostname, session) -> hostname.equalsIgnoreCase(session.getPeerHost());

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());

			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

		} catch (Exception e) {
			print("Error: " + e.getMessage());
		}
	}
}