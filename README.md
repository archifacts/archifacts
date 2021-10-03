[![Build](https://github.com/archifacts/archifacts/actions/workflows/build-on-push.yml/badge.svg)](https://github.com/archifacts/archifacts/actions/workflows/build-on-push.yml) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.archifacts/archifacts-core/badge.svg?)](https://maven-badges.herokuapp.com/maven-central/org.archifacts/archifacts-core) [![License](https://img.shields.io/github/license/archifacts/archifacts.svg?)](https://github.com/archifacts/archifacts/blob/main/LICENSE)

<img width="50%" src="archifacts-1-line.svg">

*archifacts* is a free ([Apache 2.0 license](https://github.com/archifacts/archifacts/blob/main/LICENSE)) library for describing and detecting architectural building blocks and their relationships in your Java applications.

*archifacts* heavily relies on [ArchUnit](https://www.archunit.org/) which analyzes the Java bytecode. With the help of descriptors archifacts identifies building blocks, relationships and containers of your application and builds a model.

Having this model in place you can visualize your application's architecture or verify it against certain rules.

While we already have some support for the former, the latter is subject of future work.

## Experimental API
**Caution!** *archifacts* is in a very early state. The API is not intended to be stable. We might introduce breaking changes at any time if we think it improves the overall user experience. Please be aware of this, if you decide to use *archifacts*.

Nevertheless we try to reduce the breaking changes to a minimum, but there is no guarantee.

## Usage

### Gradle

```Gradle
testImplementation 'org.archifacts:archifacts-core:0.2.0'
```

### Maven

```xml
<dependency>
    <groupId>org.archifacts</groupId>
    <artifactId>archifacts-core</artifactId>
    <version>0.2.0</version>
    <scope>test</scope>
</dependency>
```

## How to get started
Take a look at our [jmolecules example](examples/jmolecules-spring-data-jpa) to get an idea about how to use *archifacts*.

With [jbang](https://www.jbang.dev/) you can even execute it directly from GitHub:
```
jbang https://github.com/archifacts/archifacts/blob/main/jbang-examples/JMoleculesSpringDataJPAExample.java
```
After executing a webserver is launched which serves the documentation at port 8080. If a browser is available the URL [http://localhost:8080/](http://localhost:8080/) is opened automatically.

## Why is it called 'archifacts'?

*archifacts* is a made-up word out of *architects*, *artifacts* and *facts*.

*architects* who want to visualize or verify their architecture are the main target group of the library.

Every class, interface or enum in your application is treated as an *artifact*. *Artifact* is the common base class for all these elements and therefore the foundation of *archifacts*.

*archifacts* is all about *facts* as the model is extracted from bytecode. With this approach *archifacts* tackles outdated documentation. The model contents are *facts*.

## How can I contribute?
The most helpful contribution in this early project phase is feedback. Feedback about bugs, missing features, misconceptions, successes, whatever. We would like to get in touch with the library's users to improve *archifacts*.

## License

ArchUnit is published under the Apache License 2.0, see <http://www.apache.org/licenses/LICENSE-2.0> for details.
