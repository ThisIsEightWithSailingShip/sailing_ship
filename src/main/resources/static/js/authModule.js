document.addEventListener('DOMContentLoaded', function () {
    // 페이지 로딩 시 회원 인증 정보 가져오기
    fetch('/sail/authInfo', {
        method: 'GET',
        credentials: 'same-origin' // 쿠키를 자동으로 함께 보내도록 설정
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // 회원 인증 정보에 따라 헤더 업데이트
            updateHeader(data);
        })
        .catch(error => {
            console.error('Error fetching authInfo:', error);
            // 에러 발생 시 로그아웃 처리
            logout();
        });
});

function updateHeader(authInfo) {
    console.log(authInfo)

    const email = authInfo.email;
    const isOwner = authInfo.owner;

    console.log("사장님 여부 : " + isOwner)

    // 로그인 상태에 따라 헤더 업데이트
    if (email !== null) {
        // 로그인 상태인 경우
        document.getElementById('signup-btn').style.display = 'none';
        document.getElementById('signin-btn').style.display = 'none';
        document.getElementById('logout-btn').style.display = 'inline-block';

        if (isOwner) {
            // 관리자인 경우
            document.getElementById('owner-btn1').style.display = 'inline-block';
            document.getElementById('owner-btn2').style.display = 'inline-block';
        } else {
            // 일반 사용자인 경우
            document.getElementById('customer-btn').style.display = 'inline-block';
        }
    } else {
        // 비로그인 상태인 경우
        document.getElementById('signup-btn').style.display = 'inline-block';
        document.getElementById('signin-btn').style.display = 'inline-block';
        document.getElementById('logout-btn').style.display = 'none';
        document.getElementById('owner-btn').style.display = 'none';
        document.getElementById('customer-btn').style.display = 'none';
    }
}

function logout() {
    // 쿠키에 저장된 토큰 삭제
    document.cookie = 'Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

    // 로그인 페이지로 리다이렉트
    window.location.href = '/sail/login';
}
