#!/usr/bin/env sh
SCRIPT_DIR=$(CDPATH="" cd -- "$(dirname -- "$0")" && pwd)

openssl genrsa -out "$SCRIPT_DIR/dev_private_key.pem" 2048
openssl rsa -in "$SCRIPT_DIR/dev_private_key.pem" -pubout -out "$SCRIPT_DIR/dev_public_key.pem"

echo "RSA key pair generated."
echo "Private key: $SCRIPT_DIR/dev_private_key.pem"
echo "Public key: $SCRIPT_DIR/dev_public_key.pem"

echo "export SMALLRYE_JWT_SIGN_KEY_LOCATION='$SCRIPT_DIR/dev_private_key.pem'" > "$SCRIPT_DIR/dev_env.sh"
echo "export MP_JWT_VERIFY_PUBLICKEY_LOCATION='$SCRIPT_DIR/dev_public_key.pem'" >> "$SCRIPT_DIR/dev_env.sh"

echo "To set the environment variables, run:"
echo "source $SCRIPT_DIR/dev_env.sh"
