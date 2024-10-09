#!/usr/bin/env sh
SCRIPT_DIR=$(CDPATH="" cd -- "$(dirname -- "$0")" && pwd)

if [ -f "$SCRIPT_DIR/dev_env.sh" ]; then
    . "$SCRIPT_DIR/dev_env.sh"
else
    echo "JWT environment file not found. Please run setup.sh first."
    exit 1
fi

if [ -z "$SMALLRYE_JWT_SIGN_KEY_LOCATION" ] || [ -z "$MP_JWT_VERIFY_PUBLICKEY_LOCATION" ]; then
    echo "JWT key environment variables are not set. Please run setup.sh first."
    exit 1
fi

(cd ../../rest-client-quickstart ; ./mvnw quarkus:dev)
