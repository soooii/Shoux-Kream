import { checkLogin, fetchNewAccessToken } from "../useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function() {
    fetchUserCheckOuts();
});

// 주문정보 가져오기
async function fetchUserCheckOuts() {
    const token = sessionStorage.getItem('accessToken');
    console.log('Access Token:', token);
    try {
        let response = await fetch('/api/checkout', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            console.error('Error:', response.status);
            if (response.status === 401) {
                await fetchNewAccessToken();
                return fetchUserAddress();
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        const userData = await response.json();
        console.log(userData);

        const deliveryContainer = document.getElementById('delivery-container');
        deliveryContainer.innerHTML = '';

        userData.forEach((checkout) => {
            const checkoutHTML = `
                <div class="address-item py-3">
                    <div class="columns is-vcentered">
                        <div class="column">
                            <p><strong>${checkout.summaryTitle}</strong></p>
                            <p><strong>배송 상태: </strong></p> <p data-id="${checkout.id}"><strong>${checkout.deliveryStatus}</strong></p> 
                            <p>구매 금액 : ${checkout.totalPrice}</p>
                            <p>배송 주소 : (${checkout.address.postalCode}) ${checkout.address.address1}, ${checkout.address.address2}</p>
                        </div>
                        <div class="column is-narrow has-text-right">
                            <button class="button is-light is-small mb-2 edit-button" data-delivery-status="${checkout.deliveryStatus}" data-id="${checkout.id}">아이템 디테일 페이지 확인</button>
                            <button class="button is-danger is-small delete-button" data-delivery-status="${checkout.deliveryStatus}" data-id="${checkout.id}">삭제</button>
                        </div>
                    </div>
                    <hr /></div>`;

            deliveryContainer.innerHTML += checkoutHTML;
        });

        buttonMethod();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/users/login';
    }
}

//배송지 수정/삭제 버튼 동작
function buttonMethod() {
    //"user/mypage-checkout-detail"
    const editButtons = document.querySelectorAll('.edit-button');
    editButtons.forEach(button => {
        button.addEventListener('click', async function() {
            console.log(this.dataset.deliveryStatus);
            if(this.dataset.deliveryStatus==="READY"){
                const checkoutId = this.dataset.id;
                await openCheckOutDetail(checkoutId);
            }else{
                return alert("배송이 진행중입니다. 배송정보 수정이 불가능합니다.");
            }
        });
    });

    const deleteButtons = document.querySelectorAll('.delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', async function() {
            console.log(this.dataset.deliveryStatus);
            if(this.dataset.deliveryStatus==="READY"){
                const checkoutId = this.dataset.id;
                if(confirm("삭제하시겠습니까?")){
                    await deleteCheckOut(checkoutId);
                    alert("정상적으로 삭제되었습니다.");
                }else{
                    return alert("삭제 취소");
                }
            }else{
                return alert("배송이 진행중입니다. 배송정보 삭제가 불가능합니다.");
            }
        });
    });
}
// 디테일페이지 이동
async function openCheckOutDetail(checkoutId) {
    window.location.href = `/users/me/purchase/${checkoutId}`;
}


//주문 삭제
async function deleteCheckOut(checkoutId) {
    const token = sessionStorage.getItem('accessToken');
    try {
        let response = await fetch(`/api/checkout/${checkoutId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            console.error('Error:', response.status);
            if (response.status === 401) {
                console.log('토큰이 만료되었습니다. 새로운 토큰을 발급 받습니다.');
                await fetchNewAccessToken();
                return deleteAddress(checkoutId);
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        alert('삭제가 완료되었습니다.');
        fetchUserCheckOuts();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}