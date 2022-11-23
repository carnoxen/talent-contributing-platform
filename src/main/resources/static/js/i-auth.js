const authButton = document.querySelector("#btn-phone-auth");
// const phoneInput = document.querySelector("#phone");
const nameInput = document.querySelector("#name");
const uidInput = document.querySelector("#uid");
const message = document.querySelector("#authenticated");

const usernameInput = document.querySelector("#username");
const verifyEmailButton = document.querySelector("#btn-verify-email");
const verifyCodeButton = document.querySelector("#btn-verify-code");
const codeBlock = document.querySelector("#code-block");
const codeInput = document.querySelector("#code")
const verifiedHidden = document.querySelector("#verified");

const form = document.querySelector("form");

const IMP = window.IMP;
IMP.init("imp43560143");

authButton.addEventListener("click", () => {
    IMP.certification({
        // phone: phoneInput.value,
        popup: true,
    }, rsp => {
        if (rsp.success) {
            uidInput.value = rsp.imp_uid;
            // phoneInput.setAttribute("readonly", "readonly");
            authButton.remove();
            message.textContent = `확인됨`
        }
        else {
            message.textContent = `에러: ${rsp.error_msg}`
        }
    })
});

usernameInput.addEventListener("input", () => {
    verifyEmailButton.removeAttribute("disabled");
});

verifyEmailButton.addEventListener("click", async () => {
    verifyEmailButton.setAttribute("disabled", "disabled");
    verifyEmailButton.value = "전송 중"

    fetch(`/tft/verify/username?` + new URLSearchParams({username: usernameInput.value}))
    .then(res => {
        if (!res.ok) 
            throw new Error("member already exists");
        return res.text();
    })
    .then(data => {
        console.info(data);
        codeBlock.removeAttribute("hidden")
        verifyEmailButton.removeAttribute("disabled");
        verifyEmailButton.value = "코드 다시 전송"
    })
    .catch((error) => {
        console.error(error);
        alert("이미 존재하는 이메일입니다. 다른 이메일로 신청 바랍니다.");
        verifyEmailButton.removeAttribute("disabled");
        verifyEmailButton.value = "이메일 확인"
    })
    .finally()
})

verifyCodeButton.addEventListener("click", async () => {
    verifyCodeButton.setAttribute("disabled", "disabled");
    verifyCodeButton.value = "전송 중";

    fetch(`/tft/verify/code?` + new URLSearchParams({username: usernameInput.value, code: codeInput.value}))
    .then(res => {
        if (!res.ok) 
            throw new Error("code send failed");
        return res.text();
    })
    .then(() => {
        verifyEmailButton.remove();
        verifyCodeButton.remove();
        codeBlock.remove();

        verifiedHidden.value = true;
        const confirmedSpan = document.createElement("span");
        confirmedSpan.textContent = "이메일 확인 완료";
        usernameInput.insertAdjacentElement("afterend", confirmedSpan);
    })
    .catch(() => {
        alert("코드가 맞지 않습니다. 다시 확인해주시기 바랍니다.");
        verifyCodeButton.removeAttribute("disabled");
        verifyCodeButton.value = "코드 확인";
    })
})

form.addEventListener("submit", e => {
    const password = e.target.elements.password.value
    const password_confirm = e.target.elements.password_confirm.value

    const requiredInputs = Array.from(document.querySelectorAll("input[required]"));
    const reqWithNoValue = requiredInputs.some(e => e?.value === "" || e?.value === undefined || e?.value === null)

    if (password !== password_confirm || reqWithNoValue /* || ! /\d{9,12}/.test(phoneInput.value) */) {
        alert("양식을 확인해주세요.");
        e.preventDefault();
    }
})