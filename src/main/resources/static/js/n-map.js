const addressField = document.querySelector("#address");
const latitudeField = document.querySelector("#latitude");
const longitudeField = document.querySelector("#longitude");

const addressDialog = document.querySelector("#address-dialog");
const addressMap = document.querySelector("#address-map");
const addressList = document.querySelector("#address-list");

addressField.addEventListener("click", () => {
    addressDialog.showModal();
});

addressMap.addEventListener("input", async e => {
    const query = e.target.value;

    const addresses = await fetch(`https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=${query}`, {
        headers: {
            "X-NCP-APIGW-API-KEY-ID": "zjculf2oob",
            "X-NCP-APIGW-API-KEY": "s0OM1fy20dGyDALk8HCenocmzTAmPRlMBdZxhzpU"
        }
    }).then(r => r.json())
    .then(d => d.address)

    addresses.map(e => {
        const option = document.createElement("option");
        option.textContent = e.roadAddress;
        option.value = `${e.y},${e.x}`; // lat / long
        return option;
    })
})

addressList.childNodes.forEach(e => e.addEventListener("click", e => {
    const [lat, long] = e.target.value.split(',');
    addressField.value = e.target.textContent;
    latitudeField.value = lat;
    longitudeField.value = long;
}))