// 개별 삭제
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('cart-id').value;
        let userId = document.getElementById('user-id').value;
        fetch(`/api/c/carts/${id}`, {
            method: 'DELETE'
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace(`/carts/summary/${userId}`);
            });
    });
}

// 일괄 삭제


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