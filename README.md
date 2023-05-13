# udacity-capstone

Project consists of 2 main features:

Rss feed reader and Map list of riding spots.

On the main screen, under the main image, there is an crank image that rotates on click. Rotation is animated via Motion Layout.

On the "see the news" button press, user is taken to a screen where he can choose the source of the news feeds. Banner is fixed, button below the banner takes the user to the new screen, where RSS feeds are fetched and displayed in the recycler view.
Feeds are stored in Room database, and are available when user is offline (for the first fetch, user must be online. Online fetching is performed via Retrofit). On the feed item click, system web browser is opened and entire web page is displayed.

On see the riding spots, a screen with a google map and stored riding spot list is displayed. On initial launch, there are 4 preset locations visible on the map. FAB in the lower right corner takes user to the new screen where a new riding spot can be added. To add a riding spot, at least titel and location must be selected. Location can be selected with long press on the map or via click on POI.
If entered data is valid, riding location is stored in the room database, displayed in the initial riding spot screen map and recycler view and geofence for the riding spot is started. When user enters geofenced area, notificatin is sent. Notification only contains Title and riding spot Location.

Google maps API key is included in project, but is limited to this app ID. 
Fragment navigation is performed via navHostFragment.
Runtime permissions are requested when needed.
