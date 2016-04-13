# Changelog

The release rate is low because _flattr4j_ is mature and stable, and there have been only very few changes to the Flattr API recently.

## v2.13 (14.04.2016)

* Fixed OAuth issue resulting from different Flattr server response (@antennapod)
* Removed deprecated code
* Marked OAuth state related code deprecated, as it is not supported by Flattr any more.

## v2.12 (09.12.2014)

* Removed Apache HttpClient dependency. flattr4j now uses Java's HttpURLConnection.
* Updated json library.
* AccessToken and ConsumerKey are now immutable.

## v2.11 (26.09.2014)

* Removed all deprecated code
* Prepared to make AccessToken and ConsumerKey immutable
* Fix broken generic definition for GetThingsFromCollectionMethod
* Implemented upstream API changes (rename no_means to no_money)

## v2.10 (29.05.2014)

* Fixed broken OAuth2 authorization process (Iulius Gutberlet)

## v2.9 (22.04.2014)

* Added new Async module that helps accessing the Flattr server asynchronously. Thanks to Iulius Gutberlet for contributing.
* Added MiniThing for server results that have been discarded before.
* RateLimit is serializable and got a copy constructor.
* In a future release, the OpenService interface will be merged into FlattrService. The merger is prepared in this release.
* Removed code that was marked as deprecated. You have been warned... ;-)

## v2.8 (23.10.2013)

* Removed unnecessary API limitation of OpenService.getThings()
* Added new user properties "id" and "active_supporter"

## v2.7 (03.05.2013)

* Removed TwitterUserIdentifier as Twitter is not supported any more
* Fixed: getThings(Collection) only returned one Thing
* Improved HTTP Client evaluation on Android devices

## v2.6 (05.02.2013)

* Added Subscription support

## v2.5 (20.10.2012)

* Search supports multiple languages
* Added a generic UserIdentifier class
* Added url of the user's home page
* Added revenue sharing support to buttons
* Added method to read the current rating parameters
* Added evaluation of the rating's current hits and time until reset

## v2.4 (03.06.2012)

* Added Spring Social Provider module

## v2.3 (28.04.2012)

* Improved Android authentication support
* Improved logging on Android

## v2.2 (01.04.2012)

* Added full mode support
* Added email scope
* Added search for multiple categories
* Added popout parameter
* Added logging (Feature #50)

## v2.1.1 (02.03.2012)

* Added support for Apache HttpClient 4.1

## v2.1 (26.02.2012)

* Using git and github for SCM now
* All deprecated code was removed
* Minor internal optimizations and cleanups
* pom files prepared for Maven Central

## v2.0 (12.02.2012)

* Added Partner Site Integration support
* Added method to list all flattrs of a thing
* Updated model classes to all API changes up to 11.02.2012

## v2.0-rc3 (11.01.2012)

* Added Search methods
* Added support for Activities
* Added Auto Submission URLs
* Other minor changes to support the latest Flattr API v2

## v2.0-rc2 (18.11.2011)

* Added Thing.merge() for updates by Submission objects
* Set HTTP timeout of 10 sec. to avoid deadlocks on broken connections
* Improved network performance
* Added gzip support

## v2.0-rc1 (11.11.2011)

* Code cleanups and minor API changes

## v2.0-beta (03.11.2011)

* Updated to the new Flattr API v2
* Major refactoring and API changes due to API v2
* Integrated OAuth2 support (signpost is not required any more)
* Maven 3 fixes

## v1.2 (10.05.2011)

* flattr4j is now Android API level 4 compliant
* Using a hardcoded SSL cert, which is required on Android to connect to Flattr
* Reworked XML parsers (now Java 1.5 compliant)

## v1.1 (04.04.2011)

* Added method to get a Thing by its URL

## v1.0 (26.02.2011)

* First stable release
* Minor API change to the services

## v1.0-rc1 (02.02.2011)

* Updated to Flattr API v0.5
* Added support of undocumented calls
* Major API refactoring

## v0.5 (13.01.2011)

* Fix: XML header encoding was ignored
* Flattr service offers new browse method

## v0.4 (09.01.2011)

* Support of the new button designs and sizes
* Buttons and badges are separated
* Added service method to search for Things
* Thing returns an URL to a QRcode PDF
* Minor API changes to the factory

## v0.3 (23.10.2010)

* Using OAuth 1.0a
* OUT_OF_BAND mode is supported

## v0.2 (17.10.2010)

* Added Spring support

## v0.1 (09.10.2010)

* First testing release
