## Running scripts

Scripts should always be executed from the `backend/script/<prod|test|dev>` directory.

This is due to the nature of relative directories.

```bash
[user@domain:/.../backend/script]$ ./prod/build.sh
# FAILS
# ./prod/build.sh: line 2: cd: ../../rest-client-quickstart: No such file or directory
# ./prod/build.sh: line 2: ./mvnw: No such file or directory
``` 

```bash
[user@domain:/.../backend/script]$ cd prod

[user@domain:/.../backend/script/prod]$ ./build.sh
# SUCCEEDS
# [INFO] Scanning for projects...
# [INFO]
# ...
```

## Script categorization and order

There are three groups of scripts.

1. `prod/`

    These scripts should facilitate deployment. The prod scripts compile a native micro Dockerfile to be deployed via docker-compose.

    Order:
      1. `build.sh`
      2. `run.sh`
      4. `stop.sh`

2. `test/`

    These scripts should facilitate testing (for fast local testing, see `dev/`). The test scripts create a quick jvm Dockerfile to be tested against.
    
    Note: the tests are skipped during the build.

    Order:
      1. `build.sh`
      2. `test.sh`

3. `dev/`

    These scripts should faciliate development. The dev scripts run in the terminal.

    Both `dev.sh` and `test.sh` allow for testing, with the latter being faster.

    Please note that the `dev.sh` script allows for accessing endpoints via your browser while `test.sh` does not.

    The dev script also has a dev-UI. This can be accessed at the `/q/dev-ui/welcome` endpoint.

    By default the endpoints are available via `localhost:8080`.

    Order:
      1. `setup.sh`
      2. `test.sh` | `dev.sh`

