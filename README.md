# Java Flattr Client ![build status](https://jenkins.shredzone.net/project/flattr4j/builds/status.png?ref=master)

_flattr4j_ is a free open source Java library for using the [Flattr](https://flattr.com) API and adding Flattr buttons to JSP web sites.

It primarily targets for business grade Flattr integration, e.g. on servers, web sites and full-featured desktop applications, but it can also be used on Android devices.

_flattr4j_ is not endorsed by or affiliated with Flattr AB.

## Important Note

Flattr is currently changing its API. Because of that, some of the flattr4j calls are failing or not delivering the expected results.

Are you getting a "Received fatal alert: handshake_failure" exception? OpenJDK does not seem to support any of the Flattr server's cipher suites. Use the Oracle JDK instead!

## Features

* Lightweight and easy to use
* Covers the entire Flattr API
* Also runs on Android
* JSP tag library for inserting static and [javascript](https://flattr.com/support/integrate/js) Flattr buttons
* Helps through the OAuth authentication process at Flattr, no OAuth knowledge is needed
* With Maven and Spring support
* [Spring Social](http://www.springsource.org/spring-social) provider available (currently beta)

## Usage

* See the [online documentation](http://www.shredzone.org/maven/flattr4j/) about how to use _flattr4j_.

## Contribute

* Fork the [Source code at GitHub](https://github.com/shred/flattr4j). Feel free to send pull requests.
* Found a bug? [File a bug report!](https://github.com/shred/flattr4j/issues)

## License

_flattr4j_ is open source software. The source code is distributed under the terms of [GNU General Public License Version 3](http://www.gnu.org/licenses/gpl-3.0.html), [GNU Lesser General Public License Version 3](http://www.gnu.org/licenses/lgpl-3.0.html), [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
