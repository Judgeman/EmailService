$( document ).ready(function() {
    loadEmails(5);
});

function loadEmails(limit) {
    $("#response").html("Lade Daten...");
    $.getJSON('/emails?limit=' + limit, function(emails) {

        $("#response").html("");
        for (var i = 0; i < emails.length; i++) {
            var email = emails[i];

            var verifiedIcon = email.appKeyValueVerified ? '<i class="fa-solid fa-key text-success"></i>' : '<i class="fa-solid fa-xmark text-danger"></i>';
            var emailSentIcon = email.emailSent ? '<i class="fa-solid fa-paper-plane text-success"></i>' : '<i class="fa-solid fa-xmark text-danger"></i>';

            var box = '<div class="card">' +
                         '<div class="card-header text-left">' +
                            '<h5><span class="badge bg-secondary">' + email.id + '</span> ' + email.subject + '</h5>' +
                         '</div>' +
                         '<div class="card-body text-left">' +
                            '<h5 class="card-text">' + email.message + '</h5>' +
                            '<ul class="list-group list-group-flush">' +
                                 '<li class="list-group-item">an: ' + email.emailAddress + '</li>' +
                                 '<li class="list-group-item">von: ' + email.senderAddress + '</li>' +
                                 '<li class="list-group-item">AppKey: ' + email.appKeyId + ' ' + verifiedIcon + '</li>' +
                                 '<li class="list-group-item">gesendet: ' + emailSentIcon + (email.emailSent ? "" : " - Fehler: " + email.errorMessage) + '</li>' +
                                 '<li class="list-group-item">Remote Address: ' + email.remoteRequestAddress + '</li>' +
                            '</ul>' +
                         '</div>' +
                         '<div class="card-footer text-muted text-center">' + email.sendingDate + '</div>' +
                       '</div></br>';

            $("#response").append(box);
        }
    });
}