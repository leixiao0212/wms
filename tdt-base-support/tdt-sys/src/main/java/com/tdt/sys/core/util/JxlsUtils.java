package com.tdt.sys.core.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JxlsUtils {
	private Map<String,Integer> countMap = new HashMap<>();

//	static{ 
//		//添加自定义指令（可覆盖jxls原指令） 
//		//合并单元格(模板已经做过合并单元格操作的单元格无法再次合并)
//		XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class); 
//	}

	// 日期格式化
	public String dateFmt(Date date, String fmt) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
			return dateFmt.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	// if判断
	public Object ifelse(boolean b, Object o1, Object o2) {
		return b ? o1 : o2;
	}
	//if v1 contains v2,true return o1,false return o2
	public Object contains(String v1,String v2, Object o1, Object o2) {
		if(null!=v1) {
			if(Arrays.asList(v1.split(",")).contains(v2)) {
				return o1;
			}
		}
		return o2;
	}
	
	public boolean isContains(String v1,String v2,String isExport) {
		if(null==isExport || isExport.equals("N")) {
			return true;
		}
		if(null!=v1) {
			if(Arrays.asList(v1.split(",")).contains(v2)) {
				return true;
			}
		}
		return false;
	}

	public Integer count(String var){
		if (var == null) return null;
		if(countMap.containsKey(var)){
			Integer t = countMap.get(var);
			t += 1;
			countMap.replace(var,t);
			return t;
		}else{
			countMap.put(var,1);
		}
		return 1;
	}
}