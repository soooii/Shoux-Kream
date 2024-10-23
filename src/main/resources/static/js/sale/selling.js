import {checkLogin, fetchNewAccessToken} from "/js/useful-functions.js";

checkLogin();

document.addEventListener('DOMContentLoaded', function () {
    getSaleList();
});

// 판매 내역
async function getSaleList() {
    const token = sessionStorage.getItem('accessToken');
    console.log('Access Token:', token);
    try {
        let response = await fetch('/api/sale', {
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

        const saleData = await response.json();

        // saleId 기준으로 내림차순 정렬
        saleData.sort((a, b) => b.saleId - a.saleId);

        const saleContainer = document.getElementById('sale-container');
        saleContainer.innerHTML = '';

        saleData.forEach((sale, index) => {
            // due_date를 yyyy/mm/dd 형식으로 변환
            const dueDate = new Date(sale.dueDate);
            const formattedDueDate = `${dueDate.getFullYear()}/${String(dueDate.getMonth() + 1).padStart(2, '0')}/${String(dueDate.getDate()).padStart(2, '0')}`;

            // 가격을 쉼표가 있는 형식으로 변환
            const formattedSellingPrice = sale.sellingPrice.toLocaleString();

            const saleHTML = `
            <div>
                <a href="/sale/${sale.saleId}" style="text-decoration: none; color: inherit;"> <!-- 링크 추가 -->
                <div class="columns is-mobile is-vcentered">
                    <div class="column is-narrow">
                        <img src="${sale.imageUrl}" class="image" alt="상품 이미지" />
                    </div>
                    <div class="column is-three-fifths">
                        <p><strong>${sale.itemName}</strong></p>
                        <p>${sale.shortDescription}</p>
                        <p>${sale.detailDescription}</p>
                    </div>
                    <div class="column is-narrow has-text-right">
                        <div class="price-due">
                            <div class="price">
                                <p>${formattedSellingPrice}원</p>
                            </div>
                            <div class="due">
                                <p>${formattedDueDate}</p>
                            </div>
                        </div>
                    </div>
                </div>
                ${index === saleData.length - 1 ? '' : '<hr />'} <!-- 마지막 데이터 체크 -->
            </div>

            `;
            saleContainer.innerHTML += saleHTML;
        });

    } catch (error) {
        alert(`오류가 발생했습니다: ${error.message}`);
        window.location.href = '/';
    }
}