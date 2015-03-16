# OnTime
## Jason Feng, Dani Gnibus, Emily Greene, Nick Fiacco
### Dartmouth College
### Winter 2015

## Introduction:
Hate when you’re the first person to arrive to FoCo and proceed to wait in the lobby for the next 20 minutes? Ever sit awkwardly in Collis trying not to let anyone see you’re sitting alone while you’re friends take their time getting there? Well now with OnTime, you and your friends can always arrive simultaneously!

This application will inform individuals organizing an event when they need to leave their current location in order to arrive at the destination at the specified event time. It will calculate departure times based on both a user’s current location as well as the current location status of other attendees. The app will be useful in helping all attendees synchronize their arrival times at the event so that no one will wait alone for the other attendees to get there.

## Functionality:

Facebook Integration

Organize events between you and your closest friends

Location progress tracking using Google Maps and Location Services to determine the approximate arrival time of your friends

Search google maps for addresses and places

## Layout:
Sign-in Screen: Facebook login is initially shown on app installation, then My Events is shown on startup

My Events (homepage): with button for create event and clickable list of events.

Create Event: fragment with dialogs to select invitees, set location, and set date/time

Event Information: Provides important infomation about the event details and shows when and where an event is. It also includes a progress bar for other invitees to display their approximate distance to the event.

Settings Tab: Information about your facebook integration and the ability to log out of facebook
