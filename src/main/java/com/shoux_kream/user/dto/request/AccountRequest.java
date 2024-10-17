package com.shoux_kream.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String email;
    private String name;
    private String nickname;
    private String password;
    private String newPassword;

}
