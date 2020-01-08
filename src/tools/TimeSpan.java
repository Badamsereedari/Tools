package tools;

import java.util.Date;

public class TimeSpan {

	private Date startDate;
	private Date endDate;

	private int year;
	private int month;
	private int day;
	private int allDays;
	private int allMonths;
	private int hour;
	private int minute;
	private int second;
	private long millsecond;

	public TimeSpan() {
	}

	public TimeSpan(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getAllDays() {
		return allDays;
	}

	public int getAllMonths() {
		return allMonths;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public int getSecond() {
		return second;
	}

	public long getMillsecond() {
		return millsecond;
	}

	public TimeSpan dateDiff() {

		millsecond = endDate.getTime() - startDate.getTime();

		allMonths = 0;
		Date tmpStartDate = startDate;
		while (!endDate.before(tmpStartDate)) {
			tmpStartDate = Func.addMonths(tmpStartDate, 1);
			allMonths++;
		}
		allMonths--;

		second = (int) (millsecond / (long) 1000 % (long) 60);
		minute = (int) (millsecond / ((long) 1000 * (long) 60) % (long) 60);
		hour = (int) (millsecond / ((long) 1000 * (long) 60 * (long) 60) % (long) 24);
		allDays = (int) (millsecond / ((long) 1000 * (long) 60 * (long) 60 * (long) 24));
		year = Func.getYear(endDate) - Func.getYear(startDate);
		month = Func.getMonth(endDate) - Func.getMonth(startDate);
		day = Func.getDayOfMonth(endDate) - Func.getDayOfMonth(startDate);
		if (day < 0) {
			month--;
			switch (Func.getMonth(endDate) - 1) {
			case 0:
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
				day += 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day += 30;
				break;
			case 2:
				day += 28;
				if (Func.getYear(endDate) % 4 == 0)
					day++;
				if (Func.getYear(endDate) % 100 == 0)
					day--;
				if (Func.getYear(endDate) % 400 == 0)
					day++;
				break;
			default:
				break;
			}
		}
		if (month < 0) {
			year--;
			month += 12;
		}

		return this;

	}

	public TimeSpan dateDiff(Date startDate, Date endDate) {

		TimeSpan timeSpan = new TimeSpan(startDate, endDate);

		return timeSpan.dateDiff();

	}

}