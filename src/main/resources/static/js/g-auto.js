let autocomplete;
const addressField = document.querySelector("#address");
const latitudeField = document.querySelector("#latitude");
const longitudeField = document.querySelector("#longitude");

const addressDialog = document.querySelector("#address-dialog");
const addressGoogleField = document.querySelector("#address-google");

addressField.addEventListener("click", () => {
    addressDialog.show();
})

function fillInAddress() {
    const place = autocomplete.getPlace();
    addressField.value = addressGoogleField.value;
    latitudeField.value = place.geometry.location.lat();
    longitudeField.value = place.geometry.location.lng();
    addressDialog.close();
}

function initAutocomplete() {
    autocomplete = new google.maps.places.Autocomplete(addressGoogleField, {
        fields:["geometry"],
        types:["geocode"]
    });
    addressGoogleField.focus();

    autocomplete.addListener("place_changed", fillInAddress);
}