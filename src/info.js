const dir = require(`${process.env['SRC']}/directory_paths`);


const nodeVersion = process.versions.node;
const chromeVersion = process.versions.chrome;
const electronVersion = process.versions.electron;

$('document').ready(() => {
    $('#node_version').html(nodeVersion);
    $('#chrome_version').html(chromeVersion);
    $('#electron_version').html(electronVersion);

    $('#project_dir').html(dir.ROOT);
    $('#sources_dir').html(dir.SRC);
    $('#tests_dir').html(dir.TESTS);
    $('#html_dir').html(dir.HTML);
    $('#css_dir').html(dir.CSS);
    $('#images_dir').html(dir.IMG);
});
