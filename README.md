[![Verify](https://github.com/archifacts/archifacts/actions/workflows/verify-on-push.yml/badge.svg)](https://github.com/archifacts/archifacts/actions/workflows/verify-on-push.yml) [![Maven Central](https://img.shields.io/maven-central/v/org.archifacts/archifacts-core?label=Maven%20Central)](https://search.maven.org/artifact/org.archifacts/archifacts-core) [![License](https://img.shields.io/github/license/archifacts/archifacts.svg?)](https://github.com/archifacts/archifacts/blob/main/LICENSE)

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
implementation 'org.archifacts:archifacts-core:<latest-version>'
```

### Maven

```xml
<dependency>
    <groupId>org.archifacts</groupId>
    <artifactId>archifacts-core</artifactId>
    <version>&lt;latest-version&gt;</version>
</dependency>
```

## How to get started

Take a look at our [Fraktalio demo application example](https://github.com/archifacts/fraktalio-example) and [Spring Restbucks example](https://github.com/archifacts/spring-restbucks-example) to get an idea about how to use *archifacts*.

### Fraktalio

The project documents the [Fraktalio demo application](https://github.com/fraktalio) which is a Spring Boot and Axon Framework based microservice system, implemented in Java and Kotlin.

The generated documentation is deployed using GitHub Actions. You can find it [here](https://archifacts.github.io/fraktalio-example/)

### Spring Restbucks

The project documents the [Spring Restbucks application](https://github.com/odrotbohm/spring-restbucks) which is a Spring Boot and jMolecules based modular monolith system, implemented in Java.

The generated documentation is deployed using GitHub Actions. You can find it [here](https://archifacts.github.io/spring-restbucks-example/)

## Why is it called 'archifacts'?

*archifacts* is a made-up word out of *architects*, *artifacts* and *facts*.

*architects* who want to visualize or verify their architecture are the main target group of the library.

Every class, interface or enum in your application is treated as an *artifact*. *Artifact* is the common base class for all these elements and therefore the foundation of *archifacts*.

*archifacts* is all about *facts* as the model is extracted from bytecode. With this approach *archifacts* tackles outdated documentation. The model contents are *facts*.

## How can I contribute?
The most helpful contribution in this early project phase is feedback. Feedback about bugs, missing features, misconceptions, successes, whatever. We would like to get in touch with the library's users to improve *archifacts*.

## Release process

Releases are created manually via GitHub Releases and published by the `Release` GitHub Actions workflow.

### Prerequisites

- The required repository secrets for Maven Central publishing and GPG signing are configured:
  - `SONATYPE_USERNAME`
  - `SONATYPE_PASSWORD`
  - `GPG_PRIVATE_KEY`
  - `GPG_PASSPHRASE`
  - `MAVEN_GPG_KEYNAME`
- All changes for the release are merged and verified on `main`.

### Steps

1. Create and push a release tag (SemVer-style), for example:

```bash
git checkout main
git pull
git tag 0.7.0
git push origin 0.7.0
```

2. Open GitHub -> `Releases` -> `Draft a new release`.
3. Choose the existing tag from the tag selector.
4. Add title and release notes.
5. Publish the release.

Publishing the release triggers `.github/workflows/release.yml` (`release.published` event), checks out the tagged commit, and runs Maven deploy with:

```bash
-Drevision=<tag-name>
```

This repository uses Maven CI-friendly versions, so the Git tag is the source of truth for the released artifact version.

## License

archifacts is published under the Apache License 2.0, see <http://www.apache.org/licenses/LICENSE-2.0> for details.
