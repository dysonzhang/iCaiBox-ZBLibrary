package com.ichihe.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Time Util
 * 
 * @author dyson
 * @since 2014-09-04
 * 
 */
public class MyTimeUtil {
	
	/**
	 * 计算两个日期之间相差的天数 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		long between_days = 0L;
		try {
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * get today date string
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getTodayDateString() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		String today = data.format(cal.getTime());
		return today;
	}

	/**
	 * get yesterday date string
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getYesterdayDateString() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.add(Calendar.DATE, -1);
		String today = data.format(cal.getTime());
		return today;
	}

	/**
	 * get the day before yesterday date string
	 * 
	 * @return "yyyy-MM-dd"
	 */
	public static String getDayBeforeYesterdayDateString() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.add(Calendar.DATE, -2);
		String today = data.format(cal.getTime());
		return today;
	}

	/**
	 * get now time string
	 * 
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getNowTimeString() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		String startTime = data.format(cal.getTime());

		return startTime;
	}

	/**
	 * get now time string
	 * 
	 * @return "yyyy/MM/dd HH:mm:ss"
	 */
	public static String getNowTimeShortString() {
		SimpleDateFormat data = new SimpleDateFormat("MM/dd HH:mm",
				Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		String startTime = data.format(cal.getTime());

		return startTime;
	}

	/**
	 * get now time timestamp class
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getTimestamp() {
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		String startTime = data.format(cal.getTime());

		Timestamp ts = null;
		try {
			ts = new Timestamp(data.parse(startTime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ts;
	}

	/**
	 * 
	 * 
	 * @Description: TODO get time area：hour of day
	 * 
	 * @param Timestamp
	 *            ts
	 * 
	 * @return int
	 * 
	 * @throws
	 */
	public static int getTimestampHour(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(ts);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 
	 * 
	 * @Description: TODO Timestamp to String
	 * 
	 * @param Timestamp
	 *            ts
	 * 
	 * @return String
	 * 
	 */
	public static String timestampToString(Timestamp ts) {
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		tsStr = sdf.format(ts);
		return tsStr;
	}

	/**
	 * 
	 * 
	 * @Description: TODO 获取当前有效时长的时间毫秒数
	 * 
	 * @param Timestamp
	 *            ts 时间
	 * @param Long
	 *            delayTime 有效时长
	 * @return Long 有效时长的时间毫秒数 默认为当前时间的毫秒数
	 */
	public static Long getExpriedMilliseconds(Timestamp ts, long delayTime) {

		String tsStr = "";

		SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss",
				new Locale("zh", "cn"));
		tsStr = m_format.format(ts);

		SimpleDateFormat timeformat = new SimpleDateFormat(
				"yyyy-MM-dd,HH:mm:ss");
		long t = ts.getTime();
		try {
			t = timeformat.parse(tsStr).getTime() + delayTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 
	 * 
	 * @Description: TODO 门店是否为营业时间
	 * 
	 * @param openTime
	 *            开店时间
	 * @param closeTime
	 *            关店时间
	 * 
	 * 
	 * @return 1：营业中 0：打烊
	 * 
	 * @throws ParseException
	 */
	public static int compareOpen(String openTime, String closeTime)
			throws Exception {

		if (openTime.contains("00:00:00") && closeTime.contains("00:00:00")) {
			return 1;
		}

		if (closeTime.contains("00:00:00")) {
			closeTime = "23:59:59";
		}

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		String nowTime = df.format(cal.getTime());

		Date now = df.parse(nowTime);
		Date open = df.parse(openTime);
		Date close = df.parse(closeTime);

		Date zore1 = df.parse("23:59:59");
		Date zore2 = df.parse("00:00:00");

		if (close.getTime() <= open.getTime()) {
			/**
			 * 跨凌晨
			 */
			// openTime 到 23:59:59
			if (now.getTime() >= open.getTime()
					&& now.getTime() <= zore1.getTime()) {
				return 1;
			}

			// 00:00:00 到 closeTime
			if (now.getTime() >= zore2.getTime()
					&& now.getTime() <= close.getTime()) {
				return 1;
			}

		} else {
			/**
			 * 未跨凌晨
			 */
			// openTime 到 closeTime
			if (now.getTime() >= open.getTime()
					&& now.getTime() <= close.getTime()) {
				return 1;
			}
		}

		return 0;
	}

	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	// 字符串类型日期转化成date类型
	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	public static String dateToStr(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}

	public static String clanderTodatetime(Calendar calendar, String style) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(calendar.getTime());
	}
}
