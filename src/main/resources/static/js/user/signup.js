const form = document.querySelector('form');
form.addEventListener('submit', function(e) {
    e.preventDefault();

    const email = document.getElementById('exampleInputEmail1').value;
    const password = document.getElementById('exampleInputPassword1').value;
    const name = document.getElementById('exampleInputName').value;
    const nickname = document.getElementById('exampleInputNickname').value;
    const terms = document.getElementById('exampleCheck1').checked;

    if (!terms) {
        alert("이용 약관을 동의해주세요.");
        return;
    }

    const loginData = {email: email, password: password, name: name, nickname: nickname};

    fetch('/api/users/signup', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (response.ok) {
                console.log('회원가입 성공')
                window.location.href = '/users/login';
            } else {
                alert('회원가입에 실패했습니다.');}
        })
});

