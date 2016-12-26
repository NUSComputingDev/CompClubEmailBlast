/*
 * This file exposes the paths to various directories in the project.
 * Currently, it uses the environment variables set in build.sh.
 */

const pathsToPopulate = ['ROOT',
                         'SRC',
                         'TESTS',
                         'HTML',
                         'CSS',
                         'JS',
                         'IMG'];

const pathObj = {};

for (let path of pathsToPopulate) {
    pathObj[path] = process.env[path];
}

module.exports = pathObj;
