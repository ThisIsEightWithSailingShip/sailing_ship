// 토큰이 만료되었는지 확인하는 함수
function isTokenExpired(token) {
    if (!token) return true;

    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Math.floor(Date.now() / 1000);
    return now >= payload.exp;
}

// 토큰을 재발급하는 함수
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
                    throw new Error('엑세스 토큰 갱신 에러');
                }
            } else {
                throw new Error('토큰 재발급 에러');
            }
        })
        .catch(error => {
            console.error("토큰 재발급 과정 에러:", error);
            return false;
        });
}

// 토큰을 확인하고 필요시 재발급하는 함수
function checkTokenExpirationAndHandle() {
    if (isTokenExpired(localStorage.getItem("accessToken"))) {
        reissueToken().then(success => {
            if (!success) {
                window.location.href = "/login";
            }
        });
    }
}

export { checkTokenExpirationAndHandle };
