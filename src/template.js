/*
 * This is the module that handles all the templates in the html directory.
 */

const pug = require('pug');
const fs = require('fs');


const pugOptions = {
    pretty: true
};

let generatedFiles = new Set();


function generateHtml(fileNameWithoutExtension, localObjectToExpose) {
    const renderedHtmlString = compileTemplate(fileNameWithoutExtension, localObjectToExpose);
    const pathToDestinationHtmlFile = `${process.env.TMP}/${fileNameWithoutExtension}.html`;

    fs.writeFile(pathToDestinationHtmlFile, renderedHtmlString, (err) => {
        if (err) {
            console.log(`An error occured while compiling ${fileNameWithoutExtension}.pug.`);
        } else {
            generatedFiles.add(fileNameWithoutExtension);
        }
    });
}

function deleteHtml(fileNameWithoutExtension) {
        const pathToFileToDelete = `${process.env.TMP}/${fileNameWithoutExtension}.html`;

        fs.unlink(pathToFileToDelete, (err, stats) => {
            if (err) {
                console.log(`An error occured while cleaning ${fileNameWithoutExtension}.html`);
            } else {
                generatedFiles.delete(fileNameWithoutExtension);
            }
        });
}


function compileTemplate(fileNameWithoutExtension, localObjectToExpose) {
    const pathToSourcePugFile = `${process.env.HTML}/${fileNameWithoutExtension}.pug`;

    const compiledFile = pug.compileFile(pathToSourcePugFile, pugOptions);
    const renderedHtmlString = compiledFile(localObjectToExpose);

    return renderedHtmlString;
}


module.exports = {
    generateHtml,
    deleteHtml,
    compileTemplate
};
