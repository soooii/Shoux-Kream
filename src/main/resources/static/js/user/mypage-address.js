import { checkLogin, fetchNewAccessToken } from "/js/useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function() {
    fetchUserAddress();

    //배송지 추가 버튼
    const addAddressButton = document.getElementById('addAddress');

    //저장하기 버튼
    const saveButton = document.getElementById('save');

    //취소 버튼
    const closeButton = document.getElementById('close');

    //배송지 입력 모달
    const addressModal = document.getElementById('addressModal');

    //우편번호 찾기
    const searchAddressButton = document.getElementById('searchAddressButton');

    searchAddressButton.addEventListener('click', searchAddress);


    // 배송지 입력 모달 열기
    addAddressButton.addEventListener('click', function() {
        addressModal.classList.add('is-active');
    });

    // 저장하기
    saveButton.addEventListener('click', async function() {
        await saveAddress(); // 주소 저장 함수 호출
    });

    // 취소 누르면 닫힘
    closeButton.addEventListener('click', function() {
        addressModal.classList.remove('is-active');
    });

});

// 배송지 목록 가져오기
async function fetchUserAddress() {
    const token = sessionStorage.getItem('accessToken');
    console.log('Access Token:', token);
    try {
        let response = await fetch('/api/users/userAddress', {
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

        const addressContainer = document.getElementById('address-container');
        addressContainer.innerHTML = ''; // 기존 내용 초기화

        // 미리 만들어놓은 구조에 데이터 삽입
        userData.forEach((address) => {
            // 주소 정보 HTML 구조
            const addressHTML = `
                <div class="address-item py-3">
                    <div class="columns is-vcentered">
                        <div class="column">
                            <p><strong>${address.recipientName}</strong></p>
                            <p>${address.recipientPhone}</p>
                            <p>(${address.postalCode}) ${address.address1}, ${address.address2}</p>
                        </div>
                        <div class="column is-narrow has-text-right">
                            <button class="button is-light is-small mb-2 edit-button" data-id="${address.id}">수정</button>
                            <button class="button is-light is-small delete-button" data-id="${address.id}">삭제</button>
                        </div>
                    </div>
                    <hr /> 
                </div>
            `;

            // 생성한 HTML을 addressContainer에 추가
            addressContainer.innerHTML += addressHTML;
        });

        buttonMethod();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/users/login';
    }
}

//배송지 수정/삭제
function buttonMethod() {
    const editButtons = document.querySelectorAll('.edit-button');
    editButtons.forEach(button => {
        button.addEventListener('click', async function() {
            const addressId = this.dataset.id; // data-id 값 가져오기
            await loadAddressForEdit(addressId); // 주소 수정 함수 호출
            addressModal.classList.add('is-active'); // 모달 열기
        });
    });

    const deleteButtons = document.querySelectorAll('.delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', async function() {
            const addressId = this.dataset.id; // data-id 값 가져오기
            await deleteAddress(addressId); // 주소 삭제 함수 호출
        });
    });
}

//배송지 삭제
async function deleteAddress(addressId) {
    const token = sessionStorage.getItem('accessToken');

    try {
        let response = await fetch(`/api/users/userAddress/${addressId}`, {
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
                return deleteAddress(addressId);
            }
            else if(response.status === 419){
                console.log('해당 주소를 사용하는 주문이 있어 삭제할 수 없습니다.');
                alert('해당 주소를 사용하는 주문이 있어 삭제할 수 없습니다.');
                return;
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        alert('삭제가 완료되었습니다.');
        fetchUserAddress(); // 주소 목록 다시 가져오기

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}

// 새로운 배송지 저장하기
async function saveAddress() {
    const token = sessionStorage.getItem('accessToken');
    const recipientName = document.querySelector('#addressModal input[placeholder="수령인을 입력하세요"]').value;
    const recipientPhone = document.querySelector('#addressModal input[placeholder="휴대폰 번호를 입력하세요"]').value;
    const postalCode = document.getElementById('postalCodeInput').value;
    const address1 = document.getElementById('address1Input').value;
    const address2 = document.getElementById('address2Input').value;

    try {
        let response = await fetch('/api/users/userAddress', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                recipientName: recipientName,
                recipientPhone: recipientPhone,
                postalCode: postalCode,
                address1: address1,
                address2: address2
            })
        });

        if (!response.ok) {
            console.error('Error:', response.status);
            if (response.status === 401) {
                console.log('토큰이 만료되었습니다. 새로운 토큰을 발급 받습니다.');
                await fetchNewAccessToken();
                return saveAddress();
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        addressModal.classList.remove('is-active');
        fetchUserAddress();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}

// Daum 주소 API (사용 설명 https://postcode.map.daum.net/guide)
function searchAddress(e) {
    e.preventDefault();

    new daum.Postcode({
        oncomplete: function (data) {
            let addr = "";
            let extraAddr = "";

            if (data.userSelectedType === "R") {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === "R") {
                if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== "" && data.apartment === "Y") {
                    extraAddr +=
                        extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
                }
                if (extraAddr !== "") {
                    extraAddr = " (" + extraAddr + ")";
                }
            } else {
            }

            postalCodeInput.value = data.zonecode;
            address1Input.value = `${addr} ${extraAddr}`;
            address2Input.placeholder = "상세 주소를 입력해 주세요.";
            address2Input.focus();
        },
    }).open();
}


