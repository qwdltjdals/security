package com.study.springSecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RoleController {

    public ResponseEntity<?> init() {
        return ResponseEntity.ok().body(null);
    }
}
