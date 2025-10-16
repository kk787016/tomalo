package com.jjp.tomalo.util;

import com.jjp.tomalo.dto.S3.request.PresignedUrlRequestDto;
import com.jjp.tomalo.dto.S3.response.PresignedUrlInfo;
import com.jjp.tomalo.dto.S3.response.PresignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3PreSignedUrlService {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;


    public PresignedUrlResponseDto generatePresignedUrls(PresignedUrlRequestDto requestDto) {

        List<PresignedUrlInfo> presignedUrlInfos = requestDto.getFileInfos().stream()
                .map(fileInfo -> {
                    String fileKey = "images/profiles/" + UUID.randomUUID() + "-" + fileInfo.getFileName();

                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .contentType(fileInfo.getContentType())
                            .build();

                    PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(putObjectRequest)
                            .build();

                    String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
                    return new PresignedUrlInfo(fileInfo.getFileName(), presignedUrl, fileKey);
                })
                .toList();
        return new PresignedUrlResponseDto(presignedUrlInfos);
    }

}
