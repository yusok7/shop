package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * 파일을 처리하는 FileService 클래스
 * 파일을 업로드하는 메소드와 삭제하는 메소드 존재
 */

@Log
@Service
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // 새로이 지정될 파일의 이름
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData); // fileData 파일 출력 스트림에 입력
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deletefile = new File(filePath);

        if (deletefile.exists()) {
            deletefile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
