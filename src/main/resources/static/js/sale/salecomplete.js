let itemIdFinal;
let sellingPriceFinal;
let daysToAddFinal;

document.addEventListener('DOMContentLoaded', function () {
    getSaleInfo();      // getItemInfo 함수 호출
});

function getSaleInfo() {
    const token = sessionStorage.getItem('accessToken');
    if (token === null) {
        window.location.href = '/users/login';
        alert('잘못된 접근입니다.');
    }

    const itemId = sessionStorage.getItem('itemId'); // sessionStorage에서 itemId 가져오기
    if (!itemId) {
        alert('판매 등록 정보를 찾을 수 없습니다.');
        window.location.href = '/';
    }
    itemIdFinal = itemId;

    const sellingPrice = sessionStorage.getItem('sellingPrice');
    const daysToAdd = parseInt(sessionStorage.getItem('daysToAdd'));
    const imageUrl = sessionStorage.getItem('imageUrl'); // 이미지 URL
    const itemName = sessionStorage.getItem('itemName'); // 아이템 이름
    const itemPrice = sessionStorage.getItem('itemPrice'); // 아이템 가격
    const shortDescription = sessionStorage.getItem('shortDescription'); // 짧은 설명
    const detailDescription = sessionStorage.getItem('detailDescription'); // 상세 설명
    sellingPriceFinal = sellingPrice;
    daysToAddFinal = daysToAdd;


    // HTML 요소 선택
    const sellItemContainer = document.querySelector('.sell-item');

    // 값 설정
    sellItemContainer.querySelector('.image').src = imageUrl; // 이미지 URL
    sellItemContainer.querySelector('.bold-text').innerText = itemName; // 아이템 이름
    sellItemContainer.querySelector('.normal-black').innerText = shortDescription; // 짧은 설명
    sellItemContainer.querySelector('.normal-gray').innerText = detailDescription; // 상세 설명

    // 각 dd 요소 선택
    const sellPriceElement = document.querySelector('.sell-price');
    const inspectionElement = document.querySelector('.inspection');
    const feeElement = document.querySelector('.fee');

    // 판매 희망가 설정
    sellPriceElement.innerText = `${parseFloat(sellingPrice).toLocaleString()} 원`; // 원화 포맷

    // 검수비는 '무료'로 설정
    inspectionElement.innerText = '무료';

    // 수수료 계산 및 설정
    const fee = parseFloat(sellingPrice) * 0.1; // 10% 계산
    feeElement.innerText = `-${fee.toLocaleString()} 원`; // 원화 포맷

    // 최종 정산 금액 계산
    const settlementAmount = parseFloat(sellingPrice) - fee; // 최종 금액 계산
    const settlementAmountElement = document.querySelector('.settlement-amount'); // 요소 선택
    settlementAmountElement.innerText = `${settlementAmount.toLocaleString()} 원`; // 원화 포맷


    const today = new Date(); // 오늘 날짜
    today.setDate(today.getDate() + daysToAdd); // 오늘 날짜에 일수 추가

    // 날짜 포맷 yyyy/mm/dd로 변환
    const formattedDate = today.toISOString().split('T')[0].replace(/-/g, '/');

    // 입찰 마감기한을 설정
    const deadlineText = `${daysToAdd}일 (${formattedDate} 마감)`;

    const deadlineElement = document.getElementById('due-date'); // dt 요소 선택
    const deadlineValueElement = deadlineElement.nextElementSibling; // 해당 dt의 dd 요소 선택
    deadlineValueElement.innerText = deadlineText; // dd에 값 넣기

    // 버튼 요소 선택
    const submitButton = document.getElementById('submit-btn');

    // 버튼에 최종 금액을 설정
    submitButton.innerText = `${settlementAmount.toLocaleString()}원 · 입찰하기`;
}

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
        if (event.key === "Escape") {
            closeAllModals();
        }
    });
});

const bidButton = document.getElementById('submit-btn');

if (bidButton) {
    bidButton.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        const requestData = {itemId : itemIdFinal, sellingPrice : sellingPriceFinal, daysToAdd : daysToAddFinal};

        fetch('/api/sale/sell', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(requestData)
        })
            .then(response =>{
            if(response.ok){
                alert('판매 등록이 완료되었습니다.');
                // 세션 스토리지에서 값 삭제
                sessionStorage.removeItem('sellingPrice');
                sessionStorage.removeItem('daysToAdd');
                sessionStorage.removeItem('imageUrl');
                sessionStorage.removeItem('itemName');
                sessionStorage.removeItem('itemPrice');
                sessionStorage.removeItem('shortDescription');
                sessionStorage.removeItem('detailDescription');
                sessionStorage.removeItem('itemId');

                window.location.href = `/item/item-detail/${itemIdFinal}`;
                return response.json();
            }else{
                alert('판매 등록을 다시 진행해주세요.');
                window.location.href = '/';
            }
        })
    })
}