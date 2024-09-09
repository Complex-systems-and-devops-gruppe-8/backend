#!/bin/sh
(cd ../../rest-client-quickstart ; ./mvnw clean package -Dquarkus.package.type=fast-jar -DskipTests)