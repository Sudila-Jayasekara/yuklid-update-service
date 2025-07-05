package com.yuklid.updateservice.controller;

import com.yuklid.updateservice.dto.UpdateRequest;
import com.yuklid.updateservice.dto.UpdateResponse;
import com.yuklid.updateservice.update.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/update")
public class UpdateController {

    @Autowired
    private UpdateService updateService;

    @PostMapping("/check")
    public ResponseEntity<UpdateResponse> checkUpdate(@RequestBody UpdateRequest request) {
        UpdateResponse response = updateService.checkForUpdate(request.getCurrentVersion());
        return ResponseEntity.ok(response);
    }
}
