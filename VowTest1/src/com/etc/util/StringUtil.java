package com.etc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
	
	//关键字段加红
	public static String convertKeyWord(String source, String keyword){
		source=source.replaceAll(keyword," <font color=red><b>" + keyword + "</b></font>");
		return source;
	}
	
	//转换日期时间的格式   format  Data-->String    parse  String --> Date
		public static String convertDatetime(String source, String pattern){
			
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			Date date = null;
			
			try {
				 date = sdfSource.parse(source);
			} catch (ParseException e) {
				e.printStackTrace();
			}			
			SimpleDateFormat sdfDest = new SimpleDateFormat(pattern);	
			
			return sdfDest.format(date);			
		}

}

