#!/bin/sh
export SMALLRYE_JWT_SIGN_KEY_LOCATION='/mnt/c/Users/Kevinwh/Documents/Github/Complex-systems-and-devops-gruppe-8/backend/script/dev/dev_private_key.pem'
export MP_JWT_VERIFY_PUBLICKEY_LOCATION='/mnt/c/Users/Kevinwh/Documents/Github/Complex-systems-and-devops-gruppe-8/backend/script/dev/dev_public_key.pem'

# Check if the environment variables are set
if [ -z "$SMALLRYE_JWT_SIGN_KEY_LOCATION" ] || [ -z "$MP_JWT_VERIFY_PUBLICKEY_LOCATION" ]; then
    echo "JWT key environment variables are not set. Please run setup.sh first."
    exit 1
fi

# Read the contents of the private key file
PRIVATE_KEY=$(cat "$SMALLRYE_JWT_SIGN_KEY_LOCATION")
if [ $? -ne 0 ]; then
    echo "Failed to read the private key file."
    exit 1
fi

# Read the contents of the public key file
PUBLIC_KEY=$(cat "$MP_JWT_VERIFY_PUBLICKEY_LOCATION")
if [ $? -ne 0 ]; then
    echo "Failed to read the public key file."
    exit 1
fi

# Export them as environment variables
export smallrye_jwt_sign_key="$PRIVATE_KEY"
export mp_jwt_verify_publickey="$PUBLIC_KEY"

# Run Docker Compose
docker-compose -f docker-compose.yml up -d
