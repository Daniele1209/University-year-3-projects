Book object attributes:
- _id
- title
- author
- genre
- latitude
- longitude
- image
- userId

App features (for items):
- visualise the list of all available books from the server
- expand detail for each book
- edit a certain book and save the changes
- add a new book and save it

Authentication:
- store user token after authentication
- skip login page if token exists
- logout function

User specific functions:
- link resources to specific users (using userId attribute)

Online/Offline:
- notifies user if the app is offline
- if there is no connection to the internet, app stores items and changes locally
- as soon as the device goes online it update the database

Pagination/Search/Filter
- can filter books by genre attribute
- infinite scrollin for pagination
- search with partial matching