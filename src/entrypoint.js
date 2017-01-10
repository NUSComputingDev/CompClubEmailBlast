const nodemailer = require('nodemailer');
const template = require(`${process.env.SRC}/template`);


$('document').ready(function() {
    enableNavigation();
    loadAllTabs();
});


const TABS = new Set(['briefing', 'content', 'authentication', 'send']);

let editor = null;
let editorHtml = null;


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
    // one for the EDM parameters in JSON, one for the generated EDM HTML
    editor = createEditor('editor_blast', 'monokai', 'json');
    editorHtml = createEditor('editor_blast_html', 'monokai', 'html');

    // ensure that editors don't scroll while typing
    editor.$blockScrolling = true;
    editorHtml.$blockScrolling = true;

    // live updates
    editor.getSession().on('change', function(e) {
        const edmParametersJsonString = editor.getValue();

        try {
            const edmParameters = JSON.parse(edmParametersJsonString);
            const compiledEdmHtml = template.compileTemplate('edm', edmParameters);
            editorHtml.setValue(compiledEdmHtml);
        } catch (e) {
            // malformed/incomplete JSON that cannot be used to generate the EDM HTML
            // this is a normal occurrence while the user is typing
            // just ignore
        }
    });

}
function loadAuthenticationTab() {
}
function loadSendTab() {
    $('#blast').on('click', function() {
        const from = $('#send_from').val();
        const to = $('#send_to').val();
        const subject = $('#send_subject').val();
        const content = editorHtml.getValue();

        const host = $('#auth_host').val();
        const port = $('#auth_port').val();
        const isSecure = $('#auth_secure').prop('checked');
        const username = $('#auth_username').val();
        const password = $('#auth_password').val();

        const credentialObj = createCredentialObject(host, port, isSecure, username, password);
        const emailObj = createEmailObject(from, to, subject, content);

        sendMail(credentialObj, emailObj);
    });
}


function createEditor(editorId, themeName, languageName) {
    const editor = ace.edit(editorId);
    editor.setTheme(`ace/theme/${themeName}`);
    editor.getSession().setMode(`ace/mode/${languageName}`);
    return editor;
}

function createCredentialObject(host, port, isSecure, username, password) {
    return {
        host: host,
        port: port,
        secure: isSecure, // use SSL
        auth: {
            user: username,
            pass: password
        }
    };
}

function createEmailObject(from, to, subject, content) {
    return {
        from: from,
        to: to,
        subject: subject,
        html: content
    };
}

function sendMail(credentialObject, emailObject) {
    const transporter = nodemailer.createTransport(credentialObject);

    transporter.sendMail(emailObject, (err, info) => {
        if (err) {
            console.log('An error occurred.');
            console.log(err);
        } else {
            console.log(JSON.stringify(info));
        }
    });
}
