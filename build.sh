#!/bin/bash -


###########################
## Important Directories ##
###########################

# NOTE: Use these whenever directory structure needs to be referenced.
#       This can be done in Node with the `process` global.
#       Don't redefine these variables somewhere in JS or anywhere else.
#       This is in line with the DRY principle of software engineering.

export ROOT=`pwd`
export SRC=${ROOT}/src
export TESTS=${ROOT}/tests
export HTML=${SRC}/html
export CSS=${SRC}/css
export JS=${SRC}/js
export IMG=${SRC}/img


#####################
## Build Functions ##
#####################

# NOTE: All build functions must exit with a appropriate status code.

function doPackage {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function doStart {
    if [ -f ${SRC}/main.js ]; then
        ./node_modules/.bin/electron ${SRC}/main.js
        exit 0
    else
        echo "The entry point ${SRC}/main.js was not found."
        exit 1
    fi
}

function doTest {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function doCompile {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function doClean {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}


#####################################
## Command Line Parameter Handling ##
#####################################

case $1 in
    "package")
        doPackage
        ;;
    "start")
        doStart
        ;;
    "test")
        doTest
        ;;
    "compile")
        doCompile
        ;;
    "clean")
        doClean
        ;;
    *)
        # NOTE: DO NOT EXIT HERE!
        #       This is to allow sourcing of this script for env vars.
        :
esac