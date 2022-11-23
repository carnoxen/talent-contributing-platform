const createButtons = document.querySelectorAll(".create");
const cancelButtons = document.querySelectorAll(".cancel");

createButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.open();
}));

cancelButtons.forEach(e => e.addEventListener("click", e => {
    const dialog = document.querySelector(`#dialog${e.target.dataset.id}`);
    dialog.close();
}));