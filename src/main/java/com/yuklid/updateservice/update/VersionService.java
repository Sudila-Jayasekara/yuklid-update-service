package com.yuklid.updateservice.update;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Comparator;
import java.util.List;

@Service
public class VersionService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public VersionService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getLatestVersionKey() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix("flutter/releases/")
                .build();

        List<S3Object> objects = s3Client.listObjectsV2(request).contents();

        return objects.stream()
                .map(S3Object::key)
                .filter(key -> key.endsWith(".apk"))
                .max(Comparator.comparing(this::extractVersion))
                .orElse(null); // returns null if no .apk found
    }

    public String extractVersion(String key) {
        // Example key: flutter/releases/app-0.1.0+7.apk
        String filename = key.substring(key.lastIndexOf("/") + 1); // app-0.1.0+7.apk
        return filename.replace("app-", "").replace(".apk", "");   // 0.1.0+7
    }
}
