import * as Api from "../api.js";
import { checkLogin, addCommas, fetchNewAccessToken } from "../useful-functions.js";
import { loadAddressForEdit, searchAddress as modalSearchAddress } from "./mypage-address.js";

//checkoutId을 뽑아오는 작업
const checkoutId = window.location.pathname.split("/").pop();

const itemsTotalElem = document.querySelector("#itemsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const checkOutTotalElem = document.querySelector("#checkOutTotal");

//TODO 이 부분 모듈화 필요
//배송지 확인 버튼
const checkAddressButton = document.querySelector("#checkAddress");
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
const listCloseButton = document.getElementById('listClose');
let currentAddressId = null; // 수정할 주소의 ID 저장

const receiverNameInput = document.querySelector("#receiverName");
const receiverPhoneNumberInput = document.querySelector("#receiverPhoneNumber");
const postalCodeInput = document.querySelector("#postalCode");
const mainSearchAddressButton = document.querySelector("#mainSearchAddressButton");
const address1Input = document.querySelector("#address1");
const address2Input = document.querySelector("#address2");
const requestSelectBox = document.querySelector("#requestSelectBox");
const customRequestContainer = document.querySelector(
  "#customRequestContainer"
);
const customRequestInput = document.querySelector("#customRequest");

const editButton = document.querySelector("#editCheckOut");
const cancelButton = document.querySelector("#cancel");
const deleteButton = document.querySelector("#deleteCheckOut");

const requestOption = {
    1: "직접 수령하겠습니다.",
    2: "배송 전 연락바랍니다.",
    3: "부재 시 경비실에 맡겨주세요.",
    4: "부재 시 문 앞에 놓아주세요.",
    5: "부재 시 택배함에 넣어주세요.",
    6: "직접 입력",
  };


checkLogin();
addAllEvents();


  // addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
    // 배송지 입력 모달 열기
    checkAddressButton.addEventListener('click', function() {
        addressListModal.classList.add('is-active');
        checkAddress();
    });
    // 취소 누르면 닫힘
    listCloseButton.addEventListener('click', function() {
        addressListModal.classList.remove('is-active');
    });
    mainSearchAddressButton.addEventListener("click", searchAddress);
    requestSelectBox.addEventListener("change", handleRequestChange);
    mainSearchAddressButton.addEventListener("click", searchAddress);

    checkOutDetailButtonMethod();
}

document.addEventListener('DOMContentLoaded', function() {
    fetchUserCheckOut();
});

// "직접 입력" 선택 시 input칸 보이게 함
// default값(배송 시 요청사항을 선택해 주세여) 이외를 선택 시 글자가 진해지도록 함
function handleRequestChange(e) {
    const type = e.target.value;
  
    if (type === "6") {
      customRequestContainer.style.display = "flex";
      customRequestInput.focus();
    } else {
      customRequestContainer.style.display = "none";
    }
  
    if (type === "0") {
      requestSelectBox.style.color = "rgba(0, 0, 0, 0.3)";
    } else {
      requestSelectBox.style.color = "rgba(0, 0, 0, 1)";
    }
  }
  

// Daum 주소 API (사용 설명 https://postcode.map.daum.net/guide)
function searchAddress() {
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

// 주문정보 가져오기
async function fetchUserCheckOut() {
    
    const token = sessionStorage.getItem('accessToken');
    console.log('Access Token:', token);
    try {
        const checkOutItems = await fetch(`/api/checkout-item/${checkoutId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            // [{"checkOutId":4,"itemId":28,"quantity":1,"totalPrice":50}]
            return response.json();
        })

        const detailContainer = document.getElementById('checkout-detail-container');
        detailContainer.innerHTML = '';
        console.log(checkOutItems);

        // 화면에 보일 총 가격
        let itemsTotal = 0;
        checkOutItems.forEach((checkOutItem) => {
            fetch(`/item/${checkOutItem.itemId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
                return response.json();
            })
            .then(item => {
                console.log(item); // 이제 여기서 item을 사용할 수 있습니다.
        
                const itemImage = document.createElement('img');
                const itemName = document.createElement('p');
                const quantity = document.createElement('span');
                const price = document.createElement('p');
        
                const ItemDiv = document.createElement('div');
                ItemDiv.classList.add('cart-product-item');
        
                itemImage.classList.add('image');
                itemName.classList.add('content');
                quantity.classList.add('content');
                price.classList.add('content');
        
                itemImage.src = `${item.imageKey}`;
                itemName.innerText = `${item.title}`;
                quantity.innerText = `${checkOutItem.quantity}개`;
                price.innerText = '\\' + `${checkOutItem.totalPrice.toLocaleString()}`;
        
                // 링크 추가: 이미지와 상품명, 수량, 가격을 포함
                const itemLink = document.createElement('a');
                itemLink.href = `/item/item-detail/${item.id}`; // itemId 대신 item.id 사용
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
                ItemDiv.appendChild(itemLink); // itemLink를 cartItemDiv에 추가
                detailContainer.appendChild(ItemDiv); // 컨테이너에 추가

                itemsTotal += checkOutItem.totalPrice;
                itemsTotalElem.innerText = `${addCommas(itemsTotal)}원`;
                deliveryFeeElem.innerText = `3,000원`;
                checkOutTotalElem.innerText = `${addCommas(itemsTotal + 3000)}원`;
            });
        });
    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
    // 배송지 정보 추가
    try {
        const checkOut = await fetch(`/api/checkout/${checkoutId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            // [{"checkOutId":4,"itemId":28,"quantity":1,"totalPrice":50}]
            return response.json();
        })
        const addressData = checkOut.address;
        receiverNameInput.value = addressData.recipientName;
        receiverPhoneNumberInput.value = addressData.recipientPhone;
        postalCode.value = addressData.postalCode;
        address1Input.value = addressData.address1;
        address2Input.value = addressData.address2;
        
        let requestText = checkOut.request;
        const options = Array.from(requestSelectBox.options);
        console.log(requestText);
        console.log(options);
        let value = null;
        options.forEach(option => {
            if (option.label === requestText) {
                value = option.value;
            }
        });
        if(1<= value && value <=5){
            requestSelectBox.value = value;
            customRequestContainer.style.display = "none";
        }else{
            requestSelectBox.value = "6";
            customRequestInput.value = requestText;
        }
    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}

function checkAddress() {
    fetchUserAddress();
    //우편번호 찾기
    searchAddressButton.addEventListener('click', modalSearchAddress);
    editSearchAddressButton.addEventListener('click', modalSearchAddress);
  
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
    receiverNameInput.value = addressData.recipientName;
    receiverPhoneNumberInput.value = addressData.recipientPhone;
    postalCode.value = addressData.postalCode;
    address1Input.value = addressData.address1;
    address2Input.value = addressData.address2;
  
  };
  
  // 배송지 목록 가져오기 오버라이딩 방법 몰라서 재정의
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
                            <button class="button is-light is-small mb-2 select-button" data-id="${address.id}">선택</button>
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
  
  //배송지 선택/삭제 버튼 동작
  function buttonMethod() {
    const editButtons = document.querySelectorAll('.select-button');
    editButtons.forEach(button => {
        button.addEventListener('click', async function() {
            const addressId = this.dataset.id;
            const addressData = await loadAddressForEdit(addressId);
            await insertUserData(addressData);
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

  //주문 상세페이지 수정/취소/삭제 버튼 동작
function checkOutDetailButtonMethod() {
    //"user/mypage-checkout-detail"
    editButton.addEventListener('click', async function() {
        if(confirm("수정하시겠습니까?")){
            await editDelivery(checkoutId);
            alert("정상적으로 제출되었습니다.");
        }
    });
    cancelButton.addEventListener('click', async function() {
        window.location.href="/users/me/purchase";
    });
    deleteButton.addEventListener('click', async function() {
        if(confirm("삭제하시겠습니까?")){
            await deleteCheckOut(checkoutId);
            alert("정상적으로 삭제되었습니다.");
        }else{
            return alert("삭제 취소");
        }
    });
}
  
  //배송지 확인에서 유저정보 받고 팝업 닫기
  async function insertUserData(addressData) {
      receiverNameInput.value = addressData.recipientName;
      receiverPhoneNumberInput.value = addressData.recipientPhone;
      postalCode.value = addressData.postalCode;
      address1Input.value = addressData.address1;
      address2Input.value = addressData.address2;
  
      currentAddressId = addressData.id;
      addressListModal.classList.remove('is-active');
  }
  


//주문 배송지 수정하기
async function editDelivery(checkoutId) {
    const recipientName = receiverNameInput.value;
    const recipientPhone = receiverPhoneNumberInput.value;
    const postalCode = postalCodeInput.value;
    const address1 = address1Input.value;
    const address2 = address2Input.value;
    const requestType = requestSelectBox.value;
    const customRequest = customRequestInput.value;
  
    if (!receiverName || !receiverPhoneNumber || !postalCode || !address2) {
      return alert("배송지 정보를 모두 입력해 주세요.");
    }
  
    // 요청사항의 종류에 따라 request 문구가 달라짐
    let request;
    if (requestType === "0") {
      request = "요청사항 없음.";
      return alert("요청사항을 선택하거나 작성해 주세요.");
    } else if (requestType === "6") {
      if (!customRequest) {
        return alert("요청사항을 작성해 주세요.");
      }
      request = customRequest;
    } else {
      request = requestOption[requestType];
    }
  
    // TODO 백엔드에서 이 address 주소 최근주소지 등록 await Api.post("/api/user/recentdelivery", data); 대체
    const address = {
      postalCode,
      address1,
      address2,
      recipientName,
      recipientPhone,
    };
  
    try {
      // 전체 주문을 등록함
      // 응답값에 id 포함되어야함
      const checkOutData = await Api.patch("/api/checkout",checkoutId, {
        address,
        //배송요청사항
        request,
      });
      
      alert("주문정보 배송지 수정이 정상적으로 완료되었습니다.\n감사합니다.");
      window.location.href="/users/me/purchase";
    } catch (err) {
      console.log(err);
      alert(`수정 중 문제가 발생하였습니다: ${err.message}`);
    }
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
        window.location.href="/users/me/purchase";

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
    }
}