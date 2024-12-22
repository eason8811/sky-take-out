package com.sky.controller.admin;

import com.sky.constant.ImageStoreConstant;
import com.sky.constant.MessageConstant;
import com.sky.exception.FileUploadFailedException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("上传文件, 文件名称为: {}", file.getName());
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String imgName = UUID.randomUUID() + extendName;
            String imgLocation = ImageStoreConstant.FILE_TRANSFER_LOCATION + imgName;
            try {
                file.transferTo(new File(imgLocation));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return Result.success("../img/" + imgName);
        }
        throw new FileUploadFailedException(MessageConstant.UPLOAD_FAILED);
    }
}
