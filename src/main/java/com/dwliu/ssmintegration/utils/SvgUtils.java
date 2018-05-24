package com.dwliu.ssmintegration.utils;

import com.dwliu.ssmintegration.constants.ExceptionConstants;
import com.dwliu.ssmintegration.exception.MyException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author dwliu
 */
public class SvgUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SvgUtils.class);

    /**
     * 将svg字符串转换为png
     *
     * @param svgPath     svg文件路径
     * @param pngFilePath 保存的路径
     * @throws MyException svg代码异常
     * @throws IOException         io错误
     */
    public static void convertToPng(String svgPath, String pngFilePath) throws MyException {

        File file = new File(pngFilePath);
        //父目录不存在创建
        FilesUtil.fileProber(file);

        FileOutputStream outputStream = null;
        try {
            String svgCode = FilesUtil.fileToString(svgPath);
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            convertToPng(svgCode, outputStream);
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION + "{}", e.getMessage());
            throw new MyException(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.error(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION + "关闭流{}", e.getMessage());
                    throw new MyException(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION);
                }
            }
        }
    }

    /**
     * 将svgCode转换成png文件，直接输出到流中
     *
     * @param svgCode      svg代码
     * @param outputStream 输出流
     * @throws MyException 异常
     * @throws IOException         io异常
     */
    public static void convertToPng(String svgCode, OutputStream outputStream) throws MyException {
        try {
            byte[] bytes = svgCode.getBytes("utf-8");
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(
                    new ByteArrayInputStream(bytes));
            TranscoderOutput output = new TranscoderOutput(outputStream);
            t.transcode(input, output);
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION + "convertToPng{}", e.getMessage());
            throw new MyException(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.error(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION + "关闭流{}", e.getMessage());
                    throw new MyException(ExceptionConstants.CONVERT_TO_PNG_EXCEPTION);
                }
            }
        }
    }


}
