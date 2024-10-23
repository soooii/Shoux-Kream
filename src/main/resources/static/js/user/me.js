import { checkLogin, fetchNewAccessToken, logout } from "/js/useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function() {
    fetchUserData();
});

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

        if (!response.ok) {
            if (response.status === 401) {
                await fetchNewAccessToken();
                const newToken = sessionStorage.getItem('accessToken');
                console.log('new Access Token:', newToken);
                return fetchUserData();
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }
        const data = await response.json();
        if (!data) {
            throw new Error('User data is undefined');
        }
        document.getElementById('name').innerText = data.name;
    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/users/login';
    }
}



