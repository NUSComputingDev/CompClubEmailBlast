# CompClubEmailBlast #

`CompClubEmailBlast` is designed to automate the process of sending e-mail blast for NUS Computing Club. It uses the e-mail blast template for the Club.

There is no error checking, the inputs are assumed to be correct.

## Assumptions ##

The programme takes many assumptions. Some of them are:
<ol>
<li>The inputs are well formed as expected by the programme, there is no error checking done by the programme</li>
<li>For every e-mail item, there's at least a title; the image, text, and link are optional</li>
<li>The information on the no. of items, titles, and links must be available in `main.txt` in the folder that contains all the contents. Each item is listed in `main.txt` chronologically. A title is needed for each item while the link&link-text pair is optional 
<li>Text files are named as `txt`<i><b>`i`</b></i>`.html` where <i><b>`i`</b></i> is a number greater than `1`</li>
<li>Image files are named as `img`<i><b>`i`</b></i>`.html` where <i><b>`i`</b></i> is a number greater than `1`</li>
</ol>

## Email Template ##

The e-mail template can be viewed as consisting of the following:
<ol>
<li>Header</li>
<li>Content Item(s), each with:
    <ol>
    <li>Title</li>
    <li>Image(optional)</li>
    <li>Text(optional)</li>
    <li>Link(optional)</li>
    </ol>
</li>
<li>Footer</li>
</ol>


## Dependencies ##

These dependencies are included in the programme
<ol>
<li>JavaMail API (`https://java.net/projects/javamail/pages/Home`)</li>
</ol>

## Sample Instructions ##

These steps will demonstrate the basic functionality of the programme using the sample files
<ol>
<li>update `samplePrivateInfo.txt` file with the required information</li>
<li>Run `CCmailer` using the 4 arguments: `samplePrivateInfo.txt sample/ sample/ sampleTest.html` usign Eclipse IDE <b>OR</b> place the relevant files(`samplePrivateInfo.txt` and the `sample/` folder) together, compile the required files and run CCmailer with the 4 arguments:
    <ul>
    <li>For example: `java CCmailer samplePrivateInfo.txt sample/ sample/ sampleTest.html`</li>
    </ul>
</li>
<li>Check that you receive the sample e-mail</li>
<li>The ouput html, `sampleTest.html`, is in the `sample/` folder</li>
</ol>
