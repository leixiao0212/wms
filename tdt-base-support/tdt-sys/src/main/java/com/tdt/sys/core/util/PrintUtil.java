package com.tdt.sys.core.util;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PrintUtil {
    public static String transformerToHtml(String templatePath, Map<String, Object> params) throws IOException, InvalidFormatException {
        InputStream is = PrintUtil.class.getResourceAsStream(templatePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, out);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        funcs.put("dateFormat", dateFormat);
//        funcs.put("utils", new JxlsUtils());
        JexlBuilder jb = new JexlBuilder();
        evaluator.setJexlEngine(jb.namespaces(funcs).create());
        Context context = new Context();
        for (String key : params.keySet()) {
            context.putVar(key, params.get(key));
        }
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        ByteArrayInputStream swapStream = new ByteArrayInputStream(out.toByteArray());
        Workbook workbook = WorkbookFactory.create(swapStream);
        String s = ExcelToHtmlUtil.returnExcelToHtml(workbook, true);
//        ExcelToHtmlParams p = new ExcelToHtmlParams(workbook, false);
        String script = "<script src=\"/ajax/libs/layui/layui.all.js\"></script>\n" +
                            "<script type=\"text/javascript\">\n" +
                            " window.print();\n" +
                            " parent.layer.closeAll();\n" +
                        "</script>";
        workbook.close();
        is.close();
        out.flush();
        out.close();
        swapStream.close();
//        return ExcelXorHtmlUtil.excelToHtml(p) + script;
        return s + script;
    }
    public static Workbook getWorkbook(String templatePath, Map<String, Object> params) throws IOException, InvalidFormatException {
        InputStream is = PrintUtil.class.getResourceAsStream(templatePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, out);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        funcs.put("dateFormat", dateFormat);
        JexlBuilder jb = new JexlBuilder();
        evaluator.setJexlEngine(jb.namespaces(funcs).create());
        Context context = new Context();
        for (String key : params.keySet()) {
            context.putVar(key, params.get(key));
        }
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
        ByteArrayInputStream swapStream = new ByteArrayInputStream(out.toByteArray());
        Workbook workbook = WorkbookFactory.create(swapStream);
        return workbook;
    }
}
