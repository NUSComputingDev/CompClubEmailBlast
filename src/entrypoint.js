
$('document').ready(function() {
    enableNavigation();
    loadAllTabs();
});


const TABS = new Set(['start', 'content', 'settings', 'send']);

function enableNavigation() {
    $("#region_navbar li").on("click", function() {
        const tabName = this.id.replace('nav_', '');

        highlightTabInNavigationBar(tabName);
        showTab(tabName);
    });
}

function highlightTabInNavigationBar(tabToHighlight) {
    $("li").removeClass("active");
    $(`#nav_${tabToHighlight}`).addClass("active");
}
function showTab(tabToShow) {
    for (let tabName of TABS) {
        const tabElement = $(`#tab_${tabName}`);
        if (tabName === tabToShow) tabElement.show();
        else                       tabElement.hide();
    }
}


function loadAllTabs() {
    loadStartTab();
    loadContentTab();
    loadSettingsTab();
    loadSendTab();
}

function loadStartTab() {
}
function loadContentTab() {
    createEditor('editor', 'monokai', 'xml');
}
function loadSettingsTab() {
}
function loadSendTab() {
}


function createEditor(editorId, themeName, languageName) {
    editor = ace.edit(editorId);
    editor.setTheme(`ace/theme/${themeName}`);
    editor.getSession().setMode(`ace/mode/${languageName}`);
}
