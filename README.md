# ManageMembers
Android application for managing membership of Cafe.

### Basic features
* cafe owner can manage all customers who has taken membership.
* it asks customer to select 10 drinks for membership and if customer chooses premium drink total selection number is decreases to 8.
* auto backup when any changes happen it will create backup of it.
* Next time when user user his membership QR Code, app will open buyActivity where user can select drinks.
* it will send sms to user on every use of ones membership.

### Highlights
* QR Scanner implementation.
* All data is stored using SQlite databse.
* Owner can send special message to all the members.

### QR Scanner
for implementing qr code I used custom library.

```
implementation 'com.journeyapps:zxing-android-embedded:3.4.0'
```
if you build this app on traget device 30 it will not create backup due to new privacy changes by google.
so build in on target device as 10.

### Example 


