# Generic OAuth plugin for GoCD

The plugin allows user to login in GoCD using an Generic account. It is implemented using [GoCD authorization endpoint](https://plugin-api.gocd.org/current/authorization/).

# Installation

Installation documentation available [here](INSTALL.md).

# Capabilities

* Currently supports authentication and authorization capabilities.

## Building the code base

To build the jar, run `./gradlew clean test assemble`

### with Docker

```
APP=generic-oauth-authorization-plugin
docker build -t $APP .
docker create --name $APP $APP
docker cp $APP:/usr/src/gocd-$APP/build/libs/$APP-1.0.0-4.jar .
docker rm $APP
docker rmi $APP
```

## License

```plain
Copyright 2017 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
