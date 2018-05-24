package com.dwliu.ssmintegration.utils;


import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

/**
 * TAR工具
 *
 * @author
 * @since 1.0
 */
public abstract class TarUtils {

    private static final String BASE_DIR = "";

    /**
     * 符号"/"用来作为目录标识判断符
     */
    private static final String PATH = "/";
    private static final int BUFFER = 1024;


    /**
     * 归档
     *
     * @param srcPath  源路径
     * @param destPath 目标路径
     * @throws Exception
     */
    public static void archive(String srcPath, String destPath) throws Exception {
        File srcFile = new File(srcPath);
        archive(srcFile, destPath);
    }

    /**
     * 归档
     *
     * @param srcFile  源路径
     * @param destFile 目标路径
     * @throws Exception
     */
    public static void archive(File srcFile, File destFile) throws Exception {
        TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(destFile));
        //设置文件名大于100字节（file name '....' is too long ( > 100 bytes)）
        taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        archive(srcFile, taos, BASE_DIR);
        taos.flush();
        taos.close();
    }

    /**
     * 归档文件
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void archive(File srcFile, String destPath) throws Exception {
        archive(srcFile, new File(destPath));
    }


    /**
     * 归档
     *
     * @param srcFile  源路径
     * @param taos     TarArchiveOutputStream
     * @param basePath 归档包内相对路径
     * @throws Exception
     */
    private static void archive(File srcFile, TarArchiveOutputStream taos, String basePath) throws Exception {
        if (srcFile.isDirectory()) {
            archiveDir(srcFile, taos, basePath);
        } else {
            archiveFile(srcFile, taos, basePath);
        }
    }

    /**
     * 目录归档
     *
     * @param dir
     * @param taos     TarArchiveOutputStream
     * @param basePath
     * @throws Exception
     */
    private static void archiveDir(File dir, TarArchiveOutputStream taos, String basePath) throws Exception {
        File[] files = dir.listFiles();
        if (files.length < 1) {
            TarArchiveEntry entry = new TarArchiveEntry(basePath + dir.getName() + PATH);
            taos.putArchiveEntry(entry);
            taos.closeArchiveEntry();
        }
        for (File file : files) {
            // 递归归档
            archive(file, taos, basePath + dir.getName() + PATH);
        }
    }

    /**
     * 数据归档
     *
     * @param taos TarArchiveOutputStream
     * @throws Exception
     */
    private static void archiveFile(File file, TarArchiveOutputStream taos, String dir) throws Exception {

        /**
         * 归档内文件名定义
         *
         * <pre>
         * 如果有多级目录，那么这里就需要给出包含目录的文件名
         * 如果用WinRAR打开归档包，中文名将显示为乱码
         * </pre>
         */
        TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());
        entry.setSize(file.length());
        taos.putArchiveEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int count;
        byte[] data = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            taos.write(data, 0, count);
        }
        bis.close();
        taos.closeArchiveEntry();
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @throws Exception
     */
    public static void dearchive(File srcFile) throws Exception {
        String basePath = srcFile.getParent();
        dearchive(srcFile, basePath);
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void dearchive(File srcFile, File destFile) throws Exception {
        TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(srcFile));
        dearchive(destFile, tais);
        tais.close();
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void dearchive(File srcFile, String destPath) throws Exception {
        dearchive(srcFile, new File(destPath));
    }

    /**
     * 文件 解归档
     *
     * @param destFile 目标文件
     * @param tais     ZipInputStream
     * @throws Exception
     */
    private static void dearchive(File destFile, TarArchiveInputStream tais)
            throws Exception {
        TarArchiveEntry entry;
        while ((entry = tais.getNextTarEntry()) != null) {
            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            // 文件检查
            FilesUtil.fileProber(dirFile);
            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                dearchiveFile(dirFile, tais);
            }
        }
    }

    /**
     * 文件 解归档
     *
     * @param srcPath 源文件路径
     * @throws Exception
     */
    public static void dearchive(String srcPath) throws Exception {
        File srcFile = new File(srcPath);
        dearchive(srcFile);
    }

    /**
     * 文件 解归档
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @throws Exception
     */
    public static void dearchive(String srcPath, String destPath) throws Exception {
        File srcFile = new File(srcPath);
        dearchive(srcFile, destPath);
    }

    /**
     * 文件解归档
     *
     * @param destFile 目标文件
     * @param tais     TarArchiveInputStream
     * @throws Exception
     */
    private static void dearchiveFile(File destFile, TarArchiveInputStream tais) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        int count;
        byte[] data = new byte[BUFFER];
        while ((count = tais.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }


}
