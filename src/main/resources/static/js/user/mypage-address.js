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
    const editSearchAddressButton = document.getElementById('editSearchAddressButton');

    const editSaveButton = document.getElementById('editSave');
    const editCloseButton = document.getElementById('editClose');

    //우편번호 찾기
    searchAddressButton.addEventListener('click', searchAddress);
    editSearchAddressButton.addEventListener('click', searchAddress);

    // 배송지 입력 모달 열기
    addAddressButton.addEventListener('click', function() {
        document.getElementById('addressForm').reset();
        addressModal.classList.add('is-active');
    });

    // 저장하기
    saveButton.addEventListener('click', async function() {
        await saveAddress();
    });

    // 취소 누르면 닫힘
    closeButton.addEventListener('click', function() {
        addressModal.classList.remove('is-active');
    });

    // 수정하기 버튼
    editSaveButton.addEventListener('click', function() {
        updateAddress(currentAddressId);
    });

    //수정하기 모달 닫기
    editCloseButton.addEventListener('click', function(){
        const editModal = document.getElementById('editModal');
        editModal.classList.remove('is-active');
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
        addressContainer.innerHTML = '';

        userData.forEach((address) => {
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
                    <hr /></div>`;

            addressContainer.innerHTML += addressHTML;
        });

        buttonMethod();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/users/login';
    }
}

//배송지 수정/삭제 버튼 동작
function buttonMethod() {
    const editButtons = document.querySelectorAll('.edit-button');
    editButtons.forEach(button => {
        button.addEventListener('click', async function() {
            const addressId = this.dataset.id;
            const addressData = await loadAddressForEdit(addressId);
            openEditModal(addressData);
        });
    });

    const deleteButtons = document.querySelectorAll('.delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', async function() {
            const addressId = this.dataset.id;
            await deleteAddress(addressId);
        });
    });
}

// 특정 배송지 정보 가져오기
async function loadAddressForEdit(addressId) {
    const token = sessionStorage.getItem('accessToken');

    try {
        let response = await fetch(`/api/users/userAddress/${addressId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
                await fetchNewAccessToken();
                return loadAddressForEdit(addressId);
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        const addressData = await response.json();
        return addressData;

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}

// 배송지 수정
async function updateAddress(addressId) {
    const token = sessionStorage.getItem('accessToken');
    const recipientName = document.getElementById('editNameInput').value;
    const recipientPhone = document.getElementById('editPhoneInput').value;
    const postalCode = document.getElementById('editPostalCodeInput').value;
    const address1 = document.getElementById('editAddress1Input').value;
    const address2 = document.getElementById('editAddress2Input').value;

    try {
        let response = await fetch(`/api/users/userAddress/${addressId}`, {
            method: 'PATCH',
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
                await fetchNewAccessToken();
                return updateAddress(addressId);
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        alert('주소가 수정되었습니다.');
        const editModal = document.getElementById('editModal');
        editModal.classList.remove('is-active');
        fetchUserAddress();

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
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
                await fetchNewAccessToken();
                return deleteAddress(addressId);
            }
            throw new Error(`HTTP error status: ${response.status}`);
        }

        alert('삭제가 완료되었습니다.');
        fetchUserAddress();

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

let currentAddressId = null; // 수정할 주소의 ID 저장

// 배송지 수정 모달 열기
function openEditModal(addressData) {
    const editForm = document.getElementById('editForm');
    editForm.reset();

    // 기존 주소 정보로 폼 채우기
    document.getElementById('editNameInput').value = addressData.recipientName;
    document.getElementById('editPhoneInput').value = addressData.recipientPhone;
    document.getElementById('editPostalCodeInput').value = addressData.postalCode;
    document.getElementById('editAddress1Input').value = addressData.address1;
    document.getElementById('editAddress2Input').value = addressData.address2;

    currentAddressId = addressData.id;
    const editModal = document.getElementById('editModal');
    editModal.classList.add('is-active');
}

// Daum 주소 API
function searchAddress(e) {
    e.preventDefault();

    // 버튼에 따라 postalCodeInput과 address1Input을 다르게 참조
    const isEdit = e.target.id === 'editSearchAddressButton';
    const postalCodeInput = isEdit ? document.getElementById('editPostalCodeInput') : document.getElementById('postalCodeInput');
    const address1Input = isEdit ? document.getElementById('editAddress1Input') : document.getElementById('address1Input');
    const address2Input = isEdit ? document.getElementById('editAddress2Input') : document.getElementById('address2Input');

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

export {fetchUserAddress, buttonMethod, loadAddressForEdit, updateAddress, deleteAddress, saveAddress, openEditModal, searchAddress};
