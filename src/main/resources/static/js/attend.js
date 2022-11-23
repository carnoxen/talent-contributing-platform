const editButtons = document.querySelectorAll(".edit");
const attendButtons = document.querySelectorAll(".attend");
const cancelButtons = document.querySelectorAll(".cancel");

editButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.open();
}));

attendButtons.forEach(e => e.addEventListener("click", e => {
    if (!confirm("출석하시겠습니까?")) {
        e.preventDefault();
    }
}));

cancelButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.close();
}));