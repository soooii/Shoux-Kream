package com.shoux_kream.user.dto.request;

import com.shoux_kream.user.controller.UserController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;

}
