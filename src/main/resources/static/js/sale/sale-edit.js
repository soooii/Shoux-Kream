import {checkLogin} from "/js/useful-functions.js";

checkLogin();

let itemIdRequest;
let sellingPriceRequest;

document.addEventListener('DOMContentLoaded', function() {
    getItemInfo(); // getItemInfo 함수 호출
});

function getSaleIdFromUrl() {
    const path = window.location.pathname; // 현재 URL의 경로를 가져옴
    const saleId = path.split('/').pop(); // 마지막 부분을 가져옴
    return saleId;
}

function getItemInfo() {
    const token = sessionStorage.getItem('accessToken');
    const saleId = getSaleIdFromUrl(); // saleId 가져오기
    if (token === null) {
        window.location.href = '/users/login';
        alert('잘못된 접근입니다.');
        return;
    }

    if (saleId) {
        fetch(`/api/sale/${saleId}`, {
            method: 'GET',
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
                sellItemContainer.querySelector('.text-container .bold-text').innerText = data.itemName; // 첫 번째 p 태그 내용
                sellItemContainer.querySelector('.text-container .normal-black').innerText = data.shortDescription; // 두 번째 p 태그 내용
                sellItemContainer.querySelector('.text-container .normal-gray').innerText = data.detailDescription; // 세 번째 p 태그 내용

                document.getElementById('item-price-now').innerText = data.itemPrice.toLocaleString() + ' 원';

                // 초기 입력 값 설정
                inputSellPrice.value = data.sellingPrice; // 서버에서 가져온 가격으로 초기화
                updateCalculatedValues(data.sellingPrice); // 초기 값으로 계산된 값 업데이트

                // itemId 저장
                itemIdRequest = data.itemId;

                // 마감 기한 초기화
                const dueDate = new Date(data.dueDate);
                const formattedDueDate = dueDate.toISOString().split('T')[0].replace(/-/g, '/');
                const daysRemaining = Math.ceil((dueDate - new Date()) / (1000 * 60 * 60 * 24)); // 남은 일수 계산

                const deadlineText = `${daysRemaining}일 (${formattedDueDate} 마감)`;
                document.querySelector('.deadline-txt').innerText = deadlineText;
            })
            .catch(error => {
                console.error('Error fetching item info:', error);
            });
    }
}

// 수수료를 동적으로 계산해 화면에 보여줌
const inputSellPrice = document.getElementById('input-sell-price');
const inspectionElement = document.querySelector('.inspection');
const feeElement = document.querySelector('.fee');
const amountElement = document.querySelector('.amount');

function updateCalculatedValues(sellPrice) {
    const fee = sellPrice * 0.1; // 10% 계산
    inspectionElement.innerText = '무료'; // 검수비 업데이트
    feeElement.innerText = `-${fee.toLocaleString()}원`; // 가격 업데이트

    const amount = sellPrice - fee; // sellPrice - fee 계산
    amountElement.innerText = `${amount.toLocaleString()}원`; // 최종 금액 업데이트
}

inputSellPrice.addEventListener('input', function () {
    const sellPrice = parseFloat(inputSellPrice.value);
    if (!isNaN(sellPrice)) {
        updateCalculatedValues(sellPrice); // 입력값으로 계산된 값 업데이트
    } else {
        feeElement.innerText = ''; // 입력값이 없을 때는 비우기
        amountElement.innerText = ''; // 입력값이 없을 때는 비우기
    }
});

// 입찰 마감기한 동적 계산
document.querySelectorAll('.deadline-link').forEach(link => {
    link.addEventListener('click', function (event) {
        event.preventDefault(); // 링크 클릭 시 기본 동작 방지

        const days = parseInt(this.dataset.days); // 선택한 일수
        const today = new Date(); // 오늘 날짜
        today.setDate(today.getDate() + days); // 오늘 날짜에 일수 추가

        // 날짜 포맷 yyyy/mm/dd
        const formattedDate = today.toISOString().split('T')[0].replace(/-/g, '/');

        // 결과 출력
        const deadlineText = `${days}일 (${formattedDate} 마감)`;
        document.querySelector('.deadline-txt').innerText = deadlineText;
    });
});


// 검수기준 모달창
document.addEventListener('DOMContentLoaded', () => {
    // Functions to open and close a modal
    function openModal($el) {
        $el.classList.add('is-active');
    }

    function closeModal($el) {
        $el.classList.remove('is-active');
    }

    function closeAllModals() {
        (document.querySelectorAll('.modal') || []).forEach(($modal) => {
            closeModal($modal);
        });
    }

    // Add a click event on buttons to open a specific modal
    (document.querySelectorAll('.js-modal-trigger') || []).forEach(($trigger) => {
        const modal = $trigger.dataset.target;
        const $target = document.getElementById(modal);

        $trigger.addEventListener('click', () => {
            openModal($target);
        });
    });

    // Add a click event on various child elements to close the parent modal
    (document.querySelectorAll('.modal-background, .modal-close, .modal-card-head .delete, .modal-card-foot .button') || []).forEach(($close) => {
        const $target = $close.closest('.modal');

        $close.addEventListener('click', () => {
            closeModal($target);
        });
    });

    // Add a keyboard event to close all modals
    document.addEventListener('keydown', (event) => {
        if(event.key === "Escape") {
            closeAllModals();
        }
    });
});

// 폼 전달
const form = document.getElementById('submit-form');
const deadlineLinks = document.querySelectorAll('.deadline-link');
let daysToAdd; // 변수를 선언

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

    const saleId = getSaleIdFromUrl(); // saleId 가져오기

    // 폼에서 판매 가격을 가져옴
    sellingPriceRequest = inputSellPrice.value;

    fetch(`/api/sale/${saleId}`, {
        method: 'PATCH',
        headers: {
            "Content-Type": "application/json",
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({itemId: itemIdRequest, sellingPrice: sellingPriceRequest, daysToAdd: daysToAdd})
    })
        .then(() => {
            alert('변경이 완료되었습니다.');
            location.replace(`/sale/${saleId}`);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
