#!/bin/sh

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

if [ -f "$SCRIPT_DIR/dev_env.sh" ]; then
    source "$SCRIPT_DIR/dev_env.sh"
else
    echo "JWT environment file not found. Please run setup.sh first."
    exit 1
fi

if [ -z "$SMALLRYE_JWT_SIGN_KEY_LOCATION" ] || [ -z "$MP_JWT_VERIFY_PUBLICKEY_LOCATION" ]; then
    echo "JWT key environment variables are not set. Please run setup.sh first."
    exit 1
fi

(cd ../../rest-client-quickstart ; ./mvnw quarkus:dev)
