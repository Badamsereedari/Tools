package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class Main {

	public static void main(String[] args) throws Exception {

		String filePath = "C:\\Users\\badamsereedari.t\\Documents\\GEN_VIEW";
		String moduleCode = "gen.s";
		/**
		 * 20 - Table, 30 - Type, 40 - Функц, 50 - View, 60 - Procedure, 70 - Package,
		 * 80 - Data (constants, configs etc), 90 - Бусад (шаардлагатай үед ашиглана)
		 */

		String subLayer = "50";

		chgFileType(filePath, moduleCode);
		addAndGetTimestamp(filePath, subLayer);
	}

	// regex шалгах
	public static void regexChecker() {
		String text = "select :POLICY_CODE, :p2, :MTRX|DAY|AGE from Customer where name = :pp3 ";
		String ptrn = "(\\:[\\w|]+)";

		regexChecker(text, ptrn);
	}

	public static void regexChecker(String text, String pattern) {
		List<String> parameters = new ArrayList<>();
		Pattern ptrn = Pattern.compile(pattern);
		Matcher m = ptrn.matcher(text);
		while (m.find()) {
			String match = m.group(1);
			match = match.replaceAll("\\:", "");
			parameters.add(match);
		}
		System.out.println("Parameters: " + parameters);
		text = text.replaceAll("\\|", "");
		print(text);
	}

	// Төрсөн өдөр тооцох
	public static int calculateAge(Date birthDate) {
		int years = 0;
		int months = 0;

		Calendar birthDay = Calendar.getInstance();
		birthDay.setTimeInMillis(birthDate.getTime());

		long currentTime = System.currentTimeMillis();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(currentTime);

		years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		int currMonth = now.get(Calendar.MONTH) + 1;
		int birthMonth = birthDay.get(Calendar.MONTH) + 1;

		months = currMonth - birthMonth;

		if (months < 0) {
			years--;
			months = 12 - birthMonth + currMonth;
			if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
				months--;
		} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
			years--;
			months = 11;
		}

		if (now.get(Calendar.DATE) == birthDay.get(Calendar.DATE)) {
			if (months == 12) {
				years++;
				months = 0;
			}
		}
		return years;
	}

	// Trunc round
	public static double truncate(double x) {
		return Math.floor(x * 100) / 100;
	}

	// Нэг мөрөнд оруулах
	public static void oneline() {
		String filePath = "D:\\nes-server\\asr.b\\db\\20200206120100_asr.b_add_pl_oper.sql";
		oneline(filePath);
	}

	public static void oneline(String filePath) {

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
					sql = sql + "\r\n";
					isFirstLine = true;
				}
			} else {
				sql = newLine;
			}
		}
		sql = sql.trim();

		writeToFile(filePath, sql);
	}

	// Файлаас мөр өөр нь салгаж жагсаалт болгох
	public static List<String> readFileInList(String fileName) {

		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	// Файл руу текс бичих
	public static void writeToFile(String filePath, String text) {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
			bw.write(text);
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + filePath + "'");
		}
	}

	// view дотхор комент, BEQUEATH DEFINER хэсгийг хасах
	public static void cleanAndWriteSrcFile(String filePath, String startString) throws FileNotFoundException {
		cleanAndWriteSrcFile(filePath, startString, false);
	}

	public static void cleanAndWriteSrcFile(String filePath, String startString, boolean removeComment)
			throws FileNotFoundException {
		String sql = "";

		List<String> l = readFileInList(filePath);

		Iterator<String> itr = l.iterator();
		while (itr.hasNext()) {
			if (sql != null && !sql.equals("")) {
				sql = sql + "{newLine}" + itr.next();
			} else {
				sql = itr.next();
			}
		}

		if (removeComment) {
			sql = removeWithRegex(sql, "/*", "*/");
		}
		sql = removeWithRegex(sql, "(", "BEQUEATH DEFINER");
		sql = sql.replace(";", "");
		sql = sql.trim();

		sql = sql.replace("{newLine}", "\r\n");

		sql = startString + "\r\n" + sql;

		writeToFile(filePath, sql);
	}

	// өгөгдсөн фатерны хоорондохыг цэвэрлэх
	public static String removeWithRegex(String str, String p1, String p2) {
		String regexString = Pattern.quote(p1) + "(.*?)" + Pattern.quote(p2);

		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			String textInBetween = matcher.group(1);

			str = str.replace(p1 + textInBetween + p2, "");
		}
		return str;
	}

	// base64 энкод хийх
	public static String encodeAsBase64(byte[] src) {
		return Base64.getEncoder().encodeToString(src);
	}

	// base64 дэкод хийх
	public static byte[] decodeFromBase64(String src) throws IOException {
		return Base64.getDecoder().decode(src);
	}

	// Текстээс огноо гаргаж авах
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

	// file-ын урд src нэмж байгаа
	public static void chgFileType(String filePath, String moduleCode) throws IOException {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Path copied = Paths
						.get(filePath + File.separator + "src_" + moduleCode + "_" + listOfFiles[i].getName());
				Path originalPath = listOfFiles[i].toPath();
				try {
					Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// timestamp нэмэх
	public static void addAndGetTimestamp(String filePath, String subLayer) throws IOException {
		addAndGetTimestamp(filePath, subLayer, true);
	}

	public static void addAndGetTimestamp(String filePath, String subLayer, boolean isView) throws IOException {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String[] fileName = listOfFiles[i].getName().split("\\.sql");

				String timestamp = fileName[0] + "$" + subLayer + Func.toDateTimeStr(new Date(), "yyyyMMddHHmmss");
				String startup = '"' + timestamp + '"' + ",";
				timestamp = "-- " + timestamp;
				cleanAndWriteSrcFile(listOfFiles[i].getPath(), timestamp, isView);

				print(startup);
			}
		}
	}

	// тайлангын файлын зам авах
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

	// тайлангын файлын зам солих
	public static void rptChgFolder() {
		String rptPath = "C:\\Users\\badamsereedari.t\\Documents\\test\\CBS_STANDARD";
		rptChgFolder(rptPath);
	}

	public static void rptChgFolder(String rptPath) {

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

	// KENDO маст шалгалт
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

	// html -> pdf
	public static String htmlToPdf() {
		String htmlPath = "D:\\Workspace\\VAT_EMAIL_2019-12-02_09-40-02_25.HTML";
		String pdfPath = "D:\\Workspace\\PDF_test.pdf";

		return htmlToPdf(htmlPath, pdfPath);
	}

	public static String htmlToPdf(String htmlPath, String pdfPath) {
		Document document = new Document();
		// step 2
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			// step 3
			document.open();
			// step 4
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlPath));
			// step 5
			document.close();
		} catch (Exception e1) {
			print(e1.getMessage());
		}

		return pdfPath;
	}
}