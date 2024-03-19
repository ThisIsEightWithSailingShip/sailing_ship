document.querySelectorAll('.store-card').forEach(function(card) {
    card.addEventListener('click', function() {
        let storeIdString = card.getAttribute("storeId");
        // 페이지 이동
        let storeId = parseInt(storeIdString);
        window.location.href = `/sail/store/${storeId}`;
    });
});
