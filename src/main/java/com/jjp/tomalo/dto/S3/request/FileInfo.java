package com.jjp.tomalo.dto.S3.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 파일 정보를 담을 객체
@Getter
@NoArgsConstructor
public class FileInfo {
    private String fileName;
    private String contentType;
}
