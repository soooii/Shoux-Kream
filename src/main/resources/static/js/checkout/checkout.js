import * as Api from "../api.js";
import {
  checkLogin,
  addCommas,
  convertToNumber,
  navigate,
  randomPick,
  createNavbar,
} from "../useful-functions.js";


// 요소(element), input 혹은 상수
const subtitleCart = document.querySelector("#subtitleCart");
const receiverNameInput = document.querySelector("#receiverName");
const receiverPhoneNumberInput = document.querySelector("#receiverPhoneNumber");
const postalCodeInput = document.querySelector("#postalCode");
const searchAddressButton = document.querySelector("#searchAddressButton");
const address1Input = document.querySelector("#address1");
const address2Input = document.querySelector("#address2");
const requestSelectBox = document.querySelector("#requestSelectBox");
const customRequestContainer = document.querySelector(
  "#customRequestContainer"
);
const customRequestInput = document.querySelector("#customRequest");
const itemsTitleElem = document.querySelector("#itemsTitle");
const itemsTotalElem = document.querySelector("#itemsTotal");
const deliveryFeeElem = document.querySelector("#deliveryFee");
const checkOutTotalElem = document.querySelector("#checkOutTotal");
const checkOutButton = document.querySelector("#checkOutButton");

const requestOption = {
  1: "직접 수령하겠습니다.",
  2: "배송 전 연락바랍니다.",
  3: "부재 시 경비실에 맡겨주세요.",
  4: "부재 시 문 앞에 놓아주세요.",
  5: "부재 시 택배함에 넣어주세요.",
  6: "직접 입력",
};

checkLogin();
addAllElements();
addAllEvents();

// html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllElements() {
  createNavbar();
  insertCheckOutSummary();
  insertUserData();
}

// addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
function addAllEvents() {
  subtitleCart.addEventListener("click", navigate("/cart"));
  searchAddressButton.addEventListener("click", searchAddress);
  requestSelectBox.addEventListener("change", handleRequestChange);
  checkOutButton.addEventListener("click", doCheckOut);
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


//api/cart/summary > 

// TODO API로 교체 필요, 카트에서 구매하기 보낼때 selectedIds 정보도 받아야함(cartrepository.findbyslected(userid))
// 페이지 로드 시 실행되며, 결제정보에 값을 삽입함.
async function insertCheckOutSummary() {

  
  //ids 배열값 저장 > api에 들어가있는 principal로 userid 검증
  const ids = await Api.get("/api/cart/summary","");

  const selectedIds = await Api.get("/api/cart/selected",""); 

  // 구매할 아이템이 없다면 다른 페이지로 이동시킴
  const hasItemInCart = ids.length !== 0;
  const hasItemToCheckOut = selectedIds.length !== 0;

  // 카트 아이템 개수가 아예 0 이어서 false라면 => true category중에서 아무곳으로 보냄
  if (!hasItemInCart) {
    const categorys = await Api.get("/api/categories");
    const categoryTitle = randomPick(categorys).name;

    alert(`구매할 제품이 없습니다. 제품을 선택해 주세요.`);

    //TODO 카테고리의 아이템으로 보내주는 역할 구현필요
    return window.location.replace(`/item/list?category=${categoryTitle}`);
  }

  // 선택제품이 없다면 확인
  if (!hasItemToCheckOut) {
    alert("구매할 제품이 없습니다. 장바구니에서 선택해 주세요.");

    //principal 통해서 id값 가져오기
    return window.location.replace("/cart/summary");
  }

  // 화면에 보일 상품명
  let itemsTitle = "";
  let itemsTotal = 0;

  // 선택된 id의 list값에서 for문으로 id개별 삽입, get으로 단품조회 => (경로, params)
  // TODO cart 전체조회에서 받은 값 가지고 가공
  for (const id of selectedIds) {
    var title = id.itemName;
    var quantity = id.quantity;
    var price = id.itemPrice;
    // 첫 제품이 아니라면, 다음 줄에 출력되도록 \n을 추가함
    if (itemsTitle) {
      itemsTitle += "\n";
    }
    itemsTotal += quantity*price;
    itemsTitle += `${title} / ${quantity}개`;
  }

  itemsTitleElem.innerText = itemsTitle;
  itemsTotalElem.innerText = `${addCommas(itemsTotal)}원`;

  if (hasItemToCheckOut) {
    deliveryFeeElem.innerText = `3,000원`;
    checkOutTotalElem.innerText = `${addCommas(itemsTotal + 3000)}원`;
  } else {
    deliveryFeeElem.innerText = `0원`;
    checkOutTotalElem.innerText = `0원`;
  }

  receiverNameInput.focus();
}

//유저정보 받기
async function insertUserData() {
  const userData = await Api.get("/api/users/userAddress");

  //TODO 일단 0번 데이터만 등록
  const { id, recipientName, recipientPhone, postalCode, address1, address2 } = userData[0];

  // 만약 db에 데이터 값이 있었다면, 배송지정보에 삽입
  if (recipientName) {
    receiverNameInput.value = recipientName;
  }

  if (recipientPhone) {
    receiverPhoneNumberInput.value = recipientPhone;
  }

  if (address) {
    postalCode.value = postalCode;
    address1Input.value = address1;
    address2Input.value = address2;
  }
}

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

// 결제 진행
async function doCheckOut() {
  const recipientName = receiverNameInput.value;
  const recipientPhone = receiverPhoneNumberInput.value;
  const postalCode = postalCodeInput.value;
  const address1 = address1Input.value;
  const address2 = address2Input.value;
  const requestType = requestSelectBox.value;
  const customRequest = customRequestInput.value;
  const summaryTitle = itemsTitleElem.innerText;
  const totalPrice = convertToNumber(checkOutTotalElem.innerText);
  const selectedIds = await Api.get("/api/cart/selected",""); 

  if (!receiverName || !receiverPhoneNumber || !postalCode || !address2) {
    return alert("배송지 정보를 모두 입력해 주세요.");
  }

  // 요청사항의 종류에 따라 request 문구가 달라짐
  let request;
  if (requestType === "0") {
    request = "요청사항 없음.";
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
    const checkOutData = await Api.post("/api/checkout", {
      summaryTitle,
      totalPrice,
      address,
      //배송요청사항
      request,
    });

    const checkOutId = checkOutData.id;

    //TODO 위에서 한번에 받은 정보를 통해 정리
    // 제품별로 주문아이템을 등록함
    for (const id of selectedIds) {
      const itemId = id.itemId;
      console.log(`%c itemId 콘솔 ${itemId}`)
      var quantity = id.quantity;
      var price = id.itemPrice;
      const totalPrice = quantity * price;

      await Api.post("/api/checkout-item", {
        checkOutId,
        itemId,
        quantity,
        totalPrice,
      });

      const cartId = id.cartId;
      await Api.delete("/api/cart", cartId);

    }


    alert("결제 및 주문이 정상적으로 완료되었습니다.\n감사합니다.");
    window.location.href = "/checkout/complete";
  } catch (err) {
    console.log(err);
    alert(`결제 중 문제가 발생하였습니다: ${err.message}`);
  }
}
