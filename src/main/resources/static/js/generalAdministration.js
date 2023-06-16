$(document).ready(function() {
    checkAndShowUserChangedStatusMessage();

    $("#emailChangeModal").bind("show.bs.modal", event => {
        const button = event.relatedTarget
        $("#emailAddressChangeSaveButton").attr("onclick","changeEmailAddress()")
    })

    $("#smtpChangeModal").bind("show.bs.modal", event => {
        const button = event.relatedTarget
        $("#smtpChangeSaveButton").attr("onclick","changeSmtpSettings()")
    })
});

function checkAndShowUserChangedStatusMessage() {
    let searchParams = new URLSearchParams(window.location.search)
    if (searchParams.get("emailAddress")) {
        $("#statusMessage").text("Email-Addresse wurde für die wöchentliche Zusammenfassung eingetragen: " + searchParams.get("emailAddress"));
        $("#statusMessage").show();
    } else if (searchParams.get("smtpHost")) {
        $("#statusMessage").text("SMTP Einstellungen wurde eingetragen: " + searchParams.get("smtpHost") + ":" + searchParams.get("smtpPort"));
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
            window.location.href="/general/administration?emailAddress=" + emailAddress
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}

function changeSmtpSettings() {

    var numbers = /^[0-9]+$/;

    const smtpHost = $("#smtpHostInput").val();
    const smtpPort = $("#smtpPortInput").val();
    const smtpUsername = $("#smtpUsernameInput").val()
    const smtpPassword = $("#smtpPasswordInput").val();
    const smtpPasswordConfirm = $("#smtpPasswordConfirmInput").val();

    if (!smtpHost || !smtpPort || !smtpUsernameInput || !smtpPassword || !smtpPasswordConfirm) {
        alert("Bitte alle Felder ausfüllen!");
        return;
    }

    if (smtpPassword != smtpPasswordConfirm) {
        alert("Passwort stimmt nicht überein!");
        return;
    }

    if (!smtpPort.match(numbers)) {
        alert("Der Port darf nur aus Nummern bestehen!");
        return;
    }

    let smtpChangeRequestObject = JSON.stringify(
    {
        "smtpHost": smtpHost,
        "smtpPort": smtpPort,
        "smtpUsername": smtpUsername,
        "smtpPassword": smtpPassword
    })

    $.ajax({
        url: '/general/changeSmtpSettings',
        type: 'POST',
        data: smtpChangeRequestObject,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            $("#smtpChangeModal").modal('hide')
            window.location.href="/general/administration?smtpHost=" + smtpHost + "&smtpPort=" + smtpPort
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}