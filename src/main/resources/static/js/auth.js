// auth.js
document.addEventListener("DOMContentLoaded", function() {
    checkTokenExpirationAndHandle();
});

function checkTokenExpirationAndHandle() {
    if (isTokenExpired()) {
        reissueToken().then(success => {
            if (!success) {
                window.location.href = "/login";
            }
        });
    }
}

function isTokenExpired() {
    const token = localStorage.getItem("accessToken");
    if (!token) return true; // 토큰이 없으면 만료로 간주

    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Math.floor(Date.now() / 1000); // 현재 시간을 초 단위로 변환
    return now >= payload.exp; // 토큰의 만료 시간과 현재 시간을 비교
}

function reissueToken() {
    return fetch("/reissue", {
        method: "POST",
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                const newAccessToken = response.headers.get("access");
                if (newAccessToken) {
                    localStorage.setItem("accessToken", newAccessToken);
                    return true;
                } else {
                    throw new Error('토큰 재발급을 성공했으나 새 엑세스 토큰을 받지 못함');
                }
            } else {
                throw new Error('토큰 재발급 실패');
            }
        })
        .catch(error => {
            console.error("토큰 재발급 중 오류 발생:", error);
            return false;
        });
}
