document.addEventListener('DOMContentLoaded', function () {
    const priceElements = document.querySelectorAll('.item-price-text span');
    priceElements.forEach(priceElement => {
        const rawPrice = priceElement.textContent.replace(/,/g, '');
        const formattedPrice = Number(rawPrice).toLocaleString();
        priceElement.textContent = `${formattedPrice}원`;
    });
});