package com.shoux_kream.user.dto.response;

import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String name;
}
