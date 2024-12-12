package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  文件上传接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // 文件上传存储路径
    private static final String filePath = System.getProperty("user.dir") + "/file/";
    private static final String http = "http://localhost:8080/files/";

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        synchronized (FileController.class) {
            String flag = System.currentTimeMillis() + "";
            String fileName = file.getOriginalFilename();
            try {
                if (!FileUtil.isDirectory(filePath)) {
                    FileUtil.mkdir(filePath);
                }
                // 文件存储形式：时间戳-文件名
                FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
                System.out.println(fileName + "--上传成功");
                Thread.sleep(1L);
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败");
            }
            return Result.success(http + flag + "-" + fileName);
        }
    }


    /**
     * 获取文件
     *
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    /**
     * 删除文件
     *
     * @param flag
     */
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        List<String> fileNames = FileUtil.listFileNames(filePath);
        String filename = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        FileUtil.del(filePath + filename);
        System.out.println("删除文件" + filename + "成功");
    }

	/**
	* wang-editor编辑器文件上传接口
	*/
	@PostMapping("/wang/upload")
	public Map<String, Object> wangEditorUpload(MultipartFile file) {
		String flag = System.currentTimeMillis() + "";
		String fileName = file.getOriginalFilename();
		try {
			// 文件存储形式：时间戳-文件名
			FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
			System.out.println(fileName + "--上传成功");
			Thread.sleep(1L);
		} catch (Exception e) {
			System.err.println(fileName + "--文件上传失败");
		}
		Map<String, Object> resMap = new HashMap<>();
		// wangEditor上传图片成功后， 需要返回的参数
		resMap.put("errno", 0);
		resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag)));
		return resMap;
	}

}
