package com.tdt.sys.core.util;

import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintUtils {
    public static String transformerToHtml(String templatePath, Map<String, Object> params,Map<String, Object> funcs, List<String> sheetNames) {
    	Workbook workbook =null;
    	InputStream is =null;
    	ByteArrayOutputStream out =null;
    	ByteArrayInputStream swapStream =null;
//    	InputStream tmpstream =null;
//    	File tmpfile=null;
    	ExcelToHtmlParams p =null;
    	String html="";
    	String script ="";
    	try {
	        is = PrintUtil.class.getResourceAsStream(templatePath);
	        out = new ByteArrayOutputStream();
	        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
	        Transformer transformer = jxlsHelper.createTransformer(is, out);
	        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
	        if(funcs==null) {
				funcs = new HashMap<String, Object>();
			}
//	        funcs.put("jxUtils", new JxlsUtils());
	        JexlBuilder jb = new JexlBuilder();
	        evaluator.setJexlEngine(jb.namespaces(funcs).create());
	        Context context = new Context();
	        for (String key : params.keySet()) {
	            context.putVar(key, params.get(key));
	        }
	        context.putVar("sheetNames", sheetNames);
	        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
	        swapStream = new ByteArrayInputStream(out.toByteArray());

	        POIReadExcel poiread=new POIReadExcel();
	        html=poiread.readExcelToHtml(swapStream, true);
	        script = "<script src=\"/ajax/libs/layui/layui.all.js\"></script>\n" +
                            "<script type=\"text/javascript\">\n" +
                            " window.print();\n" +
                            " parent.layer.closeAll();\n" +
                        "</script>";
    	}catch(IOException e) {
    		e.printStackTrace();
    	}finally {
	        try {
//				workbook.close();
				is.close();
		        out.flush();
		        out.close();
//		        tmpstream.close();
//		        tmpfile.deleteOnExit();
		        swapStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
//    	String html=ExcelXorHtmlUtil.excelToHtml(p);
        return html + script;
    }
}
