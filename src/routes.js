/*
 * Route Configuration File
 */

export function setRoutes(app) {
    app.get('/', handleIndexPage);
}


function handleIndexPage(req, res) {
    res.send(process.env['DIR_ROOT']);
}
