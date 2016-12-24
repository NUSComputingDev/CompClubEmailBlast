#!/bin/bash -


export DIR_ROOT=`pwd`
export DIR_BUILD="lib"
export DIR_SRC="src"
export DIR_TESTS="tests"
export DIR_ASSETS="assets"


function performLaunch {
    node $DIR_BUILD/main.js
}
function performTests {
    exit 1
}
function performCompile {
    node_modules/.bin/babel $DIR_SRC -d $DIR_BUILD
}
function performClean {
    if [ -d $DIR_BUILD ]; then
        rm -rf $DIR_BUILD
    fi
}


case $1 in
    "launch")
        performLaunch
        exit 0
        ;;
    "tests")
        performTests
        exit 0
        ;;
    "compile")
        performCompile
        exit 0
        ;;
    "clean")
        performClean
        exit 0
        ;;
    *)
        :
esac
