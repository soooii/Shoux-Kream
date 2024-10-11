let cartId; // cart Id 저장
let quantity;

window.onload = function () {
    const token = sessionStorage.getItem('accessToken');
    if (token == null) {
        window.location.href = '/';
    }
    fetch('/api/cart/summary', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            // 데이터가 리스트인 경우 반복하여 처리
            const cartContainer = document.getElementById('cart-container'); // 카트 항목을 담을 컨테이너
            cartContainer.innerHTML = ''; // 기존 내용 초기화

            // 장바구니가 빈 경우
            if (!data || data.length === 0) {
                const message = document.createElement('p');
                message.classList.add('content');
                message.innerText = '장바구니가 비었습니다.';
                cartContainer.appendChild(message);
                return; // 더 이상 진행하지 않음
            }

            data.forEach((item) => {
                const id = item.cartId;

                const itemName = document.createElement('p');
                const quantity = document.createElement('span');
                const price = document.createElement('p');

                const cartItemDiv = document.createElement('div'); // 목록을 담음
                cartItemDiv.classList.add('cart-product-item')

                const checkbox = document.createElement('input'); // 체크박스 요소 생성
                checkbox.type = 'checkbox'; // 체크박스 타입 설정

                checkbox.checked = true;

                itemName.classList.add('content'); // 클래스 추가
                quantity.classList.add('quantity'); // 클래스 추가
                price.classList.add('content'); // 클래스 추가

                itemName.innerText = `${item.itemName}`;
                quantity.innerText = `${item.quantity}개`;
                price.innerText = `${item.totalPrice.toLocaleString()}원`;

                // 개별 삭제 버튼
                const deleteButton = document.createElement('button');
                deleteButton.innerText = '삭제';
                deleteButton.classList.add('delete-button');
                deleteButton.setAttribute('data-bs-toggle', 'modal');
                deleteButton.setAttribute('data-bs-target', '#staticDeleteBackdrop'); // 모달 ID 설정
                deleteButton.addEventListener('click', () => {
                    cartId = id; // 선택된 cartId 저장
                });

                // 수정 버튼
                const editButton = document.createElement('button');
                editButton.innerText = '옵션/수량 변경';
                editButton.classList.add('edit-button');
                editButton.setAttribute('data-bs-toggle', 'modal');
                editButton.setAttribute('data-bs-target', '#staticBackdrop');
                editButton.addEventListener('click', () => {
                    cartId = id; // 선택된 cartId 저장
                    quantity = item.quantity;
                });

                // 목록에 내용 추가
                cartItemDiv.appendChild(checkbox);
                cartItemDiv.appendChild(itemName);
                cartItemDiv.appendChild(quantity);
                cartItemDiv.appendChild(price);
                cartItemDiv.appendChild(deleteButton);
                cartItemDiv.appendChild(editButton);

                cartContainer.appendChild(cartItemDiv); // 컨테이너에 추가
            });

            const allSelectCheckbox = document.getElementById('allSelectCheckbox');
            allSelectCheckbox.checked = true;
            allSelectCheckbox.addEventListener('change', function () {
                // 모든 체크박스의 상태를 변경
                const checkboxes = cartContainer.querySelectorAll('input[type="checkbox"]');
                checkboxes.forEach((checkbox) => {
                    checkbox.checked = allSelectCheckbox.checked;
                });
            });
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = '/';
        });
};

// TODO 옵션/수량 수정
// 옵션/수량 수정
const editButton = document.getElementById('edit-submit-btn');

if (editButton) {
    editButton.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        if (token == null) {
            window.location.href = '/cart/summary';
        }
        // 실제 수정 요청 보내기
        fetch(`/api/cart/edit/${cartId}`, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(quantity)
        })
            .then(() => {
                alert('변경이 완료되었습니다.');
                location.replace(`/cart/summary`);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });
}

// 개별 삭제
// 모달 내 삭제 버튼 클릭 시 동작
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        if (token == null) {
            window.location.href = '/cart/summary';
        }
        // 실제 삭제 요청 보내기
        fetch(`/api/cart/${cartId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace(`/cart/summary`);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    });
}

// TODO 선택 삭제
// 선택 삭제
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
            fetch('/api/cart/', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
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