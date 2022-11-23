const kindRadios = document.querySelectorAll("input[name=kind]");
const careerField = document.querySelector("#career-field");
const ownerField = document.querySelector("#owner-field");

kindRadios.forEach(e => {
    e.addEventListener("change", event => {
        const val = event.target.value;
        if ("0" === val) {
            ownerField.removeAttribute("hidden");
            careerField.setAttribute("hidden", "hidden");
        }
        else if ("1" === val) {
            ownerField.setAttribute("hidden", "hidden");
            careerField.setAttribute("hidden", "hidden");
        }
        else if ("2" === val) {
            ownerField.setAttribute("hidden", "hidden");
            careerField.removeAttribute("hidden");
        }
    })
})