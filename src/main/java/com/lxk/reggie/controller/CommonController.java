package com.lxk.reggie.controller;

import com.lxk.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String fileName;
        try {
            String originalFilename = file.getOriginalFilename();
            fileName = UUID.randomUUID().toString();
            String[] suffix = originalFilename.split("\\.");
            fileName=fileName+"."+suffix[1];
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流,通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流,通过输出流向浏览器写回内容
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
