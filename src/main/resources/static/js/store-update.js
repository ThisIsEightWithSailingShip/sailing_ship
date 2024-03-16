document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("submitBtn").addEventListener("click", function() {
        submitForm();
    });
});

function submitForm() {
    const formData = {
        address: document.getElementById("address").value,
        phone: document.getElementById("phone").value,
        category: document.getElementById("category").value,
        storeName: document.getElementById("storeName").value
    };

    fetch("/sail/store/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            console.log(data); // 서버 응답 로그
            // 처리할 작업 추가
        })
        .catch(error => {
            console.error("Error submitting form:", error);
        });
}