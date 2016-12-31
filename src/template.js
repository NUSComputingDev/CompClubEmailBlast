/*
 * This is the module that handles all the templates in the html directory.
 */

const pug = require('pug');
const fs = require('fs');


const pugOptions = {
    pretty: true
};

let compiledFiles = new Set();


function generateHtml(fileNameWithoutExtension, localObjectToExpose) {
    const pathToSourcePugFile = `${process.env.HTML}/${fileNameWithoutExtension}.pug`;
    const pathToDestinationHtmlFile = `${process.env.TMP}/${fileNameWithoutExtension}.html`;

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
        const pathToFileToDelete = `${process.env.TMP}/${fileNameWithoutExtension}.html`;

        fs.unlink(pathToFileToDelete, (err, stats) => {
            if (err) {
                console.log(`An error occured while cleaning ${fileNameWithoutExtension}.html`);
            } else {
                compiledFiles.delete(fileNameWithoutExtension);
            }
        });
}


module.exports = {
    generateHtml,
    deleteHtml
};
