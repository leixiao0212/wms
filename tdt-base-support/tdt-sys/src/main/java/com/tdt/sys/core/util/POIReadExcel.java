package com.tdt.sys.core.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class POIReadExcel {

	public static void main(String[] args) {
		String inputpath = "D:/1.xlsx";
		String outPath = "D:/01xlsx.html";
		String content = convertXlsOrXlsxToHtml(inputpath);
		writeFile(content, outPath);
	}

	private static void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	public static String convertXlsOrXlsxToHtml(String path) {
		InputStream is = null;
		String htmlExcel = null;
		try {
			File sourcefile = new File(path);
			is = new FileInputStream(sourcefile);
			Workbook wb = WorkbookFactory.create(is);// 此WorkbookFactory在POI-3.10版本中使用需要添加dom4j
			if (wb instanceof XSSFWorkbook) {
				XSSFWorkbook xWb = (XSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(xWb, true);
			} else if (wb instanceof HSSFWorkbook) {
				HSSFWorkbook hWb = (HSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(hWb, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htmlExcel;
	}

	/**
	 * 程序入口方法
	 * 
	 * @param filePath
	 *            文件的路径
	 * @param isWithStyle
	 *            是否需要表格样式 包含 字体 颜色 边框 对齐方式
	 * @return
	 * 		<table>
	 *         ...
	 *         </table>
	 *         字符串
	 */
	public String readExcelToHtml(String filePath, boolean isWithStyle) {
		InputStream is = null;
		String htmlExcel = null;
		try {
			File sourcefile = new File(filePath);
			is = new FileInputStream(sourcefile);
			Workbook wb = WorkbookFactory.create(is);
			if (wb instanceof XSSFWorkbook) {
				XSSFWorkbook xWb = (XSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(xWb, isWithStyle);
			} else if (wb instanceof HSSFWorkbook) {
				HSSFWorkbook hWb = (HSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(hWb, isWithStyle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htmlExcel;
	}

	public String readExcelToHtml(InputStream is, boolean isWithStyle) {
		String htmlExcel = null;
		try {
			Workbook wb = WorkbookFactory.create(is);
			if (wb instanceof XSSFWorkbook) {
				XSSFWorkbook xWb = (XSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(xWb, isWithStyle);
			} else if (wb instanceof HSSFWorkbook) {
				HSSFWorkbook hWb = (HSSFWorkbook) wb;
				htmlExcel = POIReadExcel.getExcelInfo(hWb, isWithStyle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htmlExcel;
	}
	
	public static String getExcelInfo(Workbook wb, boolean isWithStyle) {
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE html>");
		sb.append("<html>");
		sb.append("<head>");
		sb.append("  <meta charset='utf-8'>");
		sb.append("  <meta name='viewport' content='width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0'>");
		sb.append("  <title>layui在线调试</title>");
		sb.append("  <link rel='stylesheet' href='http://res.layui.com/static/lay/v1/build/css/layui.css' media='all'>");
		sb.append("  <style>");
		sb.append("    body{margin: 10px;}");
		sb.append("  </style>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<div class='layui-tab layui-tab-brief' lay-filter='demo'>");  //这个位置替换class类，可以显示不同的风格
		sb.append("  <ul class='layui-tab-title'>");
		//生成目录
		for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
			Sheet sheet = wb.getSheetAt(numSheet);// 获取Sheet的内容
			if (sheet == null) {
				continue;
			}
			if(numSheet == 0){
				sb.append("    <li class='layui-this'>" + sheet.getSheetName() + "</li>");  
			}else{
				sb.append("    <li>" + sheet.getSheetName() + "</li>");  
			}
		}
		sb.append("  </ul>");
		sb.append("  <div class='layui-tab-content'>");
		//生成内容
		for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
			Sheet sheet = wb.getSheetAt(numSheet);// 获取Sheet的内容
			if (sheet == null) {
				continue;
			}
			sb.append("    <div class='layui-tab-item");
			if(numSheet == 0){
				sb.append(" layui-show'>");
			}else{
				sb.append("'>");
			}
			int lastRowNum = sheet.getLastRowNum();
			Map<String, String> map[] = getRowSpanColSpanMap(sheet);
			sb.append("<table style='border-collapse:collapse;'>");
			Row row = null; // 兼容
			Cell cell = null; // 兼容
			for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
				
				row = sheet.getRow(rowNum);
				if (row == null) {
					//sb.append("<tr><td >  </td></tr>");
					continue;
				}
				sb.append("<tr>");
				int lastColNum = row.getLastCellNum();
				for (int colNum = 0; colNum < lastColNum; colNum++) {
					cell = row.getCell(colNum);
					if (cell == null) { // 特殊情况 空白的单元格会返回null
						//sb.append("<td> </td>");
						continue;
					}
					String stringValue = getCellValue(cell);
					if (map[0].containsKey(rowNum + "," + colNum)) {
						String pointString = map[0].get(rowNum + "," + colNum);
						map[0].remove(rowNum + "," + colNum);
						int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
						int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
						int rowSpan = bottomeRow - rowNum + 1;
						int colSpan = bottomeCol - colNum + 1;
						sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
					} else if (map[1].containsKey(rowNum + "," + colNum)) {
						map[1].remove(rowNum + "," + colNum);
						continue;
					} else {
						sb.append("<td ");
					}
					// 判断是否需要样式
					if (isWithStyle) {
						dealExcelStyle(wb, sheet, cell, sb,stringValue);// 处理单元格样式
					}
					sb.append(">");
					if (stringValue == null || "".equals(stringValue.trim())) {
						sb.append("   ");
					} else {
						// 将ascii码为160的空格转换为html下的空格（ ）
//						if (stringValue.contains("x")) {
//							sb.append("<input type='text' id='" + stringValue + "' name='" + stringValue
//									+ "' value='' />");
//						} else {
							sb.append(stringValue.replace(String.valueOf((char) 160), " "));
//						}
					}
					sb.append("</td>");
				}
				sb.append("</tr>");
				
			}
			sb.append("</table>");
			sb.append("  </div>");
		}
		sb.append("</div>");
		sb.append("<div id='pageDemo'></div>");
		sb.append("<script src='http://res.layui.com/static/lay/v1/build/layui.js'></script>");
		sb.append("<script>");
		sb.append("layui.use(['layer', 'laypage', 'element'], function(){");
		sb.append("  var layer = layui.layer");
		sb.append("  ,laypage = layui.laypage");
		sb.append("  ,element = layui.element();");
		sb.append("});");
		sb.append("</script>");
		sb.append("</body>");
		sb.append("</html>");   
		return sb.toString();
	}

	
	private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
		Map<String, String> map0 = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
		int mergedNum = sheet.getNumMergedRegions();
		CellRangeAddress range = null;
		for (int i = 0; i < mergedNum; i++) {
			range = sheet.getMergedRegion(i);
			int topRow = range.getFirstRow();
			int topCol = range.getFirstColumn();
			int bottomRow = range.getLastRow();
			int bottomCol = range.getLastColumn();
			map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
			// System.out.println(topRow + "," + topCol + "," + bottomRow + ","
			// + bottomCol);
			int tempRow = topRow;
			while (tempRow <= bottomRow) {
				int tempCol = topCol;
				while (tempCol <= bottomCol) {
					map1.put(tempRow + "," + tempCol, "");
					tempCol++;
				}
				tempRow++;
			}
			map1.remove(topRow + "," + topCol);
		}
		Map[] map = { map0, map1 };
		return map;
	}

	/**
	 * 获取表格单元格Cell内容
	 * 
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {
		String result = new String();
		switch (cell.getCellTypeEnum()) {
		case NUMERIC:// 数字类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else {// 日期
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				result = sdf.format(date);
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
				result = sdf.format(date);
			} else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat format = new DecimalFormat();
				String temp = style.getDataFormatString();
				// 单元格设置成常规
				if (temp.equals("General")) {
					format.applyPattern("#");
				}
				result = format.format(value);
			}
			break;
		case STRING:// String类型
			result = cell.getRichStringCellValue().toString();
			break;
		case BLANK:
			result = "";
			break;
		default:
			result = "";
			break;
		}
		return result;
	}

	/**
	 * 处理表格样式
	 * 
	 * @param wb
	 * @param sheet
	 * @param cell
	 * @param sb
	 */
	private static void dealExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuffer sb, String stringValue) {
		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle != null) {
			HorizontalAlignment alignment = cellStyle.getAlignmentEnum();
			
			sb.append("align='" + convertAlignToHtml(alignment) + "' ");// 单元格内容的水平对齐方式
			VerticalAlignment verticalAlignment = cellStyle.getVerticalAlignmentEnum();
			sb.append("valign='" + convertVerticalAlignToHtml(verticalAlignment) + "' ");// 单元格中内容的垂直排列方式
			if (wb instanceof XSSFWorkbook) {
				XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
				boolean boldWeight = xf.getBold();
				sb.append("style='");
				if(boldWeight) {
					sb.append("font-weight:bold;"); // 字体加粗
				}
				sb.append("font-size: " + xf.getFontHeight() / 2 + "%;"); // 字体大小
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				
				sb.append("width:" + columnWidth + "%");
				
				XSSFColor xc = xf.getXSSFColor();
				if (xc != null && !"".equals(xc)) {
					sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
				}
				XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
				if (bgColor != null && !"".equals(bgColor)) {
					sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(0, cellStyle.getBorderTopEnum(),
						((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
				sb.append(getBorderStyle(1, cellStyle.getBorderRightEnum(),
						((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
				sb.append(getBorderStyle(2, cellStyle.getBorderBottomEnum(),
						((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
				sb.append(getBorderStyle(3, cellStyle.getBorderLeftEnum(),
						((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));
			} else if (wb instanceof HSSFWorkbook) {
				HSSFFont hf = ((HSSFCellStyle) cellStyle).getFont(wb);
				boolean boldWeight = hf.getBold();
				short fontColor = hf.getColor();
				sb.append("style='");
				HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette(); // 类HSSFPalette用于求的颜色的国际标准形式
				HSSFColor hc = palette.getColor(fontColor);
				if(boldWeight) {
					sb.append("font-weight:bold;"); // 字体加粗
				}
				sb.append("font-size: " + hf.getFontHeight() / 2 + "%;"); // 字体大小
				String fontColorStr = convertToStardColor(hc);
				if (fontColorStr != null && !"".equals(fontColorStr.trim())) {
					sb.append("color:" + fontColorStr + ";"); // 字体颜色
				}
				int columnWidth = sheet.getColumnWidth(cell.getColumnIndex());
				sb.append("width:" + columnWidth + "px;");
				//sb.append("width:" + stringValue.trim().length() + "%");
				short bgColor = cellStyle.getFillForegroundColor();
				hc = palette.getColor(bgColor);
				String bgColorStr = convertToStardColor(hc);
				if (bgColorStr != null && !"".equals(bgColorStr.trim())) {
					sb.append("background-color:" + bgColorStr + ";"); // 背景颜色
				}
				sb.append(getBorderStyle(palette, 0, cellStyle.getBorderTopEnum(), cellStyle.getTopBorderColor()));
				sb.append(getBorderStyle(palette, 1, cellStyle.getBorderRightEnum(), cellStyle.getRightBorderColor()));
				sb.append(getBorderStyle(palette, 3, cellStyle.getBorderLeftEnum(), cellStyle.getLeftBorderColor()));
				sb.append(getBorderStyle(palette, 2, cellStyle.getBorderBottomEnum(), cellStyle.getBottomBorderColor()));
			}
			sb.append("' ");
		}
	}

	/**
	 * 单元格内容的水平对齐方式
	 * 
	 * @param alignment
	 * @return
	 */
	private static String convertAlignToHtml(HorizontalAlignment alignment) {
		String align = "left";
		switch (alignment) {
		case LEFT:
			align = "left";
			break;
		case CENTER:
			align = "center";
			break;
		case RIGHT:
			align = "right";
			break;
		default:
			break;
		}
		return align;
	}

	/**
	 * 单元格中内容的垂直排列方式
	 * 
	 * @param verticalAlignment
	 * @return
	 */
	private static String convertVerticalAlignToHtml(VerticalAlignment verticalAlignment) {
		String valign = "middle";
		switch (verticalAlignment) {
		case BOTTOM:
			valign = "bottom";
			break;
		case CENTER:
			valign = "middle";
			break;
		case TOP:
			valign = "top";
			break;
		default:
			break;
		}
		return valign;
	}

	private static String convertToStardColor(HSSFColor hc) {
		StringBuffer sb = new StringBuffer("");
		if (hc != null) {
			if (HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex() == hc.getIndex()) {
				return null;
			}
			sb.append("#");
			for (int i = 0; i < hc.getTriplet().length; i++) {
				sb.append(fillWithZero(Integer.toHexString(hc.getTriplet()[i])));
			}
		}
		return sb.toString();
	}

	private static String fillWithZero(String str) {
		if (str != null && str.length() < 2) {
			return "0" + str;
		}
		return str;
	}

	static String[] bordesr = { "border-top:", "border-right:", "border-bottom:", "border-left:" };
	static String[] borderStyles = { "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ", "solid ",
			"solid ", "solid", "solid", "solid", "solid", "solid" };

	private static String getBorderStyle(HSSFPalette palette, int b, BorderStyle s, short t) {
		if (s.getCode() == 0)
			return bordesr[b] + borderStyles[s.getCode()] + "#d0d7e5 1px;";
		String borderColorStr = convertToStardColor(palette.getColor(t));
		borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr;
		return bordesr[b] + borderStyles[s.getCode()] + borderColorStr + " 1px;";
	}

	private static String getBorderStyle(int b, BorderStyle s, XSSFColor xc) {
		if (s.getCode() == 0)
			return bordesr[b] + borderStyles[s.getCode()] + "#d0d7e5 1px;";
		if (xc != null && !"".equals(xc)) {
			String borderColorStr = xc.getARGBHex();// t.getARGBHex();
			borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000"
					: borderColorStr.substring(2);
			return bordesr[b] + borderStyles[s.getCode()] + borderColorStr + " 1px;";
		}
		return "";
	}

}
