// 옵션 수정
// 옵션 모달창 수량 증감버튼 작동 이벤트 추가
document.addEventListener('DOMContentLoaded', function() {
    const cartQuantity = document.getElementById('quantity'); // 수량을 표시하는 input
    const increaseBtn = document.getElementById('increaseBtn'); // 증가 버튼
    const decreaseBtn = document.getElementById('decreaseBtn'); // 감소 버튼
    const editButton = document.getElementById('edit-submit-btn'); // 수정 버튼

    // 수량 증가
    increaseBtn.addEventListener('click', function () {
        cartQuantity.value = parseInt(cartQuantity.value) + 1;
    });

    // 수량 감소
    decreaseBtn.addEventListener('click', function () {
        if (parseInt(cartQuantity.value) > 1) {
            cartQuantity.value = parseInt(cartQuantity.value) - 1;
        }
    });

    // 수정 버튼 클릭 이벤트 -> 백엔드로 정보 전송
    if (editButton) {
        editButton.addEventListener('click', event => {
            const cartIdElement = document.getElementById('cart-id');
            const userIdElement = document.getElementById('user-id');
            const quantityElement = document.getElementById('quantity');

            // 요소 존재 여부 확인
            if (cartIdElement && userIdElement && quantityElement) {
                let cartId = cartIdElement.value;
                let userId = userIdElement.value;
                let quantity = quantityElement.value;

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
                    });
            } else {
                console.error("One or more elements are missing in the DOM.");
            }
        });
    }
});

// 개별 삭제
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let cartId = document.getElementById('cart-id').value;
        let userId = document.getElementById('user-id').value;
        fetch(`/api/cart/${cartId}`, {
            method: 'DELETE'
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace(`/cart/summary/${userId}`);
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