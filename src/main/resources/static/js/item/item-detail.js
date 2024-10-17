// import {getImageUrl} from "/js/aws-s3.js";
// //import * as Api from "/js/api.js";
// import {
//     getUrlParams,
//     addCommas,
//     checkUrlParams,
//     createNavbar,
// } from "/js/useful-functions.js";
// //import { addToDb, putToDb } from "/js/indexed-db.js";
//
// // 요소(element), input 혹은 상수
// const itemImageTag = document.querySelector("#itemImageTag");
// const manufacturerTag = document.querySelector("#manufacturerTag");
// const titleTag = document.querySelector("#titleTag");
// const detailDescriptionTag = document.querySelector("#detailDescriptionTag");
// //const addToCartButton = document.querySelector("#addToCartButton");
// //const purchaseButton = document.querySelector("#purchaseButton");
//
// // 새로운 삭제 버튼 요소
// const deleteButton = document.createElement("button");
// deleteButton.id = "deleteButton";
// deleteButton.classList.add("button", "is-danger");
// deleteButton.innerText = "삭제";
//
// checkUrlParams("id");
// addAllElements();
// addAllEvents();
//
// // html에 요소를 추가하는 함수들을 묶어주어서 코드를 깔끔하게 하는 역할임.
// function addAllElements() {
//     createNavbar();
//     insertItemData();
// }
//
// // addEventListener들을 묶어주어서 코드를 깔끔하게 하는 역할임.
// function addAllEvents() {
// }
//
// async function insertItemData() {
//     const {id} = getUrlParams();
//     const item = await Api.get(`/item/${id}`);
//
//     // 객체 destructuring
//     const {
//         title,
//         detailDescription,
//         menufacturer,
//         imageKey,
//         isRecommended,
//         price,
//     } = item;
//     const imageUrl = await getImageUrl(imageKey);
//
//     itemImageTag.src = imageUrl;
//     titleTag.innerText = title;
//     detailDescriptionTag.innerText = detailDescription;
//     manufacturerTag.innerText = menufacturer;
//     priceTag.innerText = `${addCommas(price)}원`;
//
//     if (isRecommended) {
//         titleTag.insertAdjacentHTML(
//             "beforeend",
//             '<span class="tag is-success is-rounded">추천</span>'
//         );
//     }
//
//     const deleteButton = document.getElementById("deleteButton");
//
//     if (deleteButton) {
//         deleteButton.addEventListener("click", () => {
//             const {id} = getUrlParams(); // 현재 아이템의 ID 가져오기
//             deleteItem(id);
//         });
//     }
//
//     // addToCartButton.addEventListener("click", async () => {
//     //   try {
//     //     await insertDb(item);
//     //
//     //     alert("장바구니에 추가되었습니다.");
//     //   } catch (err) {
//     //     // Key already exists 에러면 아래와 같이 alert함
//     //     if (err.message.includes("Key")) {
//     //       alert("이미 장바구니에 추가되어 있습니다.");
//     //     }
//     //
//     //     console.log(err);
//     //   }
//     // });
//
//     // purchaseButton.addEventListener("click", async () => {
//     //     try {
//     //         await insertDb(item);
//     //
//     //         window.location.href = "/order";
//     //     } catch (err) {
//     //         console.log(err);
//     //
//     //         //insertDb가 에러가 되는 경우는 이미 제품이 장바구니에 있던 경우임
//     //         //따라서 다시 추가 안 하고 바로 order 페이지로 이동함
//     //         window.location.href = "/order";
//     //     }
//     // });
// }
//
// async function insertDb(item) {
//     // 객체 destructuring
//     const {id: id, price} = item;
//
//     // 장바구니 추가 시, indexedDB에 제품 데이터 및
//     // 주문수량 (기본값 1)을 저장함.
//     await addToDb("cart", {...item, quantity: 1}, id);
//
//     // 장바구니 요약(=전체 총합)을 업데이트함.
//     await putToDb("order", "summary", (data) => {
//         // 기존 데이터를 가져옴
//         const count = data.itemsCount;
//         const total = data.itemsTotal;
//         const ids = data.ids;
//         const selectedIds = data.selectedIds;
//
//         // 기존 데이터가 있다면 1을 추가하고, 없다면 초기값 1을 줌
//         data.itemsCount = count ? count + 1 : 1;
//
//         // 기존 데이터가 있다면 가격만큼 추가하고, 없다면 초기값으로 해당 가격을 줌
//         data.itemsTotal = total ? total + price : price;
//
//         // 기존 데이터(배열)가 있다면 id만 추가하고, 없다면 배열 새로 만듦
//         data.ids = ids ? [...ids, id] : [id];
//
//         // 위와 마찬가지 방식
//         data.selectedIds = selectedIds ? [...selectedIds, id] : [id];
//     });
// }
//
// function deleteItem() {
//     const itemId = /*[[${item.id}]]*/ '';  // Thymeleaf 또는 JS로 itemId 설정
//
//     if (confirm('정말로 이 상품을 삭제하시겠습니까?')) {
//         fetch(`/item/${itemId}`, {  // 정확한 삭제 경로로 요청 전송
//             method: 'DELETE',
//             headers: {
//                 'Content-Type': 'application/json'
//             }
//         }).then(response => {
//             if (response.ok) {
//                 alert('상품이 삭제되었습니다.');
//                 window.location.href = '/';  // 삭제 후 메인 페이지로 이동
//             } else {
//                 alert('상품 삭제에 실패했습니다.');
//             }
//         }).catch(error => {
//             console.error('Error:', error);
//             alert('상품 삭제 중 오류가 발생했습니다.');
//         });
//     }
// }

// 장바구니 담기
document.addEventListener('DOMContentLoaded', function () {

    const addCartButton = document.getElementById('add-cart-btn');

    const decreaseBtn = document.getElementById('decreaseBtn');
    const increaseBtn = document.getElementById('increaseBtn');

    decreaseBtn.addEventListener('click', () => {
        const quantityInput = document.getElementById('quantity');
        let currentQuantity = parseInt(quantityInput.value);
        if (currentQuantity > 1) {
            quantityInput.value = currentQuantity - 1;
        }
    });

    increaseBtn.addEventListener('click', () => {
        const quantityInput = document.getElementById('quantity');
        let currentQuantity = parseInt(quantityInput.value);
        quantityInput.value = currentQuantity + 1;
    });

    if (addCartButton) {
        addCartButton.addEventListener('click', event => {
            let itemId = document.getElementById('item-id').value;
            const quantity = parseInt(document.getElementById('quantity').value);
            const token = sessionStorage.getItem('accessToken');
            if (token === null) {
                window.location.href = `/users/login`;
            }
            fetch(`/api/cart/add/${itemId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({quantity: quantity})
            })
                .then((response) => {
                    if (response.ok) {
                        alert('장바구니에 상품이 추가되었습니다.');
                        location.reload(); // 페이지 새로 고침하여 변경사항 반영
                    } else {
                        alert('장바구니에 상품 추가가 실패했습니다. 다시 시도해 주세요.');
                    }
                })
                .catch((error) => {
                    console.error('상품 추가 중 오류 발생:', error);
                    alert('상품 추가 중 오류가 발생했습니다.');
                });
        });
    }
});