package com.yuklid.updateservice.update;

import com.yuklid.updateservice.dto.UpdateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.net.URI;
import java.time.Duration;
@Service
public class UpdateService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final VersionService versionService;


    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public UpdateService(S3Client s3Client, S3Presigner s3Presigner,  VersionService versionService) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.versionService = versionService;
    }

    public UpdateResponse checkForUpdate(String currentVersion) {
        String latestKey = versionService.getLatestVersionKey();
        if (latestKey == null) {
            return new UpdateResponse(true, null, currentVersion); // No update found
        }

        String latestVersion = versionService.extractVersion(latestKey);

        if (currentVersion != null && currentVersion.equals(latestVersion)) {
            return new UpdateResponse(true, null, latestVersion);
        } else {
            String presignedUrl = generatePresignedUrl(latestKey);
            return new UpdateResponse(false, presignedUrl, latestVersion);
        }
    }


    private String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
