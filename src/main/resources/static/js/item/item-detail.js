// 구매하기 모달창 숫자 포맷팅
const itemPriceElement = document.getElementById('item-detail-price');

// 텍스트 값 가져오기 (숫자 부분만 추출)
const rawPrice = itemPriceElement.innerText.replace('원', '').replace(/,/g, ''); // '원' 제거 및 기존 쉼표 제거
const formattedPrice = Number(rawPrice).toLocaleString(); // 1000단위로 쉼표 추가

// 포맷팅된 가격을 다시 설정
itemPriceElement.innerText = `${formattedPrice}원`;

// 장바구니 담기
document.addEventListener('DOMContentLoaded', function () {

    const addCartButton = document.getElementById('add-cart-btn'); // 장바구니 추가 버튼
    const checkOutButton = document.getElementById('checkout-btn'); // 즉시 구매 버튼

    const decreaseBtn = document.getElementById('decreaseBtn');
    const increaseBtn = document.getElementById('increaseBtn');

    const itemPriceElement = document.getElementById('item-price');

    // 텍스트 값 가져오기 (숫자 부분만 추출)
    const rawPrice = itemPriceElement.innerText.replace('원', '').replace(/,/g, ''); // '원' 제거 및 기존 쉼표 제거
    const formattedPrice = Number(rawPrice).toLocaleString(); // 1000단위로 쉼표 추가

    // 포맷팅된 가격을 다시 설정
    itemPriceElement.innerText = `${formattedPrice}원`;


    // 수량 감소
    decreaseBtn.addEventListener('click', () => {
        const quantityInput = document.getElementById('quantity');
        let totalPrice = parseInt(quantityInput.value) * parseInt(rawPrice);
        let currentQuantity = parseInt(quantityInput.value);
        if (currentQuantity > 1) {
            quantityInput.value = currentQuantity - 1;
            itemPriceElement.innerText = (totalPrice - parseInt(rawPrice)).toLocaleString() + '원';
        }
    });
    
    // 수량 증가
    increaseBtn.addEventListener('click', () => {
        const quantityInput = document.getElementById('quantity');
        let totalPrice = parseInt(quantityInput.value) * parseInt(rawPrice);
        let currentQuantity = parseInt(quantityInput.value);
        quantityInput.value = currentQuantity + 1;
        itemPriceElement.innerText = (totalPrice + parseInt(rawPrice)).toLocaleString() + '원';
    });
    
    // 장바구니 추가 버튼을 누른 경우
    if (addCartButton) {
        addCartButton.addEventListener('click', event => {
            let itemId = document.getElementById('item-id').value;
            const quantity = parseInt(document.getElementById('quantity').value);
            const token = sessionStorage.getItem('accessToken');
            if (token === null) {
                window.location.href = `/users/login`;
            }
            fetch(`/api/cart/add/${itemId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({quantity: quantity})
            })
                .then((response) => {
                    if (response.ok) {
                        alert('장바구니에 상품이 추가되었습니다.');
                        location.reload(); // 페이지 새로 고침하여 변경사항 반영
                    } else {
                        alert('장바구니에 상품 추가가 실패했습니다. 다시 시도해 주세요.');
                    }
                })
        });
    }
    
    // 즉시 구매하기 버튼을 누른 경우
    if (checkOutButton) {
        checkOutButton.addEventListener('click', event => {
            const itemId = document.getElementById('item-id').value;
            const quantity = parseInt(document.getElementById('quantity').value);

            const token = sessionStorage.getItem('accessToken');
            if (token === null) {
                window.location.href = `/users/login`;
            }

            // 실제 구매 요청 보내기
            fetch(`/api/checkout-each`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    itemId: itemId,
                    quantity: quantity
                })
            })
                .then((response) => {
                    if (response.ok) {
                        window.location.href = '/checkout-each';
                    } else {
                        alert('구매에 실패했습니다. 다시 시도해 주세요.');
                    }
                })
        })
    }
});

// 판매하기
const sellButton = document.getElementById('sellButton');

if (sellButton) {
    sellButton.addEventListener('click', event => {
        const itemId = document.getElementById('item-id-sell').value;

        const token = sessionStorage.getItem('accessToken');
        if (token === null) {
            window.location.href = `/users/login`;
        } else {
            // token이 있을 때 /sell 페이지로 이동
            window.location.href = `/sale?itemId=${itemId}`; // itemId를 쿼리 파라미터로 전달
        }
    });
}
