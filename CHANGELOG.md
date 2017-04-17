# Changelog

### v0.22.4
- Switch get source method to finding by remote id

### v0.22.3
- Method for getting a single source from the database

### v0.22.2
- Add method to update article timestamp in database

### v0.22.1
- Fix bug when inserting articles and returning the incorrect id

### V0.22.0
- Android O support through updated article favoriting method
    - Use the new `ArticleIntent.Builder$setFavoriteService(Class)` method when constructing the article intent
    - Switch the old `BroadcastReceiver` to a `Service` instead

### v0.21.1
- Update drag dismiss dependency

### v0.21.0
- Update support library to 25.3.0

### v0.20.3
- Update the underlying drag dismiss library

### v0.20.2
- Update underlying drag dismiss library

### v0.20.1
- Add method for deleting article from database
- Extract logic for sharing an article to a util class

### v0.20.0
- Abstract drag dismiss activity
- Add black theme option
- Add drag-dismiss to the image viewer

### v0.19.1
- Improvements to the ArticleUtils API

### v0.19.0
- Improve styling of block quotes

### v0.18.2
- Fix another issue with loading the article id when article content is not available
- Fix an issue causing articles not to be marked as saved the first time they are opened
- Sort sources by their name when querying all of them

### v0.17.3
- Fix getting more source information for articles from a specific source

### v0.17.2
- Fix getting more source information for all saved articles

### v0.17.1
- Fix an issue that is logged setting some drawable resources

### v0.17.0
- Create a unique qualifier on the article.url database table

### v0.16.0
- Query the source model name and image url when getting all articles

### v0.15.7
- Better equals() method for source comparisons
- Add datastore method to remove a source
- Remove final modifier on DataSource for easier mocking

### v0.15.2
- Improve Android Wear 2.0 support

### v0.15.1
- Refactor away from the singleton pattern for the `DataSource`

### v0.14.0
- Add `source` and `category` tables for some future features.

### v0.13.0
- Fix proguard configuration. No app's implementing this library will need to make changes

### v0.12.1
- Fix some permission issues

### v0.12.0
- API for saving articles to apps that support it (See [README.md](README.md))

### v0.11.0
- Improve text selection colors
- Trending API endpoints
- Ability to "prepare" an article on the server, without downloading it to the device
- Fixes for blank images
- Update support library

### v0.10.2
- Crash and bug fixes

### v0.10.0
- New `ArticleIntent.Builder#setTextSize` API
- Allow text selection within a paragraph

### v0.9.1
- Bug fixes for image loading

### v0.9.0
- API to fetch articles on whatever thread it is called from, instead of doing it asynchronously

### v0.8.0
- API to preload articles and images
- Handle HTML lists

### v0.7.0
- Elastic Drag-to-Dismiss

### v0.6.0
- Standalone image viewer

### v0.5.0
- Initial Release
