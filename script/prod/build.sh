#!/bin/sh
export SMALLRYE_JWT_SIGN_KEY_LOCATION='/mnt/c/Users/Kevinwh/Documents/Github/Complex-systems-and-devops-gruppe-8/backend/script/dev/dev_private_key.pem'
export MP_JWT_VERIFY_PUBLICKEY_LOCATION='/mnt/c/Users/Kevinwh/Documents/Github/Complex-systems-and-devops-gruppe-8/backend/script/dev/dev_public_key.pem'

if [ -z "$SMALLRYE_JWT_SIGN_KEY_LOCATION" ] || [ -z "$MP_JWT_VERIFY_PUBLICKEY_LOCATION" ]; then
    echo "JWT key environment variables are not set. Please run setup.sh first."
    exit 1
fi

(cd ../../rest-client-quickstart ; ./mvnw clean package -Dquarkus.profile=prod)