package com.lubayi.fight.apply.security;

import com.lubayi.fight.apply.security.repository.param.RegisterParam;
import com.lubayi.fight.apply.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author lubayi
 * @date 2025/11/19
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterParam registerParam) {
        userService.registerUser(registerParam);
        return ResponseEntity.ok(Map.of("code", 200, "message", "User registered successfully"));
    }

    @GetMapping("/smsCode")
    public ResponseEntity<?> getSmsCode(@RequestParam("phone") String phone) {
        return null;
    }

}
