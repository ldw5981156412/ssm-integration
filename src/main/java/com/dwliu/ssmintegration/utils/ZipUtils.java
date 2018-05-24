package com.dwliu.ssmintegration.utils;

import com.dwliu.ssmintegration.constants.ExceptionConstants;
import com.dwliu.ssmintegration.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author dwliu
 */
public class ZipUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 解压文件到指定目录
     *
     * @param filePath  上传压缩文件
     * @param outputDir 解压路径，比如C:\\home\\myblog\\project\\
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(String filePath,String outputDir) throws IOException {
        File oldFile = new File(filePath);
        //解压目录
        String newDir = oldFile.getName().substring(0, oldFile.getName().length() - 4);
        String unZipRealPath = outputDir + "/" + newDir + System.currentTimeMillis() + "/";
        //如果保存解压缩文件的目录不存在，则进行创建，并且解压缩后的文件总是放在以fileName命名的文件夹下
        File unZipFile = new File(unZipRealPath);
        if (!unZipFile.exists()) {
            unZipFile.mkdirs();
        }
        //ZipInputStream用来读取压缩文件的输入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(oldFile), Charset.forName("GBK"));
        //压缩文档中每一个项为一个zipEntry对象，可以通过getNextEntry方法获得，zipEntry可以是文件，也可以是路径，比如abc/test/路径下
        ZipEntry zipEntry;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String outPath = (unZipRealPath + zipEntry.getName()).replaceAll("\\+", "/");
                //判断所要添加的文件所在路径或者
                // 所要添加的路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是,在上面三行已经创建,不需要解压
                File outFile = new File(outPath);
                if (outFile.isDirectory()) {
                    continue;
                }
                OutputStream outputStream = new FileOutputStream(outFile.getParent() + "/" + StringUtils.createUUID36() + "-" + outFile.getName());
                byte[] bytes = new byte[4096];
                int len;
                //当read的返回值为-1，表示碰到当前项的结尾，而不是碰到zip文件的末尾
                while ((len = zipInputStream.read(bytes)) > 0) {
                    outputStream.write(bytes, 0, len);
                }
                outputStream.close();
                //必须调用closeEntry()方法来读入下一项
                zipInputStream.closeEntry();
            }
        } catch (Exception e) {
            LOGGER.info("******************服务器异常********************");
            throw new MyException(ExceptionConstants.UNPACK_ERROR);
        }finally {
            LOGGER.info("******************解压完毕********************");
            zipInputStream.close();
        }
    }


}