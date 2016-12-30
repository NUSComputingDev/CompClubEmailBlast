/*
 * This is the entry point for Electron.
 */
const env = require(`${process.env['SRC']}/environment`);

const path = require('path');
const url = require('url');
const template = require(`${env.SRC}/template`);
const {app, BrowserWindow} = require('electron');


let win = null;

const pathOfPageToDisplay = path.join(env.HTML, 'entrypoint.html');
const urlOfPageToDisplay = url.format({
    pathname: pathOfPageToDisplay,
    protocol: 'file',
    slashes:  true
});


app.on('ready', () => {
    template.generateRendererHtml();
    win = new BrowserWindow({width: 1280, height: 1024});
    win.loadURL(urlOfPageToDisplay);
    win.webContents.openDevTools();
    win.on('closed', () => { win = null; });
});
app.on('window-all-closed', () => {
    app.quit();
    template.deleteRendererHtml();
});
