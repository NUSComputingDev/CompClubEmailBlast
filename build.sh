#!/bin/bash -


###########################
## Important Directories ##
###########################

export DIR_ROOT=`pwd`
export DIR_BUILD="lib"
export DIR_SRC="src"
export DIR_TESTS="tests"
export DIR_ASSETS="assets"


#####################
## Build Functions ##
#####################

# NOTE: All build functions must exit with a appropriate status code.

function runLaunch {
    if [ -f $DIR_BUILD/main.js ]; then
        node $DIR_BUILD/main.js
        exit 0
    else
        echo "The entry point ${DIR_BUILD}/main.js was not found."
        exit 1
    fi
}

function runBackgroundLaunch {
    if [ -f $DIR_BUILD/main.js ]; then
        node $DIR_BUILD/main.js &
        echo $! > .server_pid # keep track of the server's PID in a PID file
        chmod 640 .server_pid # prevent accidental modification/deletion of the PID file
        echo "Server Process: $!"
        exit 0
    else
        echo "The entry point ${DIR_BUILD}/main.js was not found."
        exit 1
    fi
}

function runBackgroundStop {
    if [ -f ".server_pid" ]; then
        # PID file found
        kill $(cat .server_pid)
        rm .server_pid
    else
        # PID file not found
        echo "The .server_pid file is missing and may have been deleted."
        echo "If the server is still running, it must be killed manually."
    fi

    exit 0
}

function runTests {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function runCompile {
    node_modules/.bin/babel $DIR_SRC -d $DIR_BUILD
    exit $?
}

function runClean {
    if [ -d $DIR_BUILD ]; then
        rm -rf $DIR_BUILD
    fi

    exit 0
}


#####################################
## Command Line Parameter Handling ##
#####################################

case $1 in
    "launch")
        runLaunch
        ;;
    "bglaunch")
        runBackgroundLaunch
        ;;
    "bgstop")
        runBackgroundStop
        ;;
    "tests")
        runTests
        ;;
    "compile")
        runCompile
        ;;
    "clean")
        runClean
        ;;
    *)
        exit 0
esac
