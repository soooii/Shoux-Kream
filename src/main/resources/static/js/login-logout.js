import { logout, checkLoginButton } from "/js/useful-functions.js";

//로그인 상태 확인 함수는 useful-function.js 존재

document.addEventListener('DOMContentLoaded', function() {
    checkLoginButton();

    const logoutButton = document.getElementById('logoutButton');

    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault();
            logout();
        });
    }
});
