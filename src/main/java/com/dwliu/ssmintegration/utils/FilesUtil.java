package com.dwliu.ssmintegration.utils;

import com.dwliu.ssmintegration.constants.ExceptionConstants;
import com.dwliu.ssmintegration.exception.MyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtil.class);

    /**
     * 文件探针
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirFile
     */
    public static void fileProber(File dirFile) {
        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);
            parentFile.mkdir();
        }
    }

    /**
     * 功能： 字符串写入文件 说明：
     *
     * @param filePath 目标文件
     * @param data     字符串
     * @return
     * @author dwliu
     */
    public static void WriteStringToFile(String filePath, String data) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(filePath);
            fileProber(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            if (data != null) {
                bw.write(data);
            } else {
                bw.write("");
            }
            bw.flush();
        } catch (IOException e) {
            LOGGER.error("{}", e.getMessage());
            throw new MyException(ExceptionConstants.WRITE_FILE_EXCEPTION);
        } catch (Exception e) {
            throw new MyException(ExceptionConstants.WRITE_FILE_EXCEPTION);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                throw new MyException(ExceptionConstants.WRITE_FILE_EXCEPTION);
            }
        }
    }

    /**
     * 功能： 读取文件 说明：
     *
     * @param filePath
     * @return
     * @author dwliu
     */
    public static String readFileToString(String filePath) throws MyException {
        String str = "";
        File file = new File(filePath);
        BufferedReader rd = null;
        try {
            rd = new BufferedReader(new FileReader(file));
            String s = rd.readLine();
            while (null != s) {
                str += s + "\n";
                s = rd.readLine();
            }
        } catch (FileNotFoundException e) {
            return str;
        } catch (IOException e) {
            throw new MyException(ExceptionConstants.READ_FILE_EXCEPTION);
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    throw new MyException(ExceptionConstants.READ_FILE_EXCEPTION);
                }
            }
        }
        return str;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否  
     * *@param sPath  要删除的目录或文件  
     * *@return 删除成功返回
     * true，否则返回 false。  
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在 
        if (!file.exists()) {
            flag = true;
            // 不存在返回 false 
            return flag;
        } else {
            file.delete();
            flag = true;
        }
        return deleteFile(sPath);
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 获取文件夹下全部的文件
     *
     * @param path
     * @return
     */
    public static List<File> readAllFiles(String path) {
        List<File> files = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String fileName : filelist) {
                String filepath = path + File.separator + fileName;
                File readfile = new File(filepath);
                if (readfile.isDirectory()) {
                    files.addAll(readAllFiles(filepath));
                } else {
                    files.add(readfile);
                }
            }
        }
        return files;
    }

    /**
     * 获取目录下的目录
     *
     * @param path
     * @return
     */
    public static List<String> listDir(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String fileName : filelist) {
                String filepath = path + File.separator + fileName;
                File readfile = new File(filepath);
                if (readfile.isDirectory()) {
                    files.add(fileName);
                }
            }
        }
        return files;
    }

    /**
     * 获取文件夹下全部的文件
     *
     * @param path
     * @return
     */
    public static List<File> readDirectlyFiles(String path) {
        List<File> files = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (String fileName : filelist) {
                String filepath = path + File.separator + fileName;
                File readfile = new File(filepath);
                if (!readfile.isDirectory()) {
                    files.add(readfile);
                }
            }
        }
        return files;
    }

    public static String readFileToJsonString(String path) throws IOException {
        List<String> list = new ArrayList<String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        List<Map<String, String>> result;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            String head = list.get(0);
            String[] headers = head.split("\\t");
            result = new ArrayList<Map<String, String>>();
            for (int i = 1; i < list.size(); i++) {
                String[] data = list.get(i).split("\\t");
                Map<String, String> map = new HashMap<String, String>();
                for (int j = 0; j < data.length; j++) {
                    map.put(headers[j], data[j]);
                }
                result.add(map);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(result);
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static String[] readFileHead(String path) throws IOException {
        List<String> list = new ArrayList<String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            String head = list.get(0);
            return head.split("\\t");
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    public static List<String[]> readFileData(String path) throws IOException {
        List<String> list = new ArrayList<String>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        List<String[]> result;
        try {
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            result = new ArrayList<String[]>();
            int length = list.size() > 6 ? 6 : list.size();
            for (int i = 1; i < length; i++) {
                result.add(list.get(i).split("\\t"));
            }
            return result;
        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) throws MyException {
        new File((new File(newPath)).getParent()).mkdir();
        InputStream input = null;
        FileOutputStream output = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            //文件存在时
            if (oldfile.exists()) {
                //读入原文件
                input = new FileInputStream(oldPath);
                output = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = input.read(buffer)) != -1) {
                    bytesum += byteread;
                    output.write(buffer, 0, byteread);
                }
            }
        } catch (IOException e) {
            LOGGER.error(ExceptionConstants.COPY_FILE_EXCEPTION + ":{}", e);
            throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.COPY_FILE_EXCEPTION + ":{}", e);
            throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
                }
            }
        }

    }

    /**
     * 复制单个文件
     *
     * @param oldfile File 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(File oldfile, String newPath) throws MyException {
        new File((new File(newPath)).getParent()).mkdir();
        InputStream input = null;
        FileOutputStream output = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldfile.exists()) {
                input = new FileInputStream(oldfile);
                output = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = input.read(buffer)) != -1) {
                    bytesum += byteread;
                    output.write(buffer, 0, byteread);
                }
            }
        } catch (IOException e) {
            LOGGER.error(ExceptionConstants.COPY_FILE_EXCEPTION + ":{}", e);
            throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error(ExceptionConstants.COPY_FILE_EXCEPTION + ":{}", e);
            throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    throw new MyException(ExceptionConstants.COPY_FILE_EXCEPTION);
                }
            }
        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) throws MyException {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;

            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                //如果是子文件夹
                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (IOException e) {
            LOGGER.error("复制整个文件夹内容操作出错:{}", e);
            throw new MyException(ExceptionConstants.COPY_FOLDER_EXCEPTION);
        } catch (Exception e) {
            LOGGER.error("复制整个文件夹内容操作出错:{}", e);
            throw new MyException(ExceptionConstants.COPY_FOLDER_EXCEPTION);
        }
    }

    /**
     * 默认使用编码UTF-8  SVG文件输入流转String
     *
     * @param filePath
     * @return SVG代码
     * @throws IOException
     */
    public static String fileToString(String filePath) throws IOException {
        InputStream in = new FileInputStream(new File(filePath));
        return inputStreamToString(in, "UTF-8");
    }

    /**
     * 指定字符集SVG输入流转String
     *
     * @param in      输入流
     * @param charset 字符编码
     * @return SVG代码
     */
    public static String inputStreamToString(InputStream in, String charset) throws MyException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bfr = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, charset);
            bfr = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bfr.readLine()) != null) {
                buffer.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            throw new MyException(ExceptionConstants.INPUTSTREAM_TO_STRING_EXCEPTION);
        } catch (IOException e) {
            throw new MyException(ExceptionConstants.INPUTSTREAM_TO_STRING_EXCEPTION);
        } finally {
            try {
                if (bfr != null) {
                    bfr.close();
                }
            } catch (IOException e) {
                throw new MyException(ExceptionConstants.INPUTSTREAM_TO_STRING_EXCEPTION);
            }
        }
        return buffer.toString();
    }
}
