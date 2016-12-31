
const nodeVersion = process.versions.node;
const chromeVersion = process.versions.chrome;
const electronVersion = process.versions.electron;

$('document').ready(() => {
    $('#node_version').html(nodeVersion);
    $('#chrome_version').html(chromeVersion);
    $('#electron_version').html(electronVersion);

    $('#project_dir').html(process.env.ROOT);
    $('#sources_dir').html(process.env.SRC);
    $('#tests_dir').html(process.env.TESTS);
    $('#html_dir').html(process.env.HTML);
    $('#css_dir').html(process.env.CSS);
    $('#images_dir').html(process.env.IMG);
});
