package tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Base64;

public class Func {

	private static final Random random = new Random();

	// ===== STATIC ===============

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String DATE_TIME_FORMAT_DOT = "yyyy.MM.dd HH:mm:ss";
	private static final String DATE_FORMAT10_DOT = "yyyy.MM.dd";
	private static final String DATE_FORMAT24 = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String DATE_FORMAT23 = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String DATE_FORMAT23_DOT = "yyyy.MM.dd HH:mm:ss.SSS";
	private static final String DATE_FORMAT22 = "yyyy-MM-dd HH:mm:ss.SS";
	private static final String DATE_FORMAT21 = "yyyy-MM-dd HH:mm:ss.S";
	private static final String DATE_FORMAT21_DOT = "yyyy.MM.dd HH:mm:ss.S";
	private static final String DATE_FORMAT28 = "EEE MMM dd HH:mm:ss zzz yyyy";
	private static final String DATE_FORMAT29 = "EEE MMM d HH:mm:ss zzz yyyy";
	private static final String DATE_FORMAT30 = "EEE MMM d HH:mm:ss zzzz yyyy";
	private static final String DATE_FORMAT34 = "EEE MMM d HH:mm:ss zzzz yyyy";
	private static final String DATE_FORMAT19_T = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE_FORMAT20_T_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_FORMAT23_S = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	private static final String DATE_FORMAT25_T_X = "yyyy-MM-dd'T'HH:mm:ssXXX";
	private static final String DATE_FORMAT6 = "yyMMdd";
	private static final String DATE_FORMAT8 = "HH:mm:ss";
	private static final String DATE_FORMAT10 = "yyMMddHHmm";
	private static final String DATE_FORMAT12 = "yyMMddHHmmss";

	public static final String RELATIONAL_OPER_EQUAL = "="; // Тэнцүү
	public static final String RELATIONAL_OPER_GREATER = ">";// Их
	public static final String RELATIONAL_OPER_LESS = "<";// Бага
	public static final String RELATIONAL_OPER_GREATER_OR_EQUAL = ">=";// Их
																		// буюу
																		// тэнцүү
	public static final String RELATIONAL_OPER_LESS_OR_EQUAL = "<=";// Бага буюу
	// тэнцүү
	public static final String RELATIONAL_OPER_NOT_EQUAL = "<>";// Ялгаатай
	public static final String RELATIONAL_OPER_BETWEEN = "BETWEEN";// Хооронд

	public static final String CLASS_NAME_BOOLEAN = "Boolean";
	public static final String CLASS_NAME_BOOL = "boolean";
	public static final String CLASS_NAME_INTEGER = "Integer";
	public static final String CLASS_NAME_INT = "int";
	public static final String CLASS_NAME_LONG = "Long";
	public static final String CLASS_NAME_L = "long";
	public static final String CLASS_NAME_DOUBLE = "Double";
	public static final String CLASS_NAME_DBL = "double";
	public static final String CLASS_NAME_FLOAT = "Float";
	public static final String CLASS_NAME_F = "float";
	public static final String CLASS_NAME_BIGDECIMAL = "BigDecimal";
	public static final String CLASS_NAME_BYTE = "Byte";
	public static final String CLASS_NAME_BT = "byte";
	public static final String CLASS_NAME_STRING = "String";
	public static final String CLASS_NAME_DATE = "Date";
	public static final String CLASS_NAME_CHAR = "Char";
	public static final String CLASS_NAME_CH = "char";
	public static final String CLASS_NAME_TIME_STAMP = "Timestamp";

	public static boolean diagMode = false;
	public static boolean logSlowSql = false;
	public static int logSlowSqlTime = 3 * 1000; // ms
	public static String serverMode = "PROD";

	private static final String FALSE_STR = "false";
	private static final String INDEX_STR = "Индекс: ";
	private static final String MINUS_STR = "minus";
	private static final String NES_BIN = "nes.bin";
	private static final String USER_DIR = "user.dir";
	private static final String UNIT_0 = "unit_0";
	private static final String UNIT_1 = "unit_1";
	private static final String CONTENT_CORMAT = "text/html; charset=\"utf-8\"";
	private static final String STP_THOUSAND = "stp_thousand";
	private static final String BIG_THOUSAND = "big_thousand";
	private static final String ONE_HUNDRED = "one_hundred";
	private static final String BIG_HUNDRED = "big_hundred";
	private static final String BIG_MILLION = "big_million";
	private static final String BIG_BILLION = "big_billion";
	private static final String FRACTION = "fraction";

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";

	private static final String UTF_8 = "UTF-8";
	private static final String AMP_STR = "&amp;";

	private static final String WRITE_EXCEL_LOG_FILE = "WriteExcel";

	private static boolean wroteInfo = false;

	// Өгөгдсөн огноонд өгөгдсөн сарыг нэмэх
	public static Date addMonths(Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, months);

		return calendar.getTime();
	}

	// Өгөгдсөн огнооны сарыг аваах
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH) + 1;
	}

	// Өгөгдсөн огнооны өдрийг аваах
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DATE);
	}

	// Өгөгдсөн огнооны оныг аваах
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static Date str2Date(String pDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date dt = null;
		try {
			dt = formatter.parse(pDate);
		} catch (ParseException ex) {
			System.out.println(String.format("data: %s, err: %s", pDate, ex.getMessage()));
		}
		return dt;
	}

	// ===== to*** ===================
	public static String toString(Object val) {

		if (null == val)
			return "";

		String ret = null;
		Class<?> type = val.getClass();
		if (type == Date.class) {
			Date d = (Date) val;
			ret = Func.toDateTimeStr(d);
		} else if (type == String.class) {
			ret = (String) val;
		} else {
			ret = String.valueOf(val);
		}

		return ret;
	}

	public static String toDateTimeStr(Date date) {
		return toDateTimeStr(date, DATE_TIME_FORMAT);
	}

	public static String toDateTimeStr(Date date, String format) {
		if (date == null)
			return "01-01-01 00:00:00";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static long toLong(Object val) {
		try {
			if (val == null)
				return 0L;

			Class<?> type = val.getClass();

			if (type == String.class)
				return Double.valueOf((String) val).longValue();
			else if (type == Integer.class)
				return (long) (Integer) val;
			else if (type == Byte.class)
				return (long) (Byte) val;
			else if (type == Long.class)
				return (Long) val;
			else if (type == BigDecimal.class)
				return ((BigDecimal) val).longValue();
			else if (type == Double.class)
				return ((Double) val).longValue();
			else if (type == Float.class)
				return ((Float) val).longValue();
			else if (type == Boolean.class)
				return (Boolean) val ? 1L : 0L;
			else {
				// Do nothing
			}

			return Double.valueOf(String.valueOf(val)).longValue();

		} catch (Exception e) {
			return 0l;
		}
	}

	public static int toInt(Object val) {
		try {
			if (val == null)
				return 0;

			Class<?> type = val.getClass();

			if (type == String.class)
				return Double.valueOf((String) val).intValue();
			else if (type == Integer.class)
				return (Integer) val;
			else if (type == Byte.class)
				return (int) (Byte) val;
			else if (type == Long.class)
				return ((Long) val).intValue();
			else if (type == BigDecimal.class)
				return ((BigDecimal) val).intValue();
			else if (type == Double.class)
				return ((Double) val).intValue();
			else if (type == Float.class)
				return ((Float) val).intValue();
			else if (type == Boolean.class)
				return (Boolean) val ? 1 : 0;
			else {
				// Do nothing
			}

			return Double.valueOf(String.valueOf(val)).intValue();

		} catch (Exception e) {
			return 0;
		}
	}

	public static byte toByte(Object val) {
		try {
			if (val == null)
				return 0;

			Class<?> type = val.getClass();

			if (type == String.class)
				return Double.valueOf((String) val).byteValue();
			else if (type == Integer.class)
				return ((Integer) val).byteValue();
			else if (type == Byte.class)
				return (Byte) val;
			else if (type == Long.class)
				return ((Long) val).byteValue();
			else if (type == BigDecimal.class)
				return ((BigDecimal) val).byteValue();
			else if (type == Double.class)
				return ((Double) val).byteValue();
			else if (type == Float.class)
				return ((Float) val).byteValue();
			else if (type == Boolean.class)
				return (Boolean) val ? (byte) 1 : (byte) 0;
			else {
				// Do nothing
			}

			return Double.valueOf(String.valueOf(val)).byteValue();

		} catch (Exception e) {
			return 0;
		}
	}

	public static double toDouble(Object val) {
		try {
			if (val == null)
				return 0.0;

			Class<?> type = val.getClass();

			if (type == String.class)
				return Double.valueOf((String) val);
			else if (type == Long.class)
				return (double) (Long) val;
			else if (type == Integer.class)
				return (double) (Integer) val;
			else if (type == Byte.class)
				return (double) (Byte) val;
			else if (type == Float.class)
				return (double) (Float) val;
			else if (type == Double.class)
				return (Double) val;
			else if (type == BigDecimal.class)
				return ((BigDecimal) val).doubleValue();
			else if (type == Boolean.class)
				return (Boolean) val ? 1.0 : 0.0;
			else {
				// Do nothing
			}

			return Double.valueOf(String.valueOf(val));

		} catch (Exception e) {
			return 0;
		}
	}

	public static float toFloat(Object val) {
		try {
			if (val == null)
				return 0.0f;

			Class<?> type = val.getClass();

			if (type == String.class)
				return Float.valueOf((String) val);
			else if (type == Long.class)
				return (float) (Long) val;
			else if (type == Integer.class)
				return (float) (Integer) val;
			else if (type == Byte.class)
				return (float) (Byte) val;
			else if (type == Float.class)
				return (Float) val;
			else if (type == Double.class)
				return ((Double) val).floatValue();
			else if (type == BigDecimal.class)
				return ((BigDecimal) val).floatValue();
			else if (type == Boolean.class)
				return (Boolean) val ? 1.0f : 0.0f;
			else {
				// Do nothing
			}

			return Float.valueOf(String.valueOf(val));

		} catch (Exception e) {
			return 0;
		}
	}

	// Өгөгдсөн жил хэдэн өдөртэй вэ?
//	public static int getDayNumOfYear(Date date) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		int year = cal.get(Calendar.YEAR);
//		int dayNum = 365;
//		if (year % 4 == 0)
//			dayNum++;
//		if (year % 100 == 0)
//			dayNum--;
//		if (year % 400 == 0)
//			dayNum++;
//
//		return dayNum;
//	}

	// Өгөгдсөн жил хэдэн өдөртэй вэ?
	public static int getDayNumOfYear(int year) {
		int dayNum = 365;
		if (year % 4 == 0)
			dayNum++;
		if (year % 100 == 0)
			dayNum--;
		if (year % 400 == 0)
			dayNum++;

		return dayNum;
	}

	// Дээшээ эсвэл доошоо round-лах
	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
		return d.setScale(scale, mode);
	}

	// Өгөгдсөн mode-р round-лах
	public static BigDecimal round(BigDecimal d, int scale, int mode) {
		return d.setScale(scale, mode);
	}

	// ===== BigDec ================
	public static BigDecimal toBigDec(Object val) {
		try {
			if (val == null)
				return BigDecimal.ZERO;

			Class<?> type = val.getClass();

			if (type == String.class)
				return new BigDecimal((String) val);
			else if (type == Long.class)
				return new BigDecimal((Long) val);
			else if (type == Integer.class)
				return new BigDecimal((Integer) val);
			else if (type == Byte.class)
				return new BigDecimal((Byte) val);
			else if (type == BigDecimal.class)
				return (BigDecimal) val;
			else if (type == Boolean.class)
				return (Boolean) val ? BigDecimal.ONE : BigDecimal.ZERO;
			else {
				// Do nothing
			}

			return new BigDecimal((String.valueOf(val)));

		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	public static BigDecimal addBigDec(BigDecimal val1, BigDecimal val2) {

		return val1.add(val2);

	}

	public static BigDecimal subBigDec(BigDecimal val1, BigDecimal val2) {

		return val1.subtract(val2);

	}

	public static BigDecimal multBigDec(BigDecimal val1, BigDecimal val2) {

		return val1.multiply(val2);

	}

	public static BigDecimal divBigDec(BigDecimal val, BigDecimal divisor) {

		return val.divide(divisor, 50, RoundingMode.FLOOR);

	}

	// obj1 == obj2
	public static boolean equal(Object obj1, Object obj2) {

		if (obj1 == null && obj2 == null)
			return true;

		if (obj1 == null || obj2 == null)
			return false;

		String clName1 = obj1.getClass().getName();
		String clName2 = obj2.getClass().getName();

		if (clName1.contains(CLASS_NAME_BOOL)) {
			if (!clName2.contains(CLASS_NAME_BOOL)) {
				return false;
			}

			return obj1 == obj2;

		} else if (clName1.contains(CLASS_NAME_INT) || clName1.contains(CLASS_NAME_INTEGER)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_BT) || clName2.contains(CLASS_NAME_BYTE))) {
				return false;
			}

			return toInt(obj1) == toInt(obj2);

		} else if (clName1.contains(CLASS_NAME_L) || clName1.contains(CLASS_NAME_LONG)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_BT) || clName2.contains(CLASS_NAME_BYTE))) {
				return false;
			}

			return toLong(obj1) == toLong(obj2);

		} else if (clName1.contains(CLASS_NAME_DBL) || clName1.contains(CLASS_NAME_DOUBLE)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_DBL) || clName2.contains(CLASS_NAME_DOUBLE)
					|| clName2.contains(CLASS_NAME_F) || clName2.contains(CLASS_NAME_FLOAT)
					|| clName2.contains(CLASS_NAME_BIGDECIMAL)) || clName2.contains(CLASS_NAME_BT)
					|| clName2.contains(CLASS_NAME_BYTE)) {
				return false;
			}

			return toDouble(obj1) == toDouble(obj2);

		} else if (clName1.contains(CLASS_NAME_F) || clName1.contains(CLASS_NAME_FLOAT)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_DBL) || clName2.contains(CLASS_NAME_DOUBLE)
					|| clName2.contains(CLASS_NAME_F) || clName2.contains(CLASS_NAME_FLOAT)
					|| clName2.contains(CLASS_NAME_BIGDECIMAL)) || clName2.contains(CLASS_NAME_BT)
					|| clName2.contains(CLASS_NAME_BYTE)) {
				return false;
			}

			return toFloat(obj1) == toFloat(obj2);

		} else if (clName1.contains(CLASS_NAME_BIGDECIMAL)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_DBL) || clName2.contains(CLASS_NAME_DOUBLE)
					|| clName2.contains(CLASS_NAME_F) || clName2.contains(CLASS_NAME_FLOAT)
					|| clName2.contains(CLASS_NAME_BIGDECIMAL)) || clName2.contains(CLASS_NAME_BT)
					|| clName2.contains(CLASS_NAME_BYTE)) {
				return false;
			}

			return toBigDec(obj1).compareTo(toBigDec(obj2)) == 0;

		} else if (clName1.contains(CLASS_NAME_BT) || clName1.contains(CLASS_NAME_BYTE)) {
			if (!(clName2.contains(CLASS_NAME_INT) || clName2.contains(CLASS_NAME_INTEGER)
					|| clName2.contains(CLASS_NAME_L) || clName2.contains(CLASS_NAME_LONG)
					|| clName2.contains(CLASS_NAME_BT) || clName2.contains(CLASS_NAME_BYTE))) {
				return false;
			}

			return toByte(obj1) == toByte(obj2);

		} else if (clName1.contains(CLASS_NAME_STRING) || clName1.contains(CLASS_NAME_CHAR)
				|| clName1.contains(CLASS_NAME_CH)) {

			return toStr(obj1).equals(toStr(obj2));

		} else if (clName1.contains(CLASS_NAME_DATE) || clName1.contains(CLASS_NAME_TIME_STAMP)) {
			if (!(clName2.contains(CLASS_NAME_DATE) || clName2.contains(CLASS_NAME_TIME_STAMP))) {
				return false;
			}
			return ((Date) obj1).compareTo(((Date) obj2)) == 0;
		} else {
			// Do nothing
		}

		return false;
	}

	public static String toStr(Object val) {
		return toString(val);
	}

	public static boolean different(Object obj1, Object obj2) {
		return !equal(obj1, obj2);
	}

	public static String encodeAsBase64(byte[] src) {
		return Base64.getEncoder().encodeToString(src);
	}
}