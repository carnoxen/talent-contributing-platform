const grantButtons = document.querySelectorAll(".grant");
const revokeButtons = document.querySelectorAll(".revoke");

grantButtons.forEach(e => e.addEventListener("click", e => {
    if (!confirm("수락하시겠습니까?")) {
        e.preventDefault();
    }
}));

revokeButtons.forEach(e => e.addEventListener("click", e => {
    if (!confirm("거절하시겠습니까?")) {
        e.preventDefault();
    }
}));