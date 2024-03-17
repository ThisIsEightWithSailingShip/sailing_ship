document.addEventListener('DOMContentLoaded', function() {
    document.getElementById("submitBtn").addEventListener("click", function(event) {
        event.preventDefault();
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

    console.log(formData)

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
            console.log(data);
        })
        .catch(error => {
            console.error("Error submitting form:", error); // 리다이렉팅 방지
        });
}