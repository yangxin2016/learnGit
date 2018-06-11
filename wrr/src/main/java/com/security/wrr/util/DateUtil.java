package com.security.wrr.util;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * @Package:com.cnpc.dlp.base.util
 * @Description:日期工具类
 * @author:HZG
 * @Version：v1.0
 * @ChangeHistoryList：version     author         date              description 
 *                      v1.0        HZG    2014-6-17 下午22:14:04     日期工具类
 */
public final class DateUtil {
	@SuppressWarnings("unused")
	private static final String pattern = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 * @Description：字符串转Date类型
	 * @param dateTime 字符串型日期 yyyyMMddHHmmss
	 * @return date 类型
	 * @author:HZG
	 * @Date :2014-6-19 上午10:15:52
	 */
	public static Date convertStringDateAndTime(String dateTime) {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d = null;
		try {
			d = (Date) df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	
	DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
	
	public static Date getFullDateFormat(String dateTime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = (Date) df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static Date getHmsFormat(String hmsStr){
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d = null;
		try {
			d = (Date) df.parse(hmsStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 
	 * @Description：根据指定格式获取当前日期
	 * @param pattern 格式 如 yyyyMMddHHmmss yyyy-MM-dd HH:mm:ss
	 * @return 返回样式
	 * @author:HZG
	 * @Date :2014-6-19 上午10:19:09
	 */
	public static final String getCurrentTime(String pattern) {

		SimpleDateFormat fam = new SimpleDateFormat(pattern);
		Calendar calendar = Calendar.getInstance();
		String currentTime = fam.format(calendar.getTime());
		return currentTime;
	}



	/**
	 * 
	 * @Description： 取当月的第一天
	 * @return 返回日期格式 yyyy-MM-01 
	 * @author:HZG
	 * @Date :2014-6-18 上午10:30:57
	 */
	public static String getFirstDayOfMonth() {

		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-01");

		Date firstDay = new Date();

		return format.format(firstDay);

	}

	/**
	 * @Description：当前月最后一天
	 * @return yyyy-MM-maxDay
	 * @author:HZG
	 * @Date :2014-6-18 上午10:32:27
	 */
	public static String getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		java.text.Format formatter = new SimpleDateFormat("yyyy-MM-"
				+ maxDay);
		return formatter.format(cal.getTime());

	}


	/**
	 * @Description：获取当天日期
	 * @return String yyyy-MM-dd
	 * @author:HZG
	 * @Date :2014-6-18 上午10:33:23
	 */
	public static String today() {
		return getCurrentTime("yyyy-MM-dd");
	}

	/**
	 * 
	 * @Description：获取前一天日期
	 * @return String yyyy-MM-dd
	 * @author:HZG
	 * @Date :2014-6-18 上午10:34:37
	 */
	public static String yestoday() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int d = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, d - 1);
		return sdf.format(c.getTime());
	}
	
	/**
	 * @Description：获取指定天数的前几天日期
	 * @param beforeDay
	 * @throws ParseException
	 * @return:String
	 * @author:yangxin
	 * @Date :2016年3月14日 下午4:35:17
	 */
	public static Date getStatetime(Integer beforeDay){		  
		Calendar c = Calendar.getInstance();  
		c.add(Calendar.DATE, - beforeDay);  
		Date monday = c.getTime();
		return monday;
	}	   
	/**
	 * 
	 * @Description：获取明天日期
	 * @param date 
	 * @return Date
	 * @author:HZG
	 * @Date :2014-6-18 上午10:39:05
	 */
	public static Date nextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR,
				(calendar.get(Calendar.DAY_OF_YEAR) + 1));
		return calendar.getTime();
	}

	/**
	 * @Description：月加减
	 * @param date 
	 * @param pattern 样式
	 * @param amount 可以-1,-2,1,2
	 * @return 返回与样式想匹配的字符串
	 * @author:HZG
	 * @Date :2014-6-17 上午10:40:59
	 */
	public static String addMonth(Date date,String pattern,int amount){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);	
		Calendar c = Calendar.getInstance();
		c.setTime(date);
//		calendar.set(Calendar.MONTH,
//				(calendar.get(Calendar.DAY_OF_YEAR) + days));
		c.add(Calendar.MONTH, amount);
		return sdf.format(c.getTime());
		
	}

	/**
	 * @Description：日期加减
	 * @param date
	 * @param days 可以-1,-2,1,2
	 * @return
	 * @author:HZG
	 * @Date :2014-6-18 上午10:42:31
	 */
	public static Date addDay(Date date,int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_YEAR,
				(calendar.get(Calendar.DAY_OF_YEAR) + days));
		return calendar.getTime();
	}
/**
 * @Description： 获得当前时间。由于freemarker的日期必须有具体类型，所以使用timestamp。
 * @return
 * java.sql.Timestamp
 * @author:HZG
 * @Date :2014-7-17 上午11:16:37
 */

	public static java.sql.Timestamp now() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}

	/**
	 * @Description： 日期格式化
	 * @param date
	 * @param pattern
	 * @return
	 * @author:HZG
	 * @Date :2014-6-18 上午10:43:50
	 */
	public static String date2String(Date date, String pattern) {
		if (date == null)
			return "";
		DateFormat df = new SimpleDateFormat(pattern);
		String d = df.format(date);
		return d;
	}

	/**
	 * 
	 * @Description：两个日期之间相隔天数的共通
	 * @param dtFrom 开始时间
	 * @param dtEnd 结束时间
	 * @return 间隔天数
	 * @author:HZG
	 * @Date :2014-6-18 上午10:44:45
	 */
	public static Integer getDaysBetweenTwoDates(Date dtFrom, Date dtEnd) {
		long begin = dtFrom.getTime();
		long end = dtEnd.getTime();
		long inter = end - begin;
		int flag = 1;
		if (inter < 0) {
			inter = inter * (-1);
			flag = flag * (-1);
		}
		long dateMillSec = 24 * 60 * 60 * 1000;

		long dateCnt = inter / dateMillSec;

		long remainder = inter % dateMillSec;

		if (remainder != 0) {
			dateCnt++;
		}
		return flag * (int) dateCnt;
	}
	/**
	 * @Description：将String型日期转换成Date型
	 * @param date 需转换的串型日期(yyyy-MM-dd)
	 * @param pattern
	 * @return
	 * @author:HZG
	 * @Date :2014-6-18 上午10:45:59
	 */
	
	public static Date stringe2Date(String date,String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		Date d = null;
		try {
			d = (Date) df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	/**
	 * @Description：取得日期是星期几
	 * @param date
	 * @return 0 表示星期日 1 星期一    
	 * @author:HZG
	 * @Date :2014-6-18 上午10:46:49
	 */
    public static int getWeekOfDate(Date date) {
   //     String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
      //  return weekDays[w];
    }
    /**
     * @Description：
     * @return
     * Date
     * @author:yangxin
     * @Date :2014-8-25 下午1:50:29
     */
    public static Date getInternetTime(){
    	 //取得资源对象
		  //String InternetAddress="http://www.time.ac.cn";
		  //String InternetAddress="http://www.ntsc.cas.cn";
		  	String InternetAddress="http://www.bjtime.cn";
			URL url;
			URLConnection uc =null;
			try {
				//生成连接对象
				 url = new URL(InternetAddress);
				 uc= url.openConnection();
				 //发出连接
				 uc.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		  long time = uc.getDate();
		  Date date = new Date(time);
		//Date systemTime=new Date();
		//System.out.println(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(systemTime));
		//System.out.println(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(date));
		  return date;
    }
    
    
    /**
     * @Description：字符串日期 YYYYMMDDHH 格式化为 字符串日期 YYYY-MM-DD h时
     *              注意：特殊处理 小时+1
     * @return：String 
     * @author:Luozc
     * @Date :2014-8-25 下午1:50:29
     */
    public static String getFormatForStr(String strDate){
    	String year = strDate.substring(0,4);
    	String month = strDate.substring(4,6);
    	String day = strDate.substring(6,8);
    	String hour = strDate.substring(8,10);
    	
    	int h = Integer.parseInt(hour) + 1;
    	hour = String.valueOf(h) + "时";

    	String ymdh = year+"-"+month+"-"+day+" "+hour;
    	
    	return ymdh;
    }
    
    /**
     * @Description：获取日期“天”
     * @param time
     * @return
     * boolean
     * @author:yangxin
     * @Date :2014-9-25 上午9:39:09
     */
    public static int dayOfDate(Date time){
		Date current =new Date();
		Calendar cal =Calendar.getInstance();
		Calendar currentCal =Calendar.getInstance();
		cal.setTime(time);
		int day=cal.get(Calendar.DATE);		
		return day;
	}
    
    /**
     * @Description:获取当前时间的前一个小时的时间段
     * 				例如:当前系统时间为"2014-10-15 16:23:45",获取当前时间的前一小时 时间为: "2014-10-15 15"
     * 				主要用途:统计模块，没隔一小时都要统计前一小时的数据量。
     * 
     * 				把时间设置为当前时间-1小时，同理，也可以设置其他时间:cal.add(Calendar.HOUR,-1);
     * 				把时间设置为当前时间-1月，同理，也可以设置其他时间:cal.add(Calendar.MONTH,-1);
     * @param:
     * @return:String
     * @author:Luozc
     * @Date :2014-9-25 上午9:39:09
     */    
    public static String getOneHoursAgoTime() {
        String oneHoursAgoTime =  "" ;
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR,-1); 
        oneHoursAgoTime = new SimpleDateFormat("yyyy-MM-dd HH").format(cal.getTime());//获取到完整的时间
        
        return  oneHoursAgoTime;
    }
    
    /** 
     * 取得指定日期所在周的第一天 
     * 
     * @param date 
     * @return 
     */  
     public static Date getFirstDayOfWeek(Date date) {  
	     Calendar c = Calendar.getInstance(); 
	     c.setFirstDayOfWeek(Calendar.MONDAY);  
	     c.setTime(date);  
	     c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday  
	     return c.getTime ();  
     }  
      
     /** 
     * 取得指定日期所在周的最后一天 
     * 
     * @param date 
     * @return 
     */  
     public static Date getLastDayOfWeek(Date date) {  
	     Calendar c = Calendar.getInstance(); 
	     c.setFirstDayOfWeek(Calendar.MONDAY);  
	     c.setTime(date);  
	     c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday  
	     return c.getTime ();  
     }  
     
     /**
      * @Description：获取指定日期在本年中是第几周。
      * @param date
      * @return:int
      * @author:yangxin
      * @Date :2016年3月21日 下午12:02:43
      */
     public static int getWeekOfYear(Date date) {  
	     Calendar c = Calendar.getInstance(); 
	     c.setTime(date);  
	     c.setFirstDayOfWeek(Calendar.MONDAY);
	     c.setMinimalDaysInFirstWeek(7);
	     int weekOfYear = c.get(Calendar.WEEK_OF_YEAR);
	     //return c.getTime ();  
	     return weekOfYear;
     }  
     
	public static void main (String[] args){
		//DateUtil.getInternetTime();	
		//DateUtil.getFormatForStr("2014090101");
		System.out.println(DateUtil.getWeekOfYear(new Date()));
	}
	
}
