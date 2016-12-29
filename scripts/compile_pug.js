#!/usr/bin/env node
const dir = require(`${process.env['SRC']}/directory_paths`);

const pug = require('pug');
const fs = require('fs');


const filesToCompile = ['info'];

const pugOptions = {
    pretty: true
};

const pugLocals = {
    dir: dir
};


for (let file of filesToCompile) {
    let compiledFile = pug.compileFile(`${dir.HTML}/${file}.pug`, pugOptions);
    let renderedFile = compiledFile(pugLocals);
    fs.writeFile(`${dir.HTML}/${file}.html`, renderedFile, (err) => {
        if (err) {
            console.log(`An error occured while compiling ${file}.pug.`);
        }
    });
}
