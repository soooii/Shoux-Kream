let cartId; // cart Id 저장
let quantityCheck;
let priceCheck;

window.onload = function () {
    const token = sessionStorage.getItem('accessToken');
    if (token === null) {
        window.location.href = '/users/login';
        alert('로그인이 필요합니다.');
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
            const cartContainer = document.getElementById('cart-container');
            cartContainer.innerHTML = '';

            if (!data || data.length === 0) {
                const message = document.createElement('p');
                message.classList.add('content');
                message.innerText = '장바구니가 비었습니다.';
                document.getElementById('productsCount').innerText = '0개';
                document.getElementById('productsTotal').innerText = '0원';
                document.getElementById('deliveryFee').innerText = '0원';
                document.getElementById('orderTotal').innerText = '0원';
                cartContainer.appendChild(message);
                return;
            }

            let totalQuantity = 0;
            let totalPrice = 0;

            data.forEach((item) => {
                const id = item.cartId;
                const itemId = item.itemId;

                const itemImage = document.createElement('img');
                const itemName = document.createElement('p');
                const quantity = document.createElement('span');
                const price = document.createElement('p');

                const cartItemDiv = document.createElement('div');
                cartItemDiv.classList.add('cart-product-item');

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.name = `checkbox`;
                checkbox.value = id;
                checkbox.checked = true;
                checkbox.classList.add('custom-checkbox');

                itemImage.classList.add('image');
                itemName.classList.add('content');
                quantity.classList.add('content');
                price.classList.add('content');

                itemImage.src = `${item.imageUrl}`;
                itemName.innerText = `${item.itemName}`;
                quantity.innerText = `${item.quantity}개`;
                price.innerText = `${item.totalPrice.toLocaleString()}원`;

                // 링크 추가: 이미지와 상품명, 수량, 가격을 포함
                const itemLink = document.createElement('a');
                itemLink.href = `/item/item-detail/${itemId}`;
                itemLink.style.textDecoration = 'none';
                itemLink.style.color = 'inherit';

                // itemLink에 내용 추가
                const itemLinkDiv = document.createElement('div');
                itemLinkDiv.style.display = 'flex'; // Flexbox 사용
                itemLinkDiv.style.alignItems = 'center'; // 세로 정렬
                itemLinkDiv.appendChild(itemImage);
                itemLinkDiv.appendChild(itemName);
                itemLinkDiv.appendChild(quantity);
                itemLinkDiv.appendChild(price);

                itemLink.appendChild(itemLinkDiv); // 링크에 itemLinkDiv 추가

                // 체크박스 추가
                cartItemDiv.appendChild(checkbox);
                cartItemDiv.appendChild(itemLink); // 링크를 cartItemDiv에 추가

                // 개별 삭제 버튼
                const deleteButton = document.createElement('button');
                deleteButton.innerText = '삭제';
                deleteButton.classList.add('delete-button');
                deleteButton.setAttribute('data-bs-toggle', 'modal');
                deleteButton.setAttribute('data-bs-target', '#staticDeleteBackdrop');
                deleteButton.addEventListener('click', (event) => {
                    event.stopPropagation(); // 이벤트 전파 중단
                    cartId = id; // 선택된 cartId 저장
                });

                // 수정 버튼
                const editButton = document.createElement('button');
                editButton.innerText = '옵션/수량 변경';
                editButton.classList.add('edit-button');
                editButton.setAttribute('data-bs-toggle', 'modal');
                editButton.setAttribute('data-bs-target', '#staticBackdrop');
                editButton.addEventListener('click', (event) => {
                    event.stopPropagation(); // 이벤트 전파 중단
                    cartId = id; // 선택된 cartId 저장
                    quantityCheck = `${item.quantity}`;
                    priceCheck = `${item.itemPrice}`;

                    const increaseBtn = document.getElementById('increaseBtn');
                    const decreaseBtn = document.getElementById('decreaseBtn');
                    const cartQuantity = document.getElementById('quantity');
                    const cartItemPrice = document.querySelector('.modal-body .fw-bold.has-text-link');

                    cartQuantity.value = quantityCheck;
                    cartItemPrice.innerText = (priceCheck * quantityCheck).toLocaleString() + '원';

                    // 수량 증가
                    increaseBtn.addEventListener('click', function () {
                        let currentQuantity = parseInt(cartQuantity.value);
                        currentQuantity++;
                        cartQuantity.value = currentQuantity;
                        cartItemPrice.innerText = (priceCheck * currentQuantity).toLocaleString() + '원';
                        quantityCheck = currentQuantity;
                    });

                    // 수량 감소
                    decreaseBtn.addEventListener('click', function () {
                        let currentQuantity = parseInt(cartQuantity.value);
                        if (currentQuantity > 1) {
                            currentQuantity--;
                            cartQuantity.value = currentQuantity;
                            cartItemPrice.innerText = (priceCheck * currentQuantity).toLocaleString() + '원';
                            quantityCheck = currentQuantity;
                        }
                    });
                });

                // cartItemDiv에 버튼 추가
                cartItemDiv.appendChild(deleteButton);
                cartItemDiv.appendChild(editButton);

                // 가격 및 수량 업데이트
                totalQuantity += item.quantity;
                totalPrice += item.totalPrice;

                cartContainer.appendChild(cartItemDiv); // 컨테이너에 추가
            });

            function updateOrderSummary() {
                let selectedItemsTotalQuantity = 0;
                let selectedItemsTotalPrice = 0;
                let deliveryFee = 0;
                const allSelectCheckbox = document.getElementById('allSelectCheckbox');
                allSelectCheckbox.checked = true;

                const checkboxes = cartContainer.querySelectorAll('input[type="checkbox"]');
                checkboxes.forEach((checkbox, index) => {
                    const itemPrice = data[index].totalPrice;
                    const itemQuantity = data[index].quantity;

                    if (checkbox.checked) {
                        selectedItemsTotalQuantity += itemQuantity;
                        selectedItemsTotalPrice += itemPrice;
                        deliveryFee = 3000;
                    } else {
                        allSelectCheckbox.checked = false;
                    }
                });

                document.getElementById('productsCount').innerText = selectedItemsTotalQuantity + '개';
                document.getElementById('productsTotal').innerText = selectedItemsTotalPrice.toLocaleString() + '원';
                document.getElementById('deliveryFee').innerText = deliveryFee.toLocaleString() + '원';
                document.getElementById('orderTotal').innerText = (selectedItemsTotalPrice + deliveryFee).toLocaleString() + '원';
            }

            updateOrderSummary();

            const allSelectCheckbox = document.getElementById('allSelectCheckbox');
            allSelectCheckbox.checked = true;
            allSelectCheckbox.addEventListener('change', function () {
                const checkboxes = cartContainer.querySelectorAll('input[type="checkbox"]');
                checkboxes.forEach((checkbox) => {
                    checkbox.checked = allSelectCheckbox.checked;
                });
                updateOrderSummary();
            });
        })
        .catch(error => {
            console.error('Error:', error);
            window.location.href = '/';
        });
};




// 옵션/수량 수정
const editButton = document.getElementById('edit-submit-btn');

if (editButton) {
    editButton.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        if (token === null) {
            window.location.href = '/cart/summary';
        }
        // 실제 수정 요청 보내기
        fetch(`/api/cart/edit/${cartId}`, {
            method: 'PATCH',
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({quantity: quantityCheck})
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
        if (token === null) {
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

// 선택 삭제
const partialDeleteLabel = document.getElementById('partialDeleteLabel');

if (partialDeleteLabel) {
    partialDeleteLabel.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        if (token === null) {
            window.location.href = '/cart/summary';
        }
        // 선택된 체크박스
        const checkedCheckboxes = document.querySelectorAll("input[type='checkbox']:checked:not(#allSelectCheckbox)");
        const cartIds = []; // cartIds 리스트 초기화
        checkedCheckboxes.forEach(checkbox => {
            cartIds.push(checkbox.value); // 각 체크박스의 value 값을 cartIds 리스트에 추가
        });

        if (cartIds.length === 0) {
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
                body: JSON.stringify(cartIds) // 배열 형태로 전송
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

// 구매하기 누를 경우 checkout으로 정보 넘겨주기
const checkoutButton = document.getElementById('purchaseButton');

if (checkoutButton) {
    checkoutButton.addEventListener('click', event => {
        const token = sessionStorage.getItem('accessToken');
        if (token === null) {
            window.location.href = '/cart/summary';
        }
        // 선택된 체크박스
        const checkedCheckboxes = document.querySelectorAll("input[type='checkbox']:checked:not(#allSelectCheckbox)");
        const cartIds = []; // cartIds 리스트 초기화
        checkedCheckboxes.forEach(checkbox => {
            cartIds.push(checkbox.value); // 각 체크박스의 value 값을 cartIds 리스트에 추가
        });

        if (cartIds.length === 0) {
            alert('구매할 상품을 선택하세요.');
            window.location.href = '/';
            return;
        }

        fetch('/api/cart/selected', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(cartIds) // 배열 형태로 전송
        })
            .then((response) => {
                if (response.ok) {
                    window.location.href = '/checkout'; // 성공 시 리다이렉트
                } else {
                    alert('상품 구매에 실패했습니다. 다시 시도해 주세요.');
                }
            })
    });
}