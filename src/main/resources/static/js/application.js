const apply_button = document.querySelectorAll(".apply");

apply_button.forEach(e => e.addEventListener("click", e => {
    if (!confirm("신청하시겠습니까?")) {
        e.preventDefault();
    }
}))