/*
 * This is the module that handles all the templates in the html directory.
 */

const env = require(`${process.env['SRC']}/environment`);

const pug = require('pug');
const fs = require('fs');


const pugOptions = {
    pretty: true
};

const rendererFiles = ['debug.entrypoint',
                       'entrypoint'];

let compiledFiles = new Set();


function generateRendererHtml() {
    for (let file of rendererFiles) {
        generateHtml(file, {env});
    }
}

function deleteRendererHtml() {
    for (let file of rendererFiles) {
        deleteHtml(file, {env});
    }
}


function generateHtml(fileNameWithoutExtension, localObjectToExpose) {
    const pathToSourcePugFile = `${env.HTML}/${fileNameWithoutExtension}.pug`;
    const pathToDestinationHtmlFile = `${env.HTML}/${fileNameWithoutExtension}.html`;

    const compiledFile = pug.compileFile(pathToSourcePugFile, pugOptions);
    const renderedFile = compiledFile(localObjectToExpose);

    fs.writeFile(pathToDestinationHtmlFile, renderedFile, (err) => {
        if (err) {
            console.log(`An error occured while compiling ${fileNameWithoutExtension}.pug.`);
        } else {
            compiledFiles.add(fileNameWithoutExtension);
        }
    });
}

function deleteHtml(fileNameWithoutExtension) {
        const pathToFileToDelete = `${env.HTML}/${fileNameWithoutExtension}.html`;

        fs.unlink(pathToFileToDelete, (err, stats) => {
            if (err) {
                console.log(`An error occured while cleaning ${fileNameWithoutExtension}.html`);
            } else {
                compiledFiles.delete(fileNameWithoutExtension);
            }
        });
}


module.exports = {
    generateRendererHtml,
    deleteRendererHtml,
    generateHtml,
    deleteHtml
};
