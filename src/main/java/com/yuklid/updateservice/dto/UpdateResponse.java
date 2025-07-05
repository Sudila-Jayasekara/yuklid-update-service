package com.yuklid.updateservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponse {
    private boolean isLatest;
    private String downloadUrl;
    private String latestVersion;
}