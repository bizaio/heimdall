# Heimdall

[![Maven Central](https://img.shields.io/maven-central/v/io.biza/heimdall?label=latest%20release)](https://search.maven.org/artifact/io.biza/heimdall) [![Nexus Latest Snapshot](https://img.shields.io/nexus/s/io.biza/heimdall?label=latest%20snapshot&server=https%3A%2F%2Foss.sonatype.org)](https://oss.sonatype.org/#nexus-search;classname~io.biza.heimdall) [![CDR Register v1.1.0](https://img.shields.io/badge/CDR%20Register-v1.1.0-brightgreen)](https://cdr-register.github.io/register)

[![develop build](https://img.shields.io/travis/com/bizaio/heimdall/develop?label=develop%20build)](https://travis-ci.com/bizaio/heimdall) [![master build](https://img.shields.io/travis/com/bizaio/heimdall/master?label=master%20build)](https://travis-ci.com/bizaio/heimdall) [![GitHub issues](https://img.shields.io/github/issues/bizaio/heimdall)](https://github.com/bizaio/heimdall/issues) ![GitHub](https://img.shields.io/github/license/bizaio/heimdall)

The Heimdall project is intended to be an example Register (as defined by the [CDR Register](https://cdr-register.github.io/register)) for the Australian Consumer Data Right (aka "open banking"). It is currently being developed by Biza.io as part of it's DataRight Lab initiative.

Heimdall is currently developed and maintained by [Biza.io](https://www.biza.io).

## Features

  - All defined Register Endpoints implemented
  - CRUD for Add/Remove/Update of Data Holder and Data Recipients
  - Software Statement Assertion signing support

We are currently working on adding the following:
   - Testing! Lots of Testing!
   - MTLS Certificate issuance

## Quick Start

Heimdall is split into a number of components that interact with each other. At a minimum you will need to start the `admin-service` which exposes a CRUD for Holders/Recipients and other Register admin functions. To make a Register like API available run `register-api-service`.

## Table of Contents

- [Features](#features)
- [Quick Start](#quick-start)
- [Running](#running)
- [Deployments](#deployments)
- [Support](#support)
- [Compatibility](#compatibility)
- [Prerequisites](#prerequisites)
- [Architecture](#architecture)
  - [Components](#components)
  - [Database Support](#database-support)
  - [Authentication](#authentication)
- [Production Deployment](#production-deployment)
- [Building](#building)
- [Contributing](#contributing)
- [License](#license)

## Running

[(Back to top)](#table-of-contents)

We are working on cleaner methods of getting started with Heimdall but right now we build from source.

## Deployments

[(Back to top)](#table-of-contents)

*Heimdall* is currently deployed within the following projects or organisations:
- [DataRight Lab](https://dataright.io/lab)

If you are using *Heimdall* in your organisation we welcome you to let us know by [email](mailto:hello@biza.io).

## Support

[(Back to top)](#table-of-contents)

[Biza Pty Ltd](https://biza.io/) are currently the primary maintainers of this software. 

We welcome bug reports via [GitHub Issues](https://github.com/bizaio/heimdall/issues) or if you prefer via [email](mailto:hello@biza.io).

If you are looking for commercial support we offer a number of deployment options including commercial software support, a managed service or pure Software-as-a-Service.


## Compatibility

[(Back to top)](#table-of-contents)

The Heimdall project aims to be entirely compliant to the [CDR Register API Specification](https://cdr-register.github.io/register/). While we try to align our version numbers to those of the Register unfortunately the ACCC, like the DSB, has chosen to use all of the *x.y.z* versioning of the semantic versioning scheme. Consequently the following table outlines the alignment between Heimdall and the respect version of the Register:

Heimdall Version                     | Release Date | Register Compatibility     | Notes                                                             | Status
------------------------------------ | ------------ | -------------------------- | ----------------------------------------------------------------- | --------
1.1.0-SNAPSHOT (**current develop**) | Regular      | 1.1.0                      | Snapshot Development Release                                      | Active Development


## Prerequisites

[(Back to top)](#table-of-contents)

You need the following installed and available in your $PATH during compilation:
- Java 11+
- Apache Maven 3.6.3 or later

## Architecture

[(Back to top)](#table-of-contents)

Heimdall is a combination of frontend and backend components, database storage and authentication clients designed to be implemented either all together or in a distributed fashion for production deployments.

### Components

[(Back to top)](#table-of-contents)

Heimdall is comprised of multiple services designed for either self contained or complete Production deployment.

Component Name                       | Description                                                                           | Dependencies
-------------------------------------|---------------------------------------------------------------------------------------|------------------------------
admin-service                        | OpenAPI 3 Administration API secured by an OIDC server                                | `shared` `payload`
shared                               | Shared Spring and Database components                                                 |
payload                              | Jackson Payload Definitions from the Register, shared by [Electronic Thumb](https://github.com/bizaio/electronic-thumb) |
register-api-service                 | CDR Register Compliant Data Endpoint                                                  | `data` `common`

### Database Support

[(Back to top)](#table-of-contents)

Deep Thought utilises Java Hibernate for database operations. While it is likely that it can support any database Hibernate supports we currently test it for the following database architectures:
  - H2 Database using Local Directory/File storage
  - MySQL Database via Network

**By default** Heimdall components will initialise using a H2 file based database located at `../localdb/heimdall`.

### Authentication

[(Back to top)](#table-of-contents)

Heimdall uses OpenID Connect for authentication. For the Administration API service it uses the scopes of `HEIMDALL:ADMIN:HOLDER:READ`, `HEIMDALL:ADMIN:HOLDER:WRITE`, `HEIMDALL:ADMIN:RECIPIENT:READ`, `HEIMDALL:ADMIN:RECIPIENT:WRITE`, `HEIMDALL:ADMIN:KEY:ADMIN`. For the Register API it uses those defined by the ACCC CDR Register.

## Production Deployment

[(Back to top)](#table-of-contents)

At this stage, production deployment of Heimdall is **not recommended**.

If you are considering setting up Heimdall for anything other than Development then we **strongly** encourage you to contact us by [email](mailto:hello@biza.io).

## Building

This project is a Maven based meta package. Consequently it is possible to build all components at once then run from each sub directory.

1. Clone the repository: `$ git clone https://github.com/bizaio/heimdall`
2. Change to the root project directory: `cd heimdall`
3. Execute the build including the Angular npm wrapper: `mvn clean install -D -Dskip.npm=false`
4. Start each service individually:
   - Start `admin-service`: `cd admin-service; mvn spring-boot:run`
   - Start `register-api-service`: `cd register-api-service; mvn spring-boot:run`


## Contributing

[(Back to top)](#table-of-contents)

1. Clone repository and create a new branch: `$ git checkout https://github.com/bizaio/heimdall -b my_new_branch`
2. Make changes (including tests please!)
3. Submit Pull Request for integration of changes

## License

[(Back to top)](#table-of-contents)

GNU General Public License v3.0 2020 - [Biza Pty Ltd](https://biza.io/). Please have a look at the [LICENSE.md](LICENSE.md) for more details.


