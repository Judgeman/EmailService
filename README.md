# EmailService
Spring Boot Server with an API to handle email requests and send it with a smtp connection.

### Email Request
Every request needs an AppKey, and it must be registered in the database.
All requests without a valid AppKey will be blocked.

To send an email with this service the AppKey, the target email address, the subject and the message itself must be sent via JSON to the endpoint `/sendEmail`
The JSON body of the request must be like this:
```
{
    "appKey" : {
        "appId" : "ExampleApplicationName",
        "keyValue" : "XXXXXXXX-XXX-XXXX-XXXX-XXXXXXXXXXXX"
    },
    "emailAddress" : "max@musterman.de",
    "subject" : "Betreff der Email",
    "message" : "Nachricht der Email"
}
```

The default sender address is set in the application.properties by the keyword `defaultSystemEmailAddress`

#### Add new AppKeys simple
For now there are now CMS to register new or delete old AppKeys. 
All changes must be performed directly on the database. 
For a quick adding AppKeys to the database a simple PUT request can be performed to `/appKey/{appId}` to create a new AppKey for an AppId. An AppId must be unique.
By default, the quick adding is disabled. It can be enabled on the application.properties via `simpleCreatingNewAppKeysViaRequestAllowed`