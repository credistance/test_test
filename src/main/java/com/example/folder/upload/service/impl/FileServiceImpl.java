package com.example.folder.upload.service.impl;

import com.example.folder.upload.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl {

    private static final String UPLOAD_DIR = "D:/test"; // 固定上传目录

    /**
     * 上传文件夹并保持结构
     * @param files 包含路径的文件数组
     * @return 成功保存的文件数量
     */
    public int folderUpload(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return 0;
        }

        int savedCount = 0;

        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isEmpty()) {
                continue;
            }

            // 构建目标路径
            Path targetPath = Paths.get(UPLOAD_DIR, originalName);

            // 创建父目录（如果不存在）
            File parentDir = targetPath.getParent().toFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 保存文件
            file.transferTo(targetPath);
            savedCount++;
        }

        return savedCount;
    }


}