/*
 * This is the entry point for Electron.
 */
const path = require('path');
const url = require('url');
const template = require(`${process.env.SRC}/template`);
const {app, BrowserWindow} = require('electron');


const rendererFiles = ['debug.entrypoint',
                       'entrypoint'];
const config = require('./../config');

function generateRendererHtml() {
    for (let file of rendererFiles) {
        template.generateHtml(file, {config: config});
    }
}

function deleteRendererHtml() {
    for (let file of rendererFiles) {
        template.deleteHtml(file);
    }
}

const pathOfPageToDisplay = path.join(process.env.TMP, 'entrypoint.html');
const urlOfPageToDisplay = url.format({
    pathname: pathOfPageToDisplay,
    protocol: 'file',
    slashes:  true
});


let win = null;

app.on('ready', () => {
    generateRendererHtml();
    win = new BrowserWindow({
      width: 1280, 
      height: 1024,
      webPreferences: {
        nodeIntegration: true,
        contextIsolation: false  
      }
    });
    win.maximize();
    win.loadURL(urlOfPageToDisplay);
    win.webContents.openDevTools();
    win.on('closed', () => { win = null; });
});
app.on('window-all-closed', () => {
    app.quit();
    deleteRendererHtml();
});
