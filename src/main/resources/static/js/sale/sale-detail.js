import {checkLogin, fetchNewAccessToken} from "/js/useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function () {
    getSaleDetail();
});

let saleIdCheck;

function getSaleIdFromUrl() {
    const path = window.location.pathname; // 현재 URL의 경로를 가져옴
    const saleId = path.split('/').pop(); // 마지막 부분을 가져옴
    return saleId;
}

// 판매 내역
async function getSaleDetail() {
    const token = sessionStorage.getItem('accessToken');
    const saleId = getSaleIdFromUrl(); // saleId 가져오기

    try {
        let response = await fetch(`/api/sale/${saleId}`, {
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

        const sale = await response.json();
        saleIdCheck = sale.saleId;

        // 형식 지정
        // due_date를 yyyy/mm/dd 형식으로 변환
        const dueDate = new Date(sale.dueDate);
        const formattedDueDate = `${dueDate.getFullYear()}/${String(dueDate.getMonth() + 1).padStart(2, '0')}/${String(dueDate.getDate()).padStart(2, '0')}`;

        // 입찰일을 yyyy/mm/dd 형식으로 변환
        const createdAt = new Date(sale.createdAt);
        const formattedCreatedAt = `${createdAt.getFullYear()}/${String(createdAt.getMonth() + 1).padStart(2, '0')}/${String(createdAt.getDate()).padStart(2, '0')}`;

        // 가격을 쉼표가 있는 형식으로 변환
        const formattedItemPrice = sale.itemPrice.toLocaleString();
        const formattedSellingPrice = sale.sellingPrice.toLocaleString();
        const formattedFee = sale.fee.toLocaleString();
        const formattedSettlement = (sale.sellingPrice - sale.fee).toLocaleString();

        // 내용 입력
        const saleDeatilContainer = document.getElementById('sale-detail-container');
        saleDeatilContainer.innerHTML = '';

        const saleHTML = `
            <!--상품 설명-->
            <div class="sell-item">
                <img src="${sale.imageUrl}" class="image" alt="상품 이미지">
                <div class="text-container">
                    <p class="bold-text">${sale.itemName}</p>
                    <p class="normal-black">${sale.shortDescription}</p>
                    <p class="normal-gray">${sale.detailDescription}</p>
                </div>
            </div>
            <hr />
            <div class="has-text-centered">
                <p class="normal-gray">즉시 구매가</p>
                <p class="item-price">${formattedItemPrice}원</p>
            </div>
            <br />
            <!--상품 바로가기-->
            <div class="has-text-centered">
                <a href="/item/item-detail/${sale.itemId}" class="button is-fullwidth">상품 상세</a>
            </div>
            <br />
            <br />
            <!--판매 입찰 내역-->
            <div>
                <p class="sale-title">판매 입찰 내역</p>
                <hr class="has-background-black my-2" />
            </div>
            <div class="custom-background">
                <div class="columns ml-2 is-vcentered">
                    <p class="column is-one-fifth"><strong>판매 희망가</strong></p>
                    <span class="column price-text">${formattedSellingPrice}원</span>
                </div>
            </div>
            <br />
            <dl>
                <div>
                    <dt class="left-info">검수비</dt>
                    <dd class="inspection">무료</dd>
                </div>
                <div>
                    <dt class="left-info">수수료</dt>
                    <dd class="fee">${formattedFee}원</dd>
                </div>
                <div>
                    <dt class="left-info">배송비</dt>
                    <dd>선불 · 판매자 부담</dd>
                </div>
                <div>
                    <dt class="left-info-hope">정산금액</dt>
                    <dd class="bold-text">${formattedSettlement}원</dd>
                </div>
                <hr />
                <div>
                    <dt class="left-info-hope" id="due-date">입찰일</dt>
                    <dd>${formattedCreatedAt}</dd>
                </div>
                <div>
                    <dt class="left-info-hope">입찰 마감기한</dt>
                    <dd>${sale.daysToAdd}일 (${formattedDueDate}까지)</dd>
                </div>
            </dl>
            <br />
            <div>
                <a href="/selling/${sale.saleId}" class="button is-black is-medium is-fullwidth">입찰 변경하기</a>
            </div>
            <br />
            <br />
            <br />
            <div class="has-text-centered">
                <a href="/selling" class="button is-normal">목록으로</a>
            </div>
            <br />
        `;

        saleDeatilContainer.innerHTML += saleHTML;

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/';
    }
}

// 입찰 지우기
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        // 사용자에게 삭제 확인 메시지 표시
        const confirmDelete = confirm('판매를 취소하시겠습니까?');

        if (confirmDelete) {
            const token = sessionStorage.getItem('accessToken');
            const saleId = saleIdCheck;

            fetch(`/api/sale/${saleId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(() => {
                    alert('판매 등록 삭제가 완료되었습니다.');
                    location.replace(`/selling`);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
    });
}