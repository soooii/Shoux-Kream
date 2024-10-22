import { checkLogin, fetchNewAccessToken, logout } from "/js/useful-functions.js";


document.addEventListener('DOMContentLoaded', function() {
    checkLogin();
    fetchUserData();
    document.getElementById('saveButton').addEventListener('click', function(event) {
        event.preventDefault();
        updateUserData();
    });
});

// 유저 정보 가져오기
async function fetchUserData() {
    const token = sessionStorage.getItem('accessToken');
    console.log('Access Token:', token);
    try {
        let response = await fetch('/api/users/me', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        // 401 에러일 경우 토큰 재발급 로직 실행
        if (!response.ok) {
            console.error('Error:', response.status);
            if (response.status === 401) {
                await fetchNewAccessToken();
                return fetchUserData();
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        const userData = await response.json();
        if (!userData) {
            throw new Error('User data is undefined');
        }

        // 사용자 정보를 폼에 표시
        document.getElementById('fullEmailInput').value = userData.email;
        document.getElementById('fullNameInput').value = userData.name;

        const nicknameInput = document.getElementById('fullNicknameInput');

        nicknameInput.addEventListener('input', function () {
            if (nicknameInput.value) {
                nicknameInput.removeAttribute('placeholder');
            } else {
                nicknameInput.setAttribute('placeholder', '닉네임을 입력해주세요.');
            }
        });

        document.getElementById('currentPasswordInput').value = "";
        document.getElementById('newPasswordInput').value = "";
        document.getElementById('confirmPasswordInput').value = "";

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/users/login';
    }
}

// 회원정보 수정하기
async function updateUserData() {
    const token = sessionStorage.getItem('accessToken');
    const email = document.getElementById('fullEmailInput').value;
    const name = document.getElementById('fullNameInput').value;
    const nickname = document.getElementById('fullNicknameInput').value;
    const currentPassword = document.getElementById('currentPasswordInput').value;
    const newPassword = document.getElementById('newPasswordInput').value;
    const confirmPassword = document.getElementById('confirmPasswordInput').value;
    const passwordMatchMessage = document.getElementById('passwordMatchMessage');
    const newPasswordMatchMessage = document.getElementById('newPasswordMatchMessage');

    if (newPassword !== confirmPassword) {
        newPasswordMatchMessage.style.display = 'block';
        newPasswordMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
        return;
    }

    try {
        let response = await fetch('/api/users/me', {
            method: 'PATCH',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                name: name,
                nickname: nickname,
                password: currentPassword,
                newPassword: newPassword
            })
        });

        if (!response.ok) {
            console.error('Error:', response.status);
            if (response.status === 401) {
                console.log('토큰이 만료되었습니다. 새로운 토큰을 발급 받습니다.');
                await fetchNewAccessToken();
                return updateUserData();
            }
            else if(response.status === 418) {
                passwordMatchMessage.style.display = 'block';
                passwordMatchMessage.textContent = '현재 비밀번호가 잘못되었습니다.';
                return;
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        passwordMatchMessage.style.display = 'none';
        newPasswordMatchMessage.style.display = 'none';
        alert('회원정보가 수정되었습니다. 다시 로그인해 주세요.');
        logout();


    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}
