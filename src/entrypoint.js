
$('document').ready(function() {
    enableNavigation();
    loadAllTabs();
});


const TABS = new Set(['briefing', 'content', 'authentication', 'send']);

function enableNavigation() {
    $('#region_navbar li').on('click', function() {
        const tabName = this.id.replace('nav_', '');

        highlightTabInNavigationBar(tabName);
        showTab(tabName);
    });
}

function highlightTabInNavigationBar(tabToHighlight) {
    const allElements = $('#region_navbar li');
    const elementToHighlight = $(`#nav_${tabToHighlight}`);

    allElements.removeClass("active");
    elementToHighlight.addClass("active");
}
function showTab(tabToShow) {
    const allElements = $('#region_main > div');
    const elementToShow = $(`#tab_${tabToShow}`);

    allElements.hide();
    elementToShow.show();
}


function loadAllTabs() {
    loadBriefingTab();
    loadContentTab();
    loadAuthenticationTab();
    loadSendTab();
}

function loadBriefingTab() {
}
function loadContentTab() {
    createEditor('editor', 'monokai', 'json');
}
function loadAuthenticationTab() {
}
function loadSendTab() {
}


function createEditor(editorId, themeName, languageName) {
    editor = ace.edit(editorId);
    editor.setTheme(`ace/theme/${themeName}`);
    editor.getSession().setMode(`ace/mode/${languageName}`);
}
