package org.zerock.apps3.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket; //S3 버킷 이름 : pse-s3-bucket

    //S3로 파일 업로드 하기
    public String upload(String filePath)throws RuntimeException{
        //UploadLocal에서 저장한 파일의 전체 경로를 이용하여 파일 불러오기
        File targetFile = new File(filePath);
        // putS3메서드를 이용하여 S3스토리지에 파일 저장
        
        String uploadImageUrl = putS3(targetFile, targetFile.getName()); // S3로 업로드
        // C:\\upload 에 저장되있는 파일을 삭제
        removeOrifinalFile(targetFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName)throws RuntimeException {
        // putObject메서드를 이용하여 S3스토리지에 파일 저장
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl
                (CannedAccessControlList.PublicRead));
                // S3에 저장된 파일을 불러올 수 있는 주소를 반환
                return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //S3로 업로드 후 원본 파일 삭제
    private void removeOrifinalFile(File targetFile) {
        // targetFile객체 안에 파일이 존재하는지 확인 && 파일을 삭제한 후 정상적으로 삭제가 됬으면 true, 에러가 발생하면 false
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }

    public void removeS3File(String fileName) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}
