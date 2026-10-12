# HerSafe
HerSafe 🛡️
A women's safety app in a single HTML file. Works in a browser and as an Android APK (Cordova).
Features
SOS with a 5-second cancelable countdown: sends location to trusted contacts by SMS (works without internet)
Volume-up button x3 starts SOS (APK only, app must be open)
Location, nearby police / hospitals / pharmacies
Journey timer ("Are you safe?" check), siren, fake call
Safety guides, helplines, official links
Permission setup in Hindi / English, one at a time, with spoken explanation
All data stays on the phone. No login, no tracking.
Browser vs APK
Feature
Browser
APK
SOS, siren, guides, location
Yes
Yes
Real SMS to contacts
Opens messaging app
Yes
Volume-button SOS
No (browsers cannot read hardware keys)
Yes
Build the APK (no install needed)
Fork or upload this repo to your GitHub account.
Open the Actions tab and run Build APK.
Download HerSafe-apk from the run's Artifacts and install app-debug.apk.
To change the app, edit www/index.html.
Disclaimer
HerSafe cannot confirm that a call connected or an SMS was delivered. In an emergency, call 112.
License
MIT
