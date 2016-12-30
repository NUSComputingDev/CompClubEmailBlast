const env = require(`${process.env['SRC']}/environment`);


const nodeVersion = process.versions.node;
const chromeVersion = process.versions.chrome;
const electronVersion = process.versions.electron;

$('document').ready(() => {
    $('#node_version').html(nodeVersion);
    $('#chrome_version').html(chromeVersion);
    $('#electron_version').html(electronVersion);

    $('#project_dir').html(env.ROOT);
    $('#sources_dir').html(env.SRC);
    $('#tests_dir').html(env.TESTS);
    $('#html_dir').html(env.HTML);
    $('#css_dir').html(env.CSS);
    $('#images_dir').html(env.IMG);
});
