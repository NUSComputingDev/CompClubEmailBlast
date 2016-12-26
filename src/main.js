/*
 * This is the entry point for Electron.
 */

const {app, BrowserWindow} = require('electron');

const path = require('path');
const url = require('url');
const dir = require('./js/directory_paths');


let win = null;

const pathOfPageToDisplay = path.join(dir.SRC, 'info.html');
const urlOfPageToDisplay = url.format({
    pathname: pathOfPageToDisplay,
    protocol: 'file',
    slashes:  true
});

app.on('ready', () => {
    win = new BrowserWindow({width: 1280, height: 1024});
    win.loadURL(urlOfPageToDisplay);
    win.webContents.openDevTools();
    win.on('closed', () => { win = null; });
});
app.on('window-all-closed', () => {
    app.quit();
});
