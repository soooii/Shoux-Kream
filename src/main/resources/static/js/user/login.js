const form = document.querySelector('form');
form.addEventListener('submit', function(e) {
    e.preventDefault();

    const email = document.getElementById('exampleInputEmail1').value;
    const password = document.getElementById('exampleInputPassword1').value;

    const loginData = {email: email, password: password};

    fetch('/api/users/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(loginData)
    })
        .then(response =>{
            if(response.ok){
                return response.json();
            }else{
                alert('이메일 또는 비밀번호를 확인해주세요');
            }
        })
        .then(data => {
            console.log('로그인 성공');
            sessionStorage.setItem('accessToken',data.accessToken);
            window.location.href='/';
        });

});

