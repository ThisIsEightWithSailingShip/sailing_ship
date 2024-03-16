// document.querySelectorAll('.store-card').forEach(function(card) {
//     card.addEventListener('click', function() {
//         let storeIdString = card.getAttribute("storeId"); // 데이터 속성으로부터 storeId를 읽어옴
//         // console.log(storeId)
//         let storeId = parseInt(storeIdString);
//         console.log(storeId)
//         fetchStoreDetail(storeId); // 추출한 storeId를 사용하여 상세 정보를 가져옴
//     });
// });
//
// function fetchStoreDetail(storeId) {
//     fetch(`/sail/store/${storeId}`)
//         .then(response => response.json())
//         .then(data => {
//             console.log(data);
//         })
//         .catch(error => console.error('Error fetching store detail:', error));
// }
