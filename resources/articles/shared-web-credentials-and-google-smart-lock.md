:title Shared Web Credentials and Google Smart Lock
:frontpage
:category api-integration
:body

Shared Web Credentials for iOS and Google Smart Lock for Android will simplify
the login step for all users using native apps.
User having enabled these features, will be prompted in enabled apps to choose
which credentials to use, and they will be automatically filled in.

### Configuration

Shared Web Credentials and Google Smart Lock can be configured
in [SelfService](/selfservice/access/), under Basic settings.

### Shared Web Credentials

#### Links
* Norway - [https://payment.schibsted.no/.well-known/apple-app-site-association](https://payment.schibsted.no/.well-known/apple-app-site-association)

* Sweden - [https://login.schibsted.com/.well-known/apple-app-site-association](https://login.schibsted.com/.well-known/apple-app-site-association)

#### Format
```
{
   "webcredentials": {
       "apps": [ "D3KQX62K1A.com.example.DemoApp" ]
    }
}
```

#### Read more

* https://developer.apple.com/library/ios/documentation/Security/Reference/SharedWebCredentialsRef/



### Google Smart Lock

#### Links
* Norway - [https://payment.schibsted.no/.well-known/assetlinks.json](https://payment.schibsted.no/.well-known/assetlinks.json)
* Sweden - [https://login.schibsted.com/.well-known/assetlinks.json](https://login.schibsted.com/.well-known/assetlinks.json)

#### Format
```
[{
    "relation": ["delegate_permission/common.get_login_creds"],
    "target": {
        "namespace": "web",
        "site": "https://signin.example.com"
    }
},
{
    "relation": ["delegate_permission/common.get_login_creds"],
    "target": {
        "namespace": "android_app",
        "package_name": "com.example",
        "sha256_cert_fingerprints": [
            "F2:52:4D:82:E7:1E:68:AF:8C:BC:EA:B0:A2:83:C8:FE:82:51:CF:63:09:6A:4C:64:AE:F4:43:27:20:40:D2:4B"
        ]
    }
}]
```

#### Read more

* https://developers.google.com/identity/smartlock-passwords/android/
* https://developers.google.com/identity/smartlock-passwords/android/associate-apps-and-sites