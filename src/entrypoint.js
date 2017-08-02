const nodemailer = require('nodemailer');
const scp2client = require('scp2');
const template = require(`${process.env.SRC}/template`);


$('document').ready(function() {
    enableNavigation();
    loadAllTabs();
});


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
    loadContentTab();
    loadImageTab();
    loadAuthenticationTab();
    loadSendTab();
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
function loadImageTab() {
    $('#upload_image').on('click', function() {
        const server = $('#img_server').val();
        const sourcePath = $('#img_path_src').val();
        const destPath = $('#img_path_dest').val();
        const username = $('#img_user').val();
        const password = $('#img_password').val();

        scp2client.scp(sourcePath, {
            host: server,
            username: username,
            password: password,
            path: destPath
        }, function(err) {
            if (err) {
                console.log(`An error occurred while sending ${sourcePath} to ${server}:${destPath}.`);
                console.log(err);
            } else {
                console.log(`The file ${sourcePath} has been uploaded successfully.`);
            }
        });
    });
}
function loadAuthenticationTab() {
}
function loadSendTab() {
    $('#blast').on('click', function() {
        swal({
            title: 'Are you sure?',
            text: "You won't be able to undo this blast!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Confirm send'
        }).then(function () {
            const from = {
                name: $('#send_from_name').val(),
                address: $('#send_from_email').val()
            };
            const to = {
                name: $('#send_to_name').val(),
                address: $('#send_to_email').val()
            };
            const bcc = $('#send_bcc').val();
            const replyTo = {
                name: $('#send_replyTo_name').val(),
                address: $('#send_replyTo_email').val()
            };
            const subject = $('#send_subject').val();
            const content = editorHtml.getValue();

            const host = $('#auth_host').val();
            const port = $('#auth_port').val();
            const isSecure = $('#auth_secure').prop('checked');
            const username = $('#auth_username').val();
            const password = $('#auth_password').val();

            const credentialObj = createCredentialObject(host, port, isSecure, username, password);
            const emailObj = createEmailObject(from, to, bcc, replyTo, subject, content);

            sendMail(credentialObj, emailObj);
        }, function (dismiss) {
            if (dismiss === 'cancel') {
                swal({
                    title: 'Cancelled',
                    text: "You will be back in edit mode.",
                    type: 'error'
                });
            }
        });
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

function createEmailObject(from, to, bcc, replyTo, subject, content) {
    return {
        from: from,
        to: to,
        bcc: bcc,
        replyTo: replyTo,
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
            swal({
              title: 'Error!',
              text: "Your mail might not have been sent. Press Ctrl+Shift+I to view the log.",
              type: 'error',
              confirmButtonText: 'OK'
            });
        } else {
            console.log(JSON.stringify(info));
            swal({
              title: 'Success!',
              text: "Your mail has been sent. Press Ctrl+Shift+I to view the log.",
              type: 'success',
              confirmButtonText: 'OK'
            });
        }
    });
}
