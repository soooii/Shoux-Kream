let itemName;
let itemPrice;
let imageUrl;
let shortDescription;
let detailDescription;
let nowDate;
let sellingPrice; // 변수를 전역으로 선언

document.addEventListener('DOMContentLoaded', function() {
    checkLoginButton(); // login-logout.js의 로그인 버튼 체크
    getItemInfo();      // getItemInfo 함수 호출
});

// URL의 쿼리 파라미터에서 itemId를 가져오는 함수
function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

function getItemInfo() {
    const token = sessionStorage.getItem('accessToken');
    if (token === null) {
        window.location.href = '/users/login';
        alert('잘못된 접근입니다.');
    }
    const itemId = getQueryParam('itemId');
    if (itemId) {
        fetch(`/api/sale/item/${itemId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json(); // JSON으로 변환
            })
            .then(data => {
                const sellItemContainer = document.querySelector('.sell-item');
                sellItemContainer.querySelector('.image').src = data.imageUrl; // 이미지 URL 추가
                sellItemContainer.querySelector('.text-container .bold-text').innerText = data.title; // 첫 번째 p 태그 내용
                sellItemContainer.querySelector('.text-container .normal-black').innerText = data.shortDescription; // 두 번째 p 태그 내용
                sellItemContainer.querySelector('.text-container .normal-gray').innerText = data.detailDescription; // 세 번째 p 태그 내용

                document.getElementById('item-price-now').innerText = data.price.toLocaleString() + ' 원';
                imageUrl = data.imageUrl;
                itemName = data.title;
                itemPrice = data.price;
                shortDescription = data.shortDescription;
                detailDescription = data.detailDescription;
            })
            .catch(error => {
                console.error('Error fetching item info:', error);
            });
    }
}

// ... (기타 코드 생략)

// 폼 전달
const form = document.getElementById('submit-form');
const inputSellPrice = document.getElementById('input-sell-price'); // 판매 가격 입력 요소
const deadlineLinks = document.querySelectorAll('.deadline-link');
let daysToAdd; // 변수를 선언해줍니다.

deadlineLinks.forEach(link => {
    link.addEventListener('click', function (e) {
        e.preventDefault(); // 링크 기본 동작 방지
        daysToAdd = this.getAttribute('data-days'); // 클릭된 링크의 data-days 값을 가져옴
        // 선택된 링크에 스타일 추가 (선택된 상태를 나타내기 위해)
        deadlineLinks.forEach(l => l.classList.remove('selected'));
        this.classList.add('selected');
    });
});

form.addEventListener('submit', function (e) {
    e.preventDefault();

    const token = sessionStorage.getItem('accessToken');
    if (token === null) {
        window.location.href = '/users/login';
        alert('잘못된 접근입니다.');
    }

    // 폼에서 판매 가격을 가져옴
    sellingPrice = inputSellPrice.value;

    // 필요한 데이터를 sessionStorage에 저장
    const itemId = getQueryParam('itemId');
    sessionStorage.setItem('itemId', itemId);
    sessionStorage.setItem('sellingPrice', sellingPrice);
    sessionStorage.setItem('daysToAdd', daysToAdd);
    sessionStorage.setItem('imageUrl', imageUrl); // 이미지 URL
    sessionStorage.setItem('itemName', itemName); // 아이템 이름
    sessionStorage.setItem('itemPrice', itemPrice); // 아이템 가격
    sessionStorage.setItem('shortDescription', shortDescription); // 짧은 설명
    sessionStorage.setItem('detailDescription', detailDescription); // 상세 설명

    // 판매 완료 페이지로 이동
    window.location.href = '/sale/complete';
});
