package com.example.basic.Service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

//파일업로드 및 삭제 처리
@Service
public class FileService {
    //파일 업로드
    //uploadFile(저장위치, 파일명(a.jpg), 파일데이터)--->새로운파일명(1236546.jpg)
    //fixme 04-03 throws Exception문구 추가
    public String uploadFile(String UploadPath, String originalFileName,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); //난수로 이름을 생성
        //abc.jpg===>jpg 분리(이미지파일의 확장자 분리)
        String extension = originalFileName.substring(
                originalFileName.lastIndexOf("."));

        //45651233.jpg =>난수파일명에 확장자를 결합해서 새로운 파일명을 만들기(전달값)
        String saveFileName = uuid.toString()+extension;

        //c:salad/board/45651233.jpg 저장할 위치와 파일명을 결합(작업처리)
        String uploadFullUrl = UploadPath+saveFileName;

        //하드 디스크에 파일저장 fixme -1꺼 대신적은거
        FileOutputStream fos = new FileOutputStream(uploadFullUrl);
        fos.write(fileData);
        fos.close();

//        //파일 저장 경로 설정  fixme 기존꺼 04-03 -1
//        File uploadPathDirectory = new File(UploadPath);
//        if (!uploadPathDirectory.exists()) {
//            uploadPathDirectory.mkdirs();
//        }
//        //하드디스크에 파일 저장
//        try {
//            //c:salad/board/45651233.jpg 이름으로
//            FileOutputStream fos = new FileOutputStream(uploadFullUrl);
//            //파일을 저장
//            fos.write(fileData);
//            fos.close();
//        } catch(Exception e) {
//
//        }

        return saveFileName;
    }

    //파일 삭제처리 (수정시 기존껄 삭제하고 새로운파일저장)
    //fixme 04-03 throws Exception 추가
    public void deleteFile(String UploadPath, String fileName)throws Exception {
        //경로+파일명
        String deleteFileName = UploadPath+fileName;

        File deleteFile = new File(deleteFileName);
        if(deleteFile.exists()) {
            deleteFile.delete();
        }
    }
}
