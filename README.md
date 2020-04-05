# CoronaTracker

 <img height=300 width=1000 src="https://user-images.githubusercontent.com/29357444/78502309-33640900-777e-11ea-8917-f737c19c9558.png" />

App link (v2.5.2): https://bit.ly/3bVxklh <br>
App link (v2.5.1): https://bit.ly/3dV9yHO <br>
App link (v2.5.0): https://bit.ly/33Twoe9 <br>
App link (v1.5.5): https://bit.ly/3dv8qdL <br>
App link (v1.5.0): https://bit.ly/3bm65jt <br>
App link (v1.0.0): https://bit.ly/3abh6np 

A simple Android application which will help you track the latest spread of corona virus and other useful features.

Simple and light weight application.

ScreenShots :
<br><br>
<b>App Version 2.5.0:</b>
<br><br>
<span>
 <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77842692-f9f63100-71b2-11ea-8ba0-e3bb75adce10.gif" />
 </span>
 <br><br>
<b>App Version 1.5.0:</b>
<br><br>
<span>
 <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77520204-daa38f00-6ea6-11ea-8674-2f409a82a38d.jpeg" />
  <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77520240-eb540500-6ea6-11ea-9299-cc7268c74f98.jpeg" />
  <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77520278-f7d85d80-6ea6-11ea-876a-a9d6904b306c.jpeg" />
 </span>
 <br><br>
<b>App Version 1.0.0:</b>
<br><br>
<span>
 <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77245472-d8da9100-6c44-11ea-97cf-f028448829b3.png" />
  <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77245482-e5f78000-6c44-11ea-9001-20eea361cc31.png" />
  <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77245494-f3ad0580-6c44-11ea-918f-3a50ec08fc2d.png" />
  <img height=400 width=200 src="https://user-images.githubusercontent.com/29357444/77245626-f4926700-6c45-11ea-917a-3643d016d569.jpeg" />
 </span>
 
<b>Backend Logic :</b> 

1. In-App Update : Firebase is implmented for In-App Update where whenever the user opens the app the current version of app is checked with the latest version given in firebase database, and if its lower than that of the latest version then user will get an update dialog with an intent to browser with latest download link.

2. Push Notifications : Firebase Cloud Messaging (FCM) is used for the following.

3. Important Announcement Activity : the data is also fetched dynamically from firebase database. 

<b>Information Source :</b> 

<del>1. Global Tracker - https://github.com/ExpDev07/coronavirus-tracker-api<del>
 
1. Global Tracker - https://corona.lmao.ninja/countries

2. Precautions - https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public

3. COVID-19 Risk Level - New Jersey Department of Health 

4. Helpline Data - https://api.rootnet.in/covid19-in/contacts

<del>5. State Wise Data - https://api.rootnet.in/covid19-in/stats/latest<del>

5. State Wise Data - https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise

<del>6. Indian Data - https://api.covid19india.org/data.json<del>

