package com.wyb.vue.fileupload.controller;

import net.sf.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/media")
public class MediaController {

    @RequestMapping("/upload")
    public JSONObject upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        result.put("success", false);
        if (!file.isEmpty()) {
            String saveFileName = file.getOriginalFilename();
            File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
            System.out.println("saveFile:       " + saveFile.getAbsolutePath());
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                result.put("success", true);
                result.put("note", saveFile.getName() + " 上传成功");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result.put("note", "上传失败," + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                result.put("note", "上传失败," + e.getMessage());
            }
        } else {
            result.put("note", "上传失败，因为文件为空.");
        }
        return result;
    }
    

    @PostMapping("/downloadFile")
    public HttpServletResponse download(@RequestBody Map<String,String> filePath, HttpServletResponse response) {
        try {
            //前台传文件路径。为了方便演示，文件直接放resources,前台传的路径就不用了
            //String path = filePath.get("filePath");
            File file = ResourceUtils.getFile("classpath:test.jpeg");
            //文件不存在就不判断了。。。。。


            String filename = file.getName();
            System.out.println(file.getAbsolutePath());

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            System.out.println(fis.available());
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }


}
