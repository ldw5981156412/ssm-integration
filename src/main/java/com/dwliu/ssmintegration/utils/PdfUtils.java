package com.dwliu.ssmintegration.utils;

import com.dwliu.ssmintegration.constants.ExceptionConstants;
import com.dwliu.ssmintegration.exception.MyException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author dwliu
 */
public class PdfUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfUtils.class);

    /**
     * 将html字符串打印成pdf文件
     *
     * @param html     html字符串
     * @param pdfPath  pdf输出路径
     * @param cssPaths css路径数组
     */
    public void htmlToPdf(String html, String pdfPath, String[] cssPaths) throws MyException {
        Document doc = null;
        //创建一个合并流的对象
        SequenceInputStream sis = null;
        PdfWriter writer = null;
        try {
            doc = new Document(PageSize.A4);
            //构建流集合。
            Vector<InputStream> vector = new Vector<>();
            for (String cssPath : cssPaths) {
                vector.addElement(new FileInputStream(cssPath));
            }
            Enumeration<InputStream> e = vector.elements();
            sis = new SequenceInputStream(e);

            writer = PdfWriter.getInstance(doc, new FileOutputStream(pdfPath));
            doc.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, doc,
                    new ByteArrayInputStream(html.getBytes()),
                    sis,
                    Charset.forName("UTF-8"), new AsianFontProvider());

        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.CREATE_PDF_EXCEPTION + "{}", e.getMessage());
            throw new MyException(ExceptionConstants.CREATE_PDF_EXCEPTION);
        } finally {
            try {
                if (sis != null) {
                    sis.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (doc != null) {
                    doc.close();
                }
            } catch (IOException e) {
                LOGGER.error(ExceptionConstants.CREATE_PDF_EXCEPTION + "{}", e.getMessage());
                throw new MyException(ExceptionConstants.CREATE_PDF_EXCEPTION);
            }
        }
    }

    /**
     * pdf中文
     */
    class AsianFontProvider extends XMLWorkerFontProvider {

        @Override
        public Font getFont(final String fontname, final String encoding,
                            final boolean embedded, final float size, final int style,
                            final BaseColor color) {
            BaseFont bf;
            try {
                bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                        BaseFont.NOT_EMBEDDED);
                Font font = new Font(bf, size, style, color);
                font.setColor(color);
                return font;
            } catch (Exception e) {
                LOGGER.error(ExceptionConstants.FONT_EXCEPTION + "{}", e.getMessage());
                throw new MyException(ExceptionConstants.FONT_EXCEPTION);
            }
        }
    }
}
