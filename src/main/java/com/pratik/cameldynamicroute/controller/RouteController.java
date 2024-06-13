package com.pratik.cameldynamicroute.controller;

import com.pratik.cameldynamicroute.service.ApiInvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    @Autowired
    ApiInvokeService apiInvokeService;

    @GetMapping("/callRoute")
    public String callRoute(@RequestParam("url") String url) throws Exception {
        return apiInvokeService.callAPI(url);
    }
}
