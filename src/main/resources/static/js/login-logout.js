function checkLogin() {
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

window.onload = checkLogin;

//로그아웃
function logout() {
    fetch('/api/users/logout', {
        method: 'GET',
    })
        .then(response => {
            if (response.ok) {
                sessionStorage.removeItem('accessToken');
                window.location.href = '/';
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
