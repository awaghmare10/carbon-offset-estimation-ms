package com.db.hackathon.controller;


import com.db.hackathon.model.CarbonOffsetRequest;
import com.db.hackathon.model.CarbonOffsetResponse;
import com.db.hackathon.service.CarbonOffsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CarbonOffsetController {

    @Autowired
    private CarbonOffsetService carbonOffsetService;

    @PostMapping("/estimate-carbon-offset")
    public CarbonOffsetResponse estimateCarbonOffset(@RequestBody CarbonOffsetRequest request) {
        return carbonOffsetService.estimateOffset(request);
    }
}