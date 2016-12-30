/*
 * This is the module that handles all the templates in the html directory.
 */

const dir = require(`${process.env['SRC']}/directory_paths`);

const pug = require('pug');
const fs = require('fs');


const pugOptions = {
    pretty: true
};

const rendererFiles = ['debug.entrypoint',
                       'blast.entrypoint'];

let compiledFiles = new Set();


function generateHtmlFromPug(fileNameWithoutExtension, localObjectToExpose) {
    const pathToSourcePugFile = `${dir.HTML}/${fileNameWithoutExtension}.pug`;
    const pathToDestinationHtmlFile = `${dir.HTML}/${fileNameWithoutExtension}.html`;

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

function compileRendererHtml() {
    for (let file of rendererFiles) {
        generateHtmlFromPug(file, {dir});
    }
}

function cleanGeneratedHtml() {
    for (let fileNameWithoutExtension of compiledFiles) {
        const pathToFileToDelete = `${dir.HTML}/${fileNameWithoutExtension}.html`;

        fs.unlink(pathToFileToDelete, (err, stats) => {
            if (err) {
                console.log(`An error occured while cleaning ${fileNameWithoutExtension}.html`);
            }
        });
    }

    compiledFiles.clear();
}


module.exports = {
    generateHtmlFromPug,
    compileRendererHtml,
    cleanGeneratedHtml
};
