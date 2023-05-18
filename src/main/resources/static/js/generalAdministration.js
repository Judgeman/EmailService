$(document).ready(function() {
    checkAndShowUserChangedStatusMessage();

    $("#emailChangeModal").bind("show.bs.modal", event => {

        const button = event.relatedTarget

        $("#userChangeSaveButton").attr("onclick","changeEmailAddress()")
    })
});

function checkAndShowUserChangedStatusMessage() {
    let searchParams = new URLSearchParams(window.location.search)
    if (searchParams.get("changed")) {
        $("#statusMessage").text("Email-Addresse wurde für die wöchentliche Zusammenfassung eingetragen: " + searchParams.get("changed"));
        $("#statusMessage").show();
    } else {
        $("#statusMessage").hide();
    }
}

function changeEmailAddress() {

    const emailAddress = $("#emailAddressInput").val()
    let emailChangeRequestObject = JSON.stringify(
    {
        "emailAddress": emailAddress
    })

    $.ajax({
        url: '/general/changeEmailAddress',
        type: 'POST',
        data: emailChangeRequestObject,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            $("#emailChangeModal").modal('hide')
            window.location.href="/general/administration?changed=" + emailAddress
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}