/*
 * This is the entry point for Electron.
 */
const dir = require(`${process.env['SRC']}/directory_paths`);

const path = require('path');
const url = require('url');
const templateCompiler = require(`${dir.SRC}/template_compiler`);
const {app, BrowserWindow} = require('electron');


let win = null;

const pathOfPageToDisplay = path.join(dir.HTML, 'blast.entrypoint.html');
const urlOfPageToDisplay = url.format({
    pathname: pathOfPageToDisplay,
    protocol: 'file',
    slashes:  true
});


app.on('ready', () => {
    templateCompiler.compileRendererHtml();
    win = new BrowserWindow({width: 1280, height: 1024});
    win.loadURL(urlOfPageToDisplay);
    win.webContents.openDevTools();
    win.on('closed', () => { win = null; });
});
app.on('window-all-closed', () => {
    app.quit();
    templateCompiler.cleanGeneratedHtml();
});
