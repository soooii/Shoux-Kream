import { checkLogin, fetchNewAccessToken } from "/js/useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function() {
    fetchUserData();

    const deleteButton = document.getElementById('delete-account-button');
    if (deleteButton) {
        deleteButton.addEventListener('click', deleteAccount);
    }
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
            console.error('Error:', response.status);
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

async function deleteAccount() {
    if (confirm('정말로 회원탈퇴를 하시겠습니까?')) {
        const token = sessionStorage.getItem('accessToken');
        try {
            let response = await fetch('/api/users/me', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                if (response.status === 401) {
                    await fetchNewAccessToken();
                    const newToken = sessionStorage.getItem('accessToken');
                    console.log('new Access Token:', newToken);
                    return deleteAccount();
                }

                if (!response.ok) {
                    throw new Error('회원탈퇴 중 오류가 발생했습니다.');
                }
            }

            alert('회원탈퇴가 완료되었습니다.');
            window.location.href = '/';
        } catch (error) {
            alert(`오류가 발생했습니다: ${error.message}`);
        }
    }
}

