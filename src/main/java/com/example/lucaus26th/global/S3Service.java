package com.example.lucaus26th.global;

import com.example.lucaus26th.global.exception.BusinessException;
import com.example.lucaus26th.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

// 프론트에서 받은 파일을 s3에 올리고, 그 파일의 s3 url을 문자열로 돌려주는 클래스
@Service
@RequiredArgsConstructor
public class S3Service {

    // S3Config에서 Bean으로 등록해둔 S3Client를 주입받음
    // 이 객체를 통해 실제 AWS S3와 통신
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String uploadIfPresent(MultipartFile file, String dirName) {
        if (file == null || file.isEmpty()){
            return null;
        }
        try{
            return upload(file, dirName);
        } catch (IOException e){
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

    }

    public String upload(MultipartFile file, String dirName) throws IOException {
        if(file == null || file.isEmpty()){
            throw new BusinessException(ErrorCode.EMPTY_FILE);
        }

        // 파일명 가져오기 -> 중복 방지를 위해 UUID 붙이기 -> S3에 저장될 key 생성
        String originalFilename = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "_" + originalFilename;
        String key = dirName + "/" + storedFileName;

        // S3 요청정보 생성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        // S3에 업로드 수행
        // MultipartFile의 InputStream(파일 데이터)을 꺼내서 S3로 보냄
        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        // DB의 필드에 저장할 URL
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }

}
