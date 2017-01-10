#!/bin/bash -


###########################
## Environment Variables ##
###########################

# NOTE: Use these whenever directory structure needs to be referenced.
#       This can be done in Node with `process.env`.
#       Don't redefine these variables somewhere in JS or anywhere else.
#       This is in line with the DRY principle of software engineering.

# NOTE: If sourcing this script, do so only from the project root.
#       Otherwise $ROOT will have the wrong value.

# NOTE: Branding is captured by the variables ORGANISATION and APPNAME.
#       It is possible to rebrand the app by just changing these two variables.

export ORGANISATION="NUS Students' Computing Club"
export APPNAME="Computing Club Email Blast"

export ROOT=`pwd`

export BIN=${ROOT}/node_modules/.bin
export LIB=${ROOT}/node_modules
export SRC=${ROOT}/src
export TESTS=${ROOT}/tests
export TMP=${ROOT}/tmp

export HTML=${SRC}/html
export CSS=${SRC}/css
export IMG=${SRC}/img


#####################
## Build Functions ##
#####################

# NOTE: All build functions must exit with a appropriate status code.

function doStart {
    createDirectoryIfNotExists ${TMP}
    startAppInElectron ${SRC}/main.js
    local appStartStatusCode=$?
    deleteDirectoryIfExists ${TMP}
    return ${appStartStatusCode}
}

function doPackage {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function doTest {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}

function doCompile {
    echo "NOT IMPLEMENTED YET"
    exit 1
}

function doClean {
    echo "NOT IMPLEMENTED YET!"
    exit 1
}


######################
## Helper Functions ##
######################

function createDirectoryIfNotExists {
    local dirToCreate=$1

    if [ ! -d ${dirToCreate} ]; then
        mkdir ${dirToCreate}
    fi
}

function deleteDirectoryIfExists {
    local dirToDelete=$1

    if [ -d ${dirToDelete} ]; then
        rm -rf ${dirToDelete}
    fi
}

function startAppInElectron {
    local entryPoint=$1

    if [ -f ${entryPoint} ]; then
        ${BIN}/electron ${entryPoint}
        return 0
    else
        echo "The entry point ${entryPoint} was not found."
        return 1
    fi
}

#####################################
## Command Line Parameter Handling ##
#####################################

case $1 in
    "start")
        doStart
        ;;
    "package")
        doPackage
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
