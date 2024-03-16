
document.querySelectorAll('.store-card').forEach(function(card) {
    let storeId = card.querySelector('.store-id')

    console.log("클릭한 아이디 : " + storeId) // 추출을 못하넹...
});

function fetchStoreDetail(storeId) {
    fetch(`/api/store/${storeId}`)
        .then(response => response.json())
        .then(data => {
            // 서버로부터 받은 매장 상세 정보 처리
            console.log(data);
        })
        .catch(error => console.error('Error fetching store detail:', error));
}