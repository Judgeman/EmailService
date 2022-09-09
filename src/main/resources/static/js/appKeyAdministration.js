$(document).ready(function() {
    checkAndShowAppKeyChangedStatusMessage();

    $("#appKeyValueModal").bind("show.bs.modal", event => {

      const button = event.relatedTarget
      const appId = button.getAttribute("data-bs-appId")
      const keyValue = button.getAttribute("data-bs-keyValue")

      $("#keyValuePlaceholder").text(keyValue)
      $("#appKeyValueModalTitle").text(appId)
    })

    $("#appKeyDeleteModal").bind("show.bs.modal", event => {

        const button = event.relatedTarget
        const appId = button.getAttribute("data-bs-appId")

        $("#appKeyIdPlaceholder").text(appId)
        $("#appKeyDeleteButton").attr("onclick","deleteAppKey('" + appId + "')")
    })

    $("#appKeyChangeModal").bind("show.bs.modal", event => {

        const button = event.relatedTarget
        const appId = button.getAttribute("data-bs-appId")
        const keyValue = button.getAttribute("data-bs-keyValue")
        const senderEmailAddress = button.getAttribute("data-bs-senderEmailAddress")

        $("#appKeyChangeTitlePlaceholder").text(appId)
        $("#appKeyChangeSaveButton").attr("onclick","changeAppKey('" + appId + "')")
        $("#appKeyValueInput").val(keyValue);
        $("#appKeySenderEmailInput").val(senderEmailAddress);
    })
});

function checkAndShowAppKeyChangedStatusMessage() {
    let searchParams = new URLSearchParams(window.location.search)
    if (searchParams.get("deleted")) {
        $("#statusMessage").text(searchParams.get("deleted") + " wurde gelöscht");
        $("#statusMessage").show();
    } if (searchParams.get("created")) {
        $("#statusMessage").text(searchParams.get("created") + " wurde erstellt");
        $("#statusMessage").show();
    } if (searchParams.get("changed")) {
        $("#statusMessage").text(searchParams.get("changed") + " wurde geändert");
        $("#statusMessage").show();
    } else {
        $("#statusMessage").hide();
    }
}

function deleteAppKey(appKeyId) {
    $.ajax({
        url: '/appKey/' + appKeyId,
        type: 'DELETE',
        success: function(result) {
            $("#appKeyDeleteModal").modal('hide')
            window.location.href="/appKey/administration?deleted=" + appKeyId
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}

function createNewAppKey() {
    let newAppKeyId = $("#newAppKeyInput").val()
    if (!newAppKeyId) {
        return;
    }

    $.ajax({
        url: '/appKey/' + newAppKeyId,
        type: 'PUT',
        success: function(result) {
            $("#appKeyNewModal").modal('hide')
            window.location.href="/appKey/administration?created=" + newAppKeyId
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}

function changeAppKey(appKeyId) {
    let appKeyValue = $("#appKeyValueInput").val()
    let appKeySenderEmail = $("#appKeySenderEmailInput").val()

    if (appKeySenderEmail.trim().length == 0) {
        appKeySenderEmail = null
    }

    let changedAppKey = JSON.stringify(
    {
        "appId": appKeyId,
        "keyValue": appKeyValue,
        "specificSenderEmailAddress": appKeySenderEmail
    })

    $.ajax({
        url: '/appKey/' + appKeyId,
        type: 'POST',
        data: changedAppKey,
        contentType: "application/json; charset=utf-8",
        success: function(result) {
            $("#appKeyChangeModal").modal('hide')
            window.location.href="/appKey/administration?changed=" + appKeyId
        },
        error: function() {
            alert("Ups da ist etwas schief gelaufen!")
        }
    });
}