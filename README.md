# CompClubEmailBlast #

`CompClubEmailBlast` is designed to automate the process of sending e-mail blast for NUS Computing Club. It uses the e-mail blast template for the Club.

There is no error checking, the inputs are assumed to be correct.

## Assumptions ##

The programme takes many assumptions. Some of them are:

1. The inputs are well formed as expected by the programme, there is no error checking done by the programme
2. For every e-mail item, there's at least a title; the image, text, and link are optional
3. The information on the no. of items, titles, and links must be available in `main.txt` in the folder that contains all the contents. Each item is listed in `main.txt` chronologically. A title is needed for each item while the link&link-text pair is optional 
4. Text files are named as `txti.html` where `i` is a number greater than `1`
5. Image files are named as `imgi.html` where `i` is a number greater than `1`

## Email Template ##

The e-mail template can be viewed as consisting of the following:

1. Header
2. Content Item(s), each with:
   * Title
   * Image(optional)
   * Text(optional)
   * Link(optional)
3. Footer

## Dependencies ##

These dependencies are included in the programme

* JavaMail API (`https://java.net/projects/javamail/pages/Home`)


## Sample Instructions ##

These steps will demonstrate the basic functionality of the programme using the sample files

1. update `samplePrivateInfo.txt` file with the required information
2. Run `CCmailer` using the 4 arguments: `samplePrivateInfo.txt sample/ sample/ sampleTest.html` using Eclipse IDE <b>OR</b> place the relevant files(`samplePrivateInfo.txt` and the `sample/` folder) together, compile the required files and run CCmailer with the 4 arguments:
   * For example, `java CCmailer samplePrivateInfo.txt sample/ sample/ sampleTest.html`
3. Check that you receive the sample e-mail
4. The ouput html, `sampleTest.html`, is in the `sample/` folder
