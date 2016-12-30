/*
 * This file exposes the paths to various directories in the project.
 * Currently, it uses the environment variables set in build.sh.
 */

const variablesToPopulate = ['ROOT',
                             'SRC',
                             'TESTS',
                             'HTML',
                             'CSS',
                             'IMG',
                             'ORGANISATION',
                             'APPNAME'];

const environment = {};

for (let path of variablesToPopulate) {
    environment[path] = process.env[path];
}


module.exports = environment;
