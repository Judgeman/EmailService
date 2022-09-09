$(document).ready(function() {
    checkAndShowUserChangedStatusMessage();

    $("#userDeleteModal").bind("show.bs.modal", event => {

        const button = event.relatedTarget
        const username = button.getAttribute("data-bs-username")

        $("#usernamePlaceholder").text(username)
        $("#userDeleteButton").attr("onclick","deleteUser('" + username + "')")
    })

    $("#userChangeModal").bind("show.bs.modal", event => {

        const button = event.relatedTarget
        const username = button.getAttribute("data-bs-username")

        $("#userChangeTitlePlaceholder").text(username)
        $("#userChangeSaveButton").attr("onclick","changeUser('" + username + "')")
    })
});

function checkAndShowUserChangedStatusMessage() {
    let searchParams = new URLSearchParams(window.location.search)
    if (searchParams.get("deleted")) {
        $("#statusMessage").text("User " + searchParams.get("deleted") + " wurde gelöscht");
        $("#statusMessage").show();
    } if (searchParams.get("created")) {
        $("#statusMessage").text("User " + searchParams.get("created") + " wurde erstellt");
        $("#statusMessage").show();
    } if (searchParams.get("changed")) {
        $("#statusMessage").text("User " + searchParams.get("changed") + " wurde geändert");
        $("#statusMessage").show();
    } else {
        $("#statusMessage").hide();
    }
}

function deleteUser(username) {
    $.ajax({
        url: '/user/' + username,
        type: 'DELETE',
        success: function(result) {
            $("#userDeleteModal").modal('hide')
            window.location.href="/user/administration?deleted=" + username
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}

function createNewUser() {
    let newUsername = $("#newUsernameInput").val()
    if (!newUsername) {
        return;
    }

    $.ajax({
        url: '/user/' + newUsername,
        type: 'PUT',
        success: function(result) {
            $("#userNewModal").modal('hide')
            window.location.href="/user/administration?created=" + newUsername
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}

function changeUser(username) {
    let newPassword = $("#newPasswordInput").val()
    let confirmNewPassword = $("#confirmNewPasswordInput").val()

    if (newPassword.trim().length == 0) {
        alert("Ein leeres Passwort ist nicht zulässig!")
        return;
    } else if (confirmNewPassword != newPassword) {
        alert("Die Bestätigung stimmt nicht mit dem Passwort überein!");
        return;
    }

    let userChangeRequestObject = JSON.stringify(
    {
        "username": username,
        "newPassword": newPassword
    })

    $.ajax({
        url: '/user/' + username,
        type: 'POST',
        data: userChangeRequestObject,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            $("#userChangeModal").modal('hide')
            window.location.href="/user/administration?changed=" + username
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}