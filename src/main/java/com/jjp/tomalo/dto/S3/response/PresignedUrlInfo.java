package com.jjp.tomalo.dto.S3.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlInfo {
    private String originalFileName;
    private String presignedUrl;
    private String fileKey;
}
