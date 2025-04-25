package com.example.folder.upload.service.impl;

import com.example.folder.upload.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

@Service
public class ZipUploadService implements FileService {

    private static final String UPLOAD_DIR = "D:/test";
    private static final int BUFFER_SIZE = 4096;

    /**
     * 上传并解压ZIP文件夹
     * @param zipFile 上传的ZIP文件
     * @return 解压的文件数量
     */
    @Override
    public int folderUpload(MultipartFile zipFile) throws IOException {
        if (zipFile == null || zipFile.isEmpty()) {
            throw new IllegalArgumentException("ZIP文件不能为空");
        }

        // 创建基础目录
        Path basePath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(basePath);

        // 生成唯一解压目录
        String folderName = getFolderName(zipFile.getOriginalFilename());
        Path targetPath = basePath.resolve(folderName);
        
        // 解压ZIP文件
        return unzip(zipFile.getInputStream(), targetPath);
    }

    /**
     * 解压ZIP文件
     */
    private int unzip(InputStream zipInputStream, Path targetPath) throws IOException {
        int fileCount = 0;

        // 使用GBK编码解析文件名（兼容中文Windows系统）
        Charset charset = Charset.forName("GBK");

        try (ZipInputStream zis = new ZipInputStream(zipInputStream, charset)) {
            ZipEntry entry;
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((entry = zis.getNextEntry()) != null) {
                try {
                    String entryName = entry.getName();
                    Path entryPath = targetPath.resolve(entryName).normalize();

                    // 安全检查
                    if (!entryPath.startsWith(targetPath)) {
                        throw new IOException("非法ZIP条目路径: " + entryName);
                    }

                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        Files.createDirectories(entryPath.getParent());

                        try (OutputStream fos = Files.newOutputStream(entryPath)) {
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                        fileCount++;
                    }
                } catch (Exception e) {
                    // 记录错误但继续处理其他文件

                } finally {
                    zis.closeEntry();
                }
            }
        } catch (ZipException e) {
            throw new IOException("ZIP文件格式无效或已损坏", e);
        }
        return fileCount;
    }

    /**
     * 从文件名生成唯一目录名
     */
    private String getFolderName(String originalFilename) {
        String baseName = originalFilename.replace(".zip", "");
        return baseName + "_" + System.currentTimeMillis();
    }
}