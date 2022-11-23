const editButtons = document.querySelectorAll(".edit");
const removeButtons = document.querySelectorAll(".remove");
const cancelButtons = document.querySelectorAll(".cancel");

editButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.showModal();
}));

removeButtons.forEach(e => e.addEventListener("click", e => {
    if (!confirm("삭제하시겠습니까?")) {
        e.preventDefault();
    }
}));

cancelButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.close();
}));