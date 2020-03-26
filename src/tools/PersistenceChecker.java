package tools;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PersistenceChecker {
	static String fileName = "fileNames.txt";
	static String errLayer = "ERR";
	static HashMap<String, List<String>> hashFileNames = new HashMap<>();

	public static void main(String[] args) {

		hashFileNames.clear();
		String path = System.getProperty("user.dir");

		// Параметрээр зам дамжуулсан бол авах
		if (null != args && args.length > 0) {
			path = args[0];
		}

		goAll(path);
	}

	private static void goAll(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				try {
					checkEntities(listOfFiles[i].getPath());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	private static File findFolder(File root, String fileName) {
		if (root.getAbsolutePath().contains(".EJB") && root.getName().equals(fileName)) {
			return root;
		}

		File[] files = root.listFiles();

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					File file = findFolder(f, fileName);
					if (file != null) {
						return file;
					}
				}
			}
		}
		return null;
	}

	public static void checkEntities(String path) {
		// фолдер олдохгүй эсвэл фолдер биш бол үйлдэл дуусна

		File rootFolder = new File(path);
		if (!rootFolder.exists() || !rootFolder.isDirectory()) {
			System.out.println("Зам буруу байна!");
			return;
		}

		// Entity нэрүүдийг цуглуулах
		List<String> lstClassPath = new ArrayList<>();
		File entityFolder = findFolder(rootFolder, "entity");
		if (null != entityFolder) {
			// Файлын жагсаалт авах
			File[] arrEntities = entityFolder.listFiles();
			Arrays.sort(arrEntities);
			if (null != arrEntities && arrEntities.length > 0) {
				for (File entity : arrEntities) {
					String entityPath = entity.getAbsolutePath();
					int indx = entityPath.indexOf("\\mn\\");
					entityPath = entityPath.substring(indx + 1).replace("\\", ".").replace(".java", "");
					lstClassPath.add(entityPath);
				}
			}
		}
		// persistence.xml-г унших
		File metaInfFolder = findFolder(rootFolder, "META-INF");
		File file = new File(metaInfFolder.getAbsolutePath() + File.separator + "persistence.xml");
		List<String> headerLines = new ArrayList<>();
		List<String> footerLines = new ArrayList<>();
		String tab = "";
		try {
			// Файлын бүх мөрийг уншиж авах
			List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			boolean header = true;
			String chkString = "</persistence-unit>";
			for (String line : allLines) {
				if (line.contains("<properties>")) {
					chkString = "<properties>";
					break;
				}
			}
			for (String line : allLines) {
				// entity зарласан мөрүүдийг алгасах.
				if (!line.trim().startsWith("<class>")) {
					// persistence-unit ээр 2 хувааж header, footer хэсэгт авах
					if (header && !line.contains(chkString)) {
						headerLines.add(line);
					} else {
						// шинэ мөрийг нэг шугаманд оруулахын тулд таб-н хэмжээг авах
						if (line.contains(chkString)) {
							tab = line.substring(0, line.indexOf(chkString));
						}
						header = false;
						footerLines.add(line);
					}
				}
			}
			// header + entities + footer
			List<String> writeLines = new ArrayList<>();
			writeLines.addAll(headerLines);
			for (String classPath : lstClassPath) {
				writeLines.add(tab + "<class>" + classPath + "</class>");
			}
			writeLines.addAll(footerLines);
			// Файлруу бичих
			Files.write(file.toPath(), writeLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
