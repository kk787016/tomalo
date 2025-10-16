package com.jjp.tomalo.controller;

import com.jjp.tomalo.dto.S3.request.PresignedUrlRequestDto;
import com.jjp.tomalo.dto.S3.response.PresignedUrlResponseDto;
import com.jjp.tomalo.util.S3PreSignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class UploadController {

    private final S3PreSignedUrlService s3PreSignedUrlService;

    @PostMapping("/presigned-urls")
    public ResponseEntity<PresignedUrlResponseDto> generatePresignedUrls(@RequestBody PresignedUrlRequestDto requestDto) {
        return ResponseEntity.ok(s3PreSignedUrlService.generatePresignedUrls(requestDto));
    }
}

