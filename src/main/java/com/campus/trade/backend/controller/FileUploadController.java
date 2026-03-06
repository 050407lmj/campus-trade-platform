package com.campus.trade.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 文件上传控制器
 * 
 * 处理图片上传功能，支持：
 * - 单张图片上传
 * - 多张图片批量上传
 * - 图片格式验证
 * - 图片大小限制
 */
@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    // 图片存储目录 - 使用绝对路径
    private static final String UPLOAD_DIR;
    
    static {
        // 初始化上传目录
        String userDir = System.getProperty("user.dir");
        UPLOAD_DIR = userDir + File.separator + "uploads" + File.separator + "images";
        
        // 确保目录存在
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("创建上传目录: " + UPLOAD_DIR + " - " + (created ? "成功" : "失败"));
        }
    }
    
    // 允许的图片格式
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "gif", "webp"));
    
    // 最大文件大小：10MB
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    /**
     * 单张图片上传
     * @param file 上传的文件
     * @return 包含图片 URL 的响应
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证文件
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "上传文件不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                result.put("success", false);
                result.put("message", "文件大小不能超过 5MB");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                result.put("success", false);
                result.put("message", "文件名无效");
                return ResponseEntity.badRequest().body(result);
            }
            
            String extension = getFileExtension(originalFilename);
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                result.put("success", false);
                result.put("message", "不支持的图片格式，仅支持：" + String.join(", ", ALLOWED_EXTENSIONS));
                return ResponseEntity.badRequest().body(result);
            }
            
            // 创建存储目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名
            String fileName = generateUniqueFileName(originalFilename);
            File destFile = new File(UPLOAD_DIR + File.separator + fileName);
            
            // 保存文件
            file.transferTo(destFile);
            
            // 返回图片 URL
            String imageUrl = "/api/files/" + fileName;
            
            System.out.println("文件上传成功: " + destFile.getAbsolutePath());
            
            result.put("success", true);
            result.put("message", "上传成功");
            result.put("url", imageUrl);
            result.put("fileName", fileName);
            result.put("size", file.getSize());
            
            return ResponseEntity.ok(result);
            
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * 多张图片批量上传
     * @param files 上传的文件数组
     * @return 包含所有图片 URL 的响应
     */
    @PostMapping("/upload/batch")
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> uploadedFiles = new ArrayList<>();
        
        try {
            if (files.length == 0) {
                result.put("success", false);
                result.put("message", "至少上传一张图片");
                return ResponseEntity.badRequest().body(result);
            }
            
            if (files.length > 5) {
                result.put("success", false);
                result.put("message", "最多只能上传 5 张图片");
                return ResponseEntity.badRequest().body(result);
            }
            
            // 创建存储目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 批量上传
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                
                // 验证文件大小
                if (file.getSize() > MAX_FILE_SIZE) {
                    result.put("success", false);
                    result.put("message", "文件 " + file.getOriginalFilename() + " 超过 5MB");
                    return ResponseEntity.badRequest().body(result);
                }
                
                // 验证文件类型
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) {
                    continue;
                }
                
                String extension = getFileExtension(originalFilename);
                if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                    result.put("success", false);
                    result.put("message", "文件 " + file.getOriginalFilename() + " 格式不支持");
                    return ResponseEntity.badRequest().body(result);
                }
                
                // 生成唯一文件名并保存
                String fileName = generateUniqueFileName(originalFilename);
                File destFile = new File(UPLOAD_DIR + File.separator + fileName);
                file.transferTo(destFile);
                
                // 记录上传结果
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("url", "/api/files/" + fileName);
                fileInfo.put("fileName", fileName);
                fileInfo.put("originalName", originalFilename);
                fileInfo.put("size", file.getSize());
                uploadedFiles.add(fileInfo);
            }
            
            result.put("success", true);
            result.put("message", "上传成功");
            result.put("files", uploadedFiles);
            result.put("count", uploadedFiles.size());
            
            return ResponseEntity.ok(result);
            
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * 提供静态文件访问
     * @param filename 文件名
     * @return 图片文件
     */
    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        try {
            File file = new File(UPLOAD_DIR + File.separator + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String contentType = getContentType(filename);
            
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Cache-Control", "max-age=3600")
                    .body(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    /**
     * 生成唯一的文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String extension = getFileExtension(originalFilename);
        return timestamp + "_" + random + "." + extension;
    }

    /**
     * 根据文件扩展名获取 Content-Type
     */
    private String getContentType(String filename) {
        String ext = getFileExtension(filename).toLowerCase();
        switch (ext) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}
