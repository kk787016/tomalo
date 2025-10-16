package com.jjp.tomalo.dto.S3.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PresignedUrlRequestDto {
    private List<FileInfo> fileInfos;
}

