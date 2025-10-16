package com.jjp.tomalo.dto.S3.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PresignedUrlResponseDto {

    private List<PresignedUrlInfo> presignedUrls;
}

