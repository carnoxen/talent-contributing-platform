const lecsels = document.querySelectorAll(".lecsel");
const context_path = document.querySelector("#context-path");

lecsels.forEach(e => e.addEventListener("change", event => {
    location.href = `${context_path.value}${event.target.value}`;
}))