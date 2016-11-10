![feature graphic](artwork/sample.png)

# Android Article Viewer

This library is an attempt to build on top of the concept of Chrome Custom Tabs - taking it one step further and creating a consistent experience for news/article viewing. It's main goal is to detect whether a URL is a link to an article somewhere on the web and if it is, parse that article and display it in a RecyclerView. If it isn't, then open the link in a Chrome Custom Tab instead.

## Including It In Your Project

One of the major goals is to make this as simple as possible to integrate into your new or existing apps when you're already considering (or have implemented) using Custom Tabs for opening links.

To include it in your project, add this to your `build.gradle` file:

```groovy
dependencies {
	...
	compile 'com.klinkerapps:article_viewer:0.7.0-SNAPSHOT'
}
```

When using a chrome custom tab, you would create and start the view with code similar to the following:

```java
CustomTabIntent intent = new CustomTabIntent.Builder()
        .setToolbarColor(primaryColor)
        .build();

intent.launchUrl(this, Uri.parse(url));
```

My goal was to enable you to simply swap out the `CustomTabIntent` class for `ArticleIntent`, meaning the the previous example would be invoked like the following for this library:

<pre lang="java">
<b>ArticleIntent</b> intent = new <b>ArticleIntent</b>.Builder(<b>this</b>)
        .setToolbarColor(primaryColor)
        .build();

intent.launchUrl(this, Uri.parse(url));
</pre>

Simple, right? You can pass any parameters into the builder that you would normally use in a custom tab such as colors and toolbar actions (however, many of those actions are not currently supported by this library, though they will be displayed if the library ends up opening a custom tab instead of displaying the article natively). I also added on a few extras to the builder that you can use to customize the UI more to your liking:

<pre lang="java">
ArticleIntent intent = new ArticleIntent.Builder(this)
        .setToolbarColor(primaryColor)
        <b>.setAccentColor(accentColor)</b>
        <b>.setTheme(ArticleIntent.THEME_DARK)</b>
        .build();

intent.launchUrl(this, Uri.parse(url));
</pre>

You can check out the sample application for more information and implementation notes.

## How It Works

This library leverages a node.js backend that I have deployed on AWS that does all of the heavy lifting for processing an article. On the backend, we go and grab the article and strip out anything in it that we don't want as soon as we get a URL from the app. We'll then return the results to the library and cache them in a MongoDB instance so that next time we get a request for the same article, it is significantly faster to load.

## Why Should I Use This?

There are quite a few benefits that I see from using this library over just simple Custom Tabs, but of course there are also some downsides.

Benefits:
* Save user's time and data
 * responses are smaller since all of the extra junk (ads, other articles, etc) is taken out
 * can load significanly faster than a full webpage when article is already cached
* Consistent (and beautiful) UI accress all of the articles that you send to it
* Keeps your users inside and enjoying your app instead of sending them elseware to view an article

Downsides:
* When not an article, we still take time to try and process it on the server and return something to the device, so that can be some wasted time that could have been spent going immediately to the Custom Tab insteead. After we get a good amount of cached data however, this should be negligable as all of the responses will be immediately from the server and won't require additional processing
* It's difficult to parse every article out there since every site formats it's data differently. If this is the case, then we'll fall back to opening the article in a Custom Tab immediately, however sometimes it's difficult to recognize on the server whether or not something actually is an article. We're trying to apply machine learning to get better at this.
* You're sending your users to our backend instead of your own. I don't collect any personal data from anything sent to the server, just cache the requested article so there aren't really privacy concerns here that I can see, just something to be aware of.

I personally think that the benefits of a smooth, quick UI outweigh the downsides, espcially in apps where a lot of users are sharing links to articles like on Twitter or texting.

## License

    Copyright 2016 Jake Klinker

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
