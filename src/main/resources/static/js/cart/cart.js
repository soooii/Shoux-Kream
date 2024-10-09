// 옵션 수정
const editButtons = document.querySelectorAll('.edit-button');

editButtons.forEach(button => {
    button.addEventListener('click', event => {
        // 클릭한 항목의 정보를 가져옵니다.
        const cartItem = event.target.closest('.cart-product-item');
        const cartId = cartItem.querySelector('input[type="hidden"][id^="cart-id"]').value;
        const userId = cartItem.querySelector('input[type="hidden"][id^="user-id"]').value;
        const itemPrice = cartItem.querySelector('.fw-bold.text-primary').innerText; // 가격
        const quantity = cartItem.querySelector('input[name="quantity"]').value; // 수량

        // 모달에 필요한 데이터 설정
        const editButton = document.getElementById('edit-submit-btn');
        editButton.setAttribute('data-cart-id', cartId);
        editButton.setAttribute('data-user-id', userId);

        // 모달의 가격 및 수량 필드 업데이트
        document.getElementById('quantity').value = quantity; // 수량 업데이트
        document.querySelector('.modal-body .fw-bold.text-primary').innerText = itemPrice * quantity; // 가격 업데이트
    });
});

// 옵션 모달창 수량 증감버튼 작동 이벤트 추가
document.addEventListener('DOMContentLoaded', function () {
    const cartQuantity = document.getElementById('quantity'); // 수량을 표시하는 input
    const increaseBtn = document.getElementById('increaseBtn'); // 증가 버튼
    const decreaseBtn = document.getElementById('decreaseBtn'); // 감소 버튼
    const editButton = document.getElementById('edit-submit-btn'); // 수정 버튼
    const cartItemPrice = document.querySelector('.modal-body .fw-bold.text-primary');

    // 수량 증가
    increaseBtn.addEventListener('click', function () {
        const initialPrice = parseInt(cartItemPrice.innerText) / cartQuantity.value // 초기 가격을 저장
        cartQuantity.value = parseInt(cartQuantity.value) + 1;
        cartItemPrice.innerText = parseInt(cartItemPrice.innerText) + initialPrice;
    });

    // 수량 감소
    decreaseBtn.addEventListener('click', function () {
        if (parseInt(cartQuantity.value) > 1) {
            const initialPrice = parseInt(cartItemPrice.innerText) / cartQuantity.value
            cartQuantity.value = parseInt(cartQuantity.value) - 1;
            cartItemPrice.innerText = parseInt(cartItemPrice.innerText) - initialPrice;
        }
    });

    // 수정 버튼 클릭 이벤트 -> 백엔드로 정보 전송
    if (editButton) {
        editButton.addEventListener('click', event => {
            const cartId = editButton.getAttribute('data-cart-id');
            const userId = editButton.getAttribute('data-user-id');
            const quantity = parseInt(document.getElementById('quantity').value);

            fetch(`/api/cart/edit/${cartId}`, {
                method: 'PATCH',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ quantity: quantity })
            })
                .then(() => {
                    alert('변경이 완료되었습니다.');
                    location.replace(`/cart/summary/${userId}`);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    }
});

// cart-product-item 내의 각 삭제 버튼을 가져옴
const deleteButtons = document.querySelectorAll('.delete-button');

// 각 삭제 버튼에 클릭 이벤트 추가
deleteButtons.forEach(button => {
    button.addEventListener('click', event => {
        // 선택한 상품의 cartId와 userId를 모달의 삭제 버튼에 설정
        const cartId = event.target.closest('.cart-product-item').querySelector('input[type="hidden"][id^="cart-id"]').value;
        const userId = event.target.closest('.cart-product-item').querySelector('input[type="hidden"][id^="user-id"]').value;

        // 모달 내 삭제 버튼에 cartId와 userId를 설정
        const deleteButton = document.getElementById('delete-btn');
        deleteButton.setAttribute('data-cart-id', cartId);
        deleteButton.setAttribute('data-user-id', userId);
    });
});

// 모달 내 삭제 버튼 클릭 시 동작
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        // data-cart-id와 data-user-id에서 값을 가져옴
        const cartId = deleteButton.getAttribute('data-cart-id');
        const userId = deleteButton.getAttribute('data-user-id');

        // 실제 삭제 요청 보내기
        fetch(`/api/cart/${cartId}?userId=${userId}`, {
            method: 'DELETE'
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace(`/cart/summary/${userId}`);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });
}

// 일괄 삭제
const partialDeleteLabel = document.getElementById('partialDeleteLabel');

if (partialDeleteLabel) {
    partialDeleteLabel.addEventListener('click', event => {
        // 선택된 체크박스
        const selectedCheckboxes = document.querySelectorAll('.cart-checkbox:checked');
        const cartIds = Array.from(selectedCheckboxes).map((checkbox) => checkbox.value);

        if (cartIds.length == 0) {
            alert('삭제할 상품을 선택하세요.');
            return;
        }

        // 사용자에게 삭제 확인
        const confirmDelete = confirm('선택한 상품을 정말로 삭제하시겠습니까?');

        if (confirmDelete) {
            fetch('/api/cart/delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(cartIds), // 배열 형태로 전송
            })
                .then((response) => {
                    if (response.ok) {
                        alert('선택한 상품이 삭제되었습니다.');
                        location.reload(); // 페이지 새로 고침하여 변경사항 반영
                    } else {
                        alert('삭제 실패했습니다. 다시 시도해 주세요.');
                    }
                })
                .catch((error) => {
                    console.error('삭제 중 오류 발생:', error);
                    alert('삭제 중 오류가 발생했습니다.');
                });
        } else {
            alert('삭제가 취소되었습니다.');
        }
    });
}


// 체크 박스로 상품 전체 선택 or 전체 해제
const allSelectCheckbox = document.getElementById('allSelectCheckbox');
const cartCheckboxes = document.querySelectorAll('.cart-checkbox');

if (allSelectCheckbox) {
    allSelectCheckbox.addEventListener('change', () => {
        cartCheckboxes.forEach(checkbox => {
            checkbox.checked = allSelectCheckbox.checked; // 전체 선택 체크박스의 상태를 상품 체크박스에 반영
        });
    });
}