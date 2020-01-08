package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Main {

	public static void main(String[] args) throws Exception {
//		String formula = "SELECT CASE WHEN :AGE < 80 " + "          THEN :MATRIX|CNTRY|AGE " + "          ELSE "
//				+ "             CASE WHEN :AGE >= 80 AND :AGE < 160  " + "             THEN 2 * :MATRIX "
//				+ "             ELSE :MATRIX " + "             END " + "       END " + "          RESULT "
//				+ "  FROM DUAL";
//		List<String> params = getParamsFromFormula(formula);
//		for (String p : params) {
//			if (p.contains("MATRIX")) {
//				System.out.println(p);
//			}
//		}

//		String a = "a|a";
//		String[] mParams = a.split("\\|");
//		if (mParams.length > 0) {
//			System.out.println(a);
//		}
//
//		String dateString = "04-JAN-17";
//		System.out.println("Date: " + str2Date(dateString));

//		rptChgFolder();
//		int Y3 = 2017;

//		long diff = new TimeSpan().dateDiff(Func.str2Date("2017-08-15"), Func.str2Date("2017-08-20")).getAllDays();

//		int days = new TimeSpan().dateDiff(Func.str2Date("2000-01-01"), Func.str2Date("2019-10-21")).getAllDays();
//
//		String daysStr = "";
//		daysStr = String.valueOf(days);
//		while (daysStr.length() < 4) {
//			daysStr = "0" + daysStr;
//		}
//
//		if (daysStr.length() > 4) {
//			daysStr = daysStr.substring(0, 4);
//		}

//		print("isTrue: " + (a != null && !a.equals("")));

//		oneline();
//		readFromFile("-- SRC_VW_ASR_ACNT$20191029172111.sql");

//		String data = "MM00000000";
//		String regex = kendoMaskToRegex(data);
//		System.out.println(regex);
//
//		Pattern pattern = Pattern.compile(regex);
//
//		Matcher matcher = pattern.matcher("RЭ90010101");
//
//		if (matcher.matches()) {
//			System.out.println("matched");
//		} else {
//			System.out.println("not matched");
//		}

//		for (int i = 0; i < special.length(); i++) {
//			print(Func.toString(special.charAt(i)));
//			getHttpRequest("Administrator", "UBMongolia1234" + special.charAt(i));
//		}

//		String[] token1 = { "administrator", "system", "admin", "nes", "gcm", "posgres", "grapecity", "5DORRSH", "Mongolia@1", "UBMongolia1234" };
//		String[] token2 = { "A7FBF607C0D37FED2AB4A4B5DFB7B8A04C1A0D37", "Mongolia@1", "UBMongolia1234", "nes", "gcm" };
//
//		for (int i = 0; i < 10; i++) {
//			for (int j = 0; j < 5; j++) {
//				String name = token1[i];
//				String password = token2[j];
//				getHttpRequest(name, password);
//			}
//		}
//		String special = "0123456789@abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//		for (int i = 0; i < special.length(); i++) {
//			String str = Func.toString(special.charAt(i));
//			print(str);
//			getHttpRequest("posgres' and password like '%" + str + "%' --", "asdfasd");
//		}
//		getHttpRequest("admin", "UBMongolia1234");

//		String rptPath = "C:\\Users\\badamsereedari.t\\Documents\\Reports";
//		getRptPath(rptPath);

//		rptChgFolder();

//		BigDecimal ctBa = new BigDecimal("625959.47");
//		BigDecimal dtBa = new BigDecimal("-625959.4725");
//
//		dtBa = Func.round(
//				Func.divBigDec(Func.round(Func.multBigDec(dtBa, new BigDecimal("1000")), 2, BigDecimal.ROUND_DOWN),
//						new BigDecimal("1000")),
//				2, BigDecimal.ROUND_DOWN);
//
//		if (Func.different(Func.addBigDec(ctBa, dtBa), BigDecimal.ZERO)) {
//			print("ctBa amount: " + ctBa + ", dtBa amount: " + dtBa);
//		} else {
//			print("Done");
//		}

//		generateDoc();

		oneline();
	}

	public static double truncate(double x) {
		return Math.floor(x * 100) / 100;
	}

	public static void oneline() {
		String filePath = "D:\\nes-server\\asr.b\\db\\20191217190100_asr.b_add_autonum_ptrn.sql";

		String sql = "";

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		boolean isFirstLine = true;
		while (itr.hasNext()) {
			boolean isLastLine = false;
			String newLine = itr.next();
			newLine = newLine.replaceAll("\\s+", " ");

			if (newLine.contains("~~")) {
				isLastLine = true;
			}

			if (sql != null && !sql.equals("")) {
				if (isFirstLine) {
					sql = sql + " " + newLine;
				} else {
					sql = sql + newLine;
					isFirstLine = false;
				}
				if (isLastLine) {
					sql = sql + "\n";
					isFirstLine = true;
				}
			} else {
				sql = newLine;
			}
		}
		sql = sql.trim();

		writeToFile(filePath, sql);
	}

	public static List<String> readFileInList(String fileName) {

		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		}

		catch (IOException e) {

			// do something
			e.printStackTrace();
		}
		return lines;
	}

	public static void writeToFile(String filePath, String str) {
		try {
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter(filePath);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Note that write() does not automatically
			// append a newline character.
			bufferedWriter.write(str);

			// Always close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + filePath + "'");
		}
	}

	public static void readFromFile(String filePath, String startString) throws FileNotFoundException {
//		String filePath = "C:\\Users\\badamsereedari.t\\Documents\\asr_view\\SRC_VW_ASR_ACNT.sql";

		String sql = "";
//		File file = new File(filePath);
//		Scanner sc = new Scanner(file);
//
//		// we just need to use \\Z as delimiter
//		sc.useDelimiter("\\Z");
//
//		System.out.println(sc.next());

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		while (itr.hasNext()) {
			if (sql != null && !sql.equals("")) {
				sql = sql + "{newLine}" + itr.next();
			} else {
				sql = itr.next();
			}
		}

		sql = removeWithRegex(sql, "/*", "*/");
		sql = removeWithRegex(sql, "(", "BEQUEATH DEFINER");
		sql = sql.replace(";", "");
		sql = sql.trim();

		sql = sql.replace("{newLine}", "\n");

		sql = startString + "\n" + sql;

		writeToFile(filePath, sql);
	}

	public static String removeWithRegex(String str, String p1, String p2) {
		String regexString = Pattern.quote(p1) + "(.*?)" + Pattern.quote(p2);

		Pattern pattern = Pattern.compile(regexString);
		// text contains the full text that you want to extract data
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			String textInBetween = matcher.group(1); // Since (.*?) is capturing group 1
			// You can insert match into a List/Collection here

			str = str.replace(p1 + textInBetween + p2, "");
		}
		return str;
	}

	public static String encodeAsBase64(byte[] src) {
		return Base64.getEncoder().encodeToString(src);
	}

	public static byte[] decodeFromBase64(String src) throws IOException {
		return Base64.getDecoder().decode(src);
	}

	public static List<String> getParamsFromFormula(String formula) {
		List<String> params = new ArrayList<>();
		String regexString = ":(.*?) ";
		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(formula);

		while (matcher.find()) {
			params.add(matcher.group(1));
		}

		Set<String> set = new LinkedHashSet<>();
		set.addAll(params);

		params.clear();

		params.addAll(set);

		return params;
	}

	public static Date str2Date(String pDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
		Date dt = null;
		try {
			dt = formatter.parse(pDate);
		} catch (ParseException ex) {
			System.out.println(String.format("data: %s, err: %s", pDate, ex.getMessage()));
		}
		return dt;
	}

	public static void chgFileType() throws IOException {
		String filePath = "C:\\Users\\badamsereedari.t\\Documents\\asr view";

		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
//				Path copied = Paths.get(filePath + File.separator + "SRC_" + listOfFiles[i].getName());
				Path originalPath = listOfFiles[i].toPath();
//				try {
//					Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				String[] fileName = listOfFiles[i].getName().split("\\.");

				String timestamp = fileName[0] + "$" + Func.toDateTimeStr(new Date(), "yyyyMMddHHmmss") + ".sql";
				String startup = '"' + timestamp + '"' + ",";
				timestamp = "-- " + timestamp;
				readFromFile(listOfFiles[i].getPath(), timestamp);

				print(startup);
			}
		}
	}

	public static void getRptPath(String rptPath) {
		File folder = new File(rptPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getPath().contains(".prpt")) {
				String ret = listOfFiles[i].getPath().replace("C:\\Users\\badamsereedari.t\\Documents\\", "");
				print(ret);
			} else if (listOfFiles[i].isDirectory()) {
				getRptPath(listOfFiles[i].getPath());
			}
		}
	}

	public static void rptChgFolder() {
		String rptPath = "C:\\Users\\badamsereedari.t\\Documents\\test\\CBS_STANDARD";

		File folder = new File(rptPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

			} else if (listOfFiles[i].isDirectory()) {
				rptChgFolderSingle(listOfFiles[i].getPath(), rptPath);
			}
		}
	}

	private static void rptChgFolderSingle(String path, String rootPath) {
		String prevFolder = path;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		boolean hasFile = false;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				print(listOfFiles[i].getName());
				copyToPrevFolder(listOfFiles[i], rootPath);
				hasFile = true;
			} else if (listOfFiles[i].isDirectory()) {
				rptChgFolderSingle(listOfFiles[i].getPath(), rootPath);
			}
		}

		if (hasFile) {
			deleteFolder(prevFolder);
		}
	}

//	private static void copyToPrevFolder(File file, String rootPath) {
//		String copyPath = file.getParentFile().getName();
//
//		if (copyPath.equals(rootPath)) {
//			print("same path no copy");
//		} else {
//			file.renameTo(new File("copyPath" + File.separator + file.getName()));
//		}
//
//	}

	private static void copyToPrevFolder(File file, String rootPath) {
		String copyPath = file.getParentFile().getParentFile().getPath();
		if (!rootPath.equals(copyPath)) {
			Path copied = Paths.get(copyPath + File.separator + file.getName());
			Path originalPath = file.toPath();
			try {
				Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void deleteFolder(String path) {
		File file = new File(path);

//		if (file.delete()) {
//			System.out.println("File deleted successfully! path: " + path);
//		} else {
//			System.out.println("Failed to delete the file! path: " + path);
//		}

		deleteDir(file);
	}

	private static void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	private static void print(String msg) {
		System.out.println(msg);
	}

	public static String kendoMaskToRegex(String kendoMask) {

		StringBuilder sbRegex = new StringBuilder();
		int count = 0;
		char lastChar = Character.MIN_VALUE;
		if (null != kendoMask && !kendoMask.isEmpty()) {
			for (char ch : kendoMask.toCharArray()) {
				if (ch == lastChar) {
					count++;
				} else {
					sbRegex = new StringBuilder(sbRegex.toString().replace("COUNT", String.valueOf(count)));
					count = 0;
					switch (ch) {
					// Тоо. 0 -с 9 хүртэл цифр заавал оруулна.
					case '0':
						sbRegex.append("(\\d){COUNT}");
						break;
					// Тоо болон хоосон зай. 0 -с 9 хүртэлх цифр оруулж болно.
					case '9':
						sbRegex.append("(\\d|\\s){COUNT}");
						break;
					// Тоо болон хоосон зай. Дээрх "9" гэсэн дүрэмтэй ижил, харин дээр нь нэмээд (+)
					// болон (-) тэмдгүүдийг оруулж болно.
					case '#':
						sbRegex.append("(\\d|\\s|+|-){COUNT}");
						break;
					// Үсэг. Жижиг (а-я) болон ТОМ (А-Я) үсэг заавал оруулна.
					case 'M':
						sbRegex.append("[а-яА-Я]{COUNT}");
						break;
					case 'L':
						sbRegex.append("[а-яА-Яa-zA-Z]{COUNT}");
						break;
					// Үсэг болон хоосон зай. Жижиг (а-я) болон ТОМ (А-Я) үсэг оруулж болно.
					case '?':
						sbRegex.append("[а-яА-Яa-zA-Z|\\s]{COUNT}");
						break;
					// Тэмдэгт. Бүх төрлийн тэмдэгт эсвэл хоосон зай заавал оруулна.
					case '&':
						sbRegex.append("(\\S){COUNT}");
						break;
					// Тэмдэгт эсвэл хоосон зай. Бүх төрлийн тэмдэгт оруулж болно.
					case 'C':
						sbRegex.append("(.){COUNT}");
						break;
					// Тоо болон үсэг. Зөвхөн үсэг юм уу эсвэл тоо оруулна.
					case 'A':
						sbRegex.append("[а-яА-Яa-zA-Z0-9]{COUNT}");
						break;
					// Тоо, үсэг, болон хоосон зай. Зөвхөн үсэг юм уу тоо эсвэл хоосон зай оруулж
					// болно.
					case 'a':
						sbRegex.append("[а-яА-Яa-zA-Z0-9|\\s]{COUNT}");
						break;
					// Бутархайн эсвэл мянганы орон, он сар өдөр, тусгаарлагч.
					case '.':
					case ',':
					case ':':
					case ';':
					case '-':
					case '/':
					case '$':
					case '(':
					case ')':
						sbRegex.append("(\\").append(ch).append("){COUNT}");
						break;
					default:
						sbRegex.append("(").append(ch).append("){COUNT}");
						break;
					}
					lastChar = ch;
					count++;
				}
			}
			if (count > 0) {
				sbRegex = new StringBuilder(sbRegex.toString().replace("COUNT", String.valueOf(count)));
			}
		}

		return sbRegex.toString();
	}

//	public static void getHttpRequest(String loginName, String loginPassword) throws IOException {
//		String ret = "";
//		String cookie = "__RequestVerificationToken=JY8a2RvGwEdtufllyLZV1ssVQTQY0JUGDP7C0yB5NrJi76jKeNSrnu5-xFrjtYxCEs7tODx9UYW_gQ1_7GtG8eB2wCTryT1EbXbjASACYAU1; ASP.NET_SessionId=nzorrwmyd2am0ogcwqck2lg3";
//		String checkUrl = "http://202.131.242.158:8080/Tasks/Login1";
//		URL url = new URL(checkUrl);
//
//		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//		httpConn.setDoOutput(true);
//		httpConn.setDoInput(true);
//		httpConn.setRequestProperty("Accept", "application/json");
//		httpConn.setRequestMethod("GET");
//		httpConn.setRequestProperty("Cookie", cookie);
//
//		int responseCode = httpConn.getResponseCode();
//
//		if (responseCode == HttpURLConnection.HTTP_OK) {
//			BufferedReader br = new BufferedReader(new InputStreamReader((httpConn.getInputStream())));
//			String tmp;
//			while ((tmp = br.readLine()) != null) {
//				ret = ret + tmp;
//			}
//		}
//		Document doc = Jsoup.parse(ret);
//		Elements newsHeadlines = doc.select("input");
//		String RVT = "";
//		for (Element n : newsHeadlines) {
//			Attributes attr = n.attributes();
//			String name = attr.get("name");
//			String value = attr.get("value");
//			if (name != null && name.equals("__RequestVerificationToken")) {
//				RVT = value;
//				break;
//			}
//		}
//
//		Map<String, String> arguments = new HashMap<>();
//		arguments.put("__RequestVerificationToken", RVT);
//		arguments.put("LoginName", loginName);
//		arguments.put("LoginPass", loginPassword);
//		StringJoiner sj = new StringJoiner("&");
//		for (Map.Entry<String, String> entry : arguments.entrySet())
//			sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
//		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
//		int length = out.length;
//
//		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
//		httpCon.setFixedLengthStreamingMode(length);
//		httpCon.setDoOutput(true);
//		httpCon.setRequestProperty("Cookie", cookie);
//		httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		httpCon.setRequestProperty("Accept",
//				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
//		httpCon.setRequestMethod("POST");
//
//		try (OutputStream os = httpCon.getOutputStream()) {
//			os.write(out);
//		}
//
//		responseCode = httpCon.getResponseCode();
//
//		// always check HTTP response code first
//		if (responseCode == HttpURLConnection.HTTP_OK) {
//			BufferedReader br = new BufferedReader(new InputStreamReader((httpCon.getInputStream())));
//			String tmp;
//			ret = "";
//			while ((tmp = br.readLine()) != null) {
//				ret = ret + tmp;
//			}
//			String resp = ret.split("<div class=\"col-md-9\" style=\"color: red\">")[1]
//					.split("                </div>")[0];
//			print("userName: " + loginName + ", password: " + loginPassword + ", response: " + resp);
////			if (!resp.equals("Чадсангүй")) {
////				print("userName: " + loginName + ", password: " + loginPassword + ", response: " + resp);
////			}
//		} else {
//			ret = "";
//			print("responseCode: " + Func.toString(responseCode) + ", userName: " + loginName + ", password: "
//					+ loginPassword);
//		}
//	}

	public static String generateDoc() {
		String path = "D:\\Workspace\\VAT_EMAIL_2019-12-02_09-40-02_25.HTML";
		String pdfPath = "D:\\Workspace\\PDF_test.pdf";
		Document document = new Document();
		// step 2
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			// step 3
			document.open();
			// step 4
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(path));
			// step 5
			document.close();
		} catch (Exception e1) {
			print(e1.getMessage());
		}

		return pdfPath;
	}
}