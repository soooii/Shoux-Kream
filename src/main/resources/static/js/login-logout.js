// 로그인/로그아웃 버튼 변경 함수
function checkLoginButton() {
    const token = sessionStorage.getItem('accessToken');

    if (token) {
        // 로그인 상태일 때
        document.getElementById('loginButton').style.display = 'none';
        document.getElementById('logoutButton').style.display = 'inline';
    } else {
        // 로그아웃 상태일 때
        document.getElementById('loginButton').style.display = 'inline';
        document.getElementById('logoutButton').style.display = 'none';
    }
}

window.onload = checkLoginButton;

//로그인 상태 확인 함수는 useful-function.js 존재

// 로그아웃
function logout() {
    const token = sessionStorage.getItem('accessToken');

    fetch('/api/users/logout', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}` // Authorization 헤더에 토큰 추가
        }
    })
        .then(response => {
            if (response.ok) {
                sessionStorage.removeItem('accessToken');  // accessToken 삭제
                window.location.href = '/';  // 메인 페이지로 리디렉션
            } else {
                console.error('로그아웃에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
}

document.addEventListener('DOMContentLoaded', function() {
    const logoutButton = document.getElementById('logoutButton');

    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault();
            logout();
        });
    }
});
