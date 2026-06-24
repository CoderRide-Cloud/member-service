package com.codingclub.member.client;

import com.codingclub.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @PostMapping("/api/v1/users/bulk")
    List<UserDto> getUsersBulk(@RequestBody List<Long> userIds);
}
