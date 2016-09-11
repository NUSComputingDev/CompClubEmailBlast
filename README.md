# CompClubEmailBlast #

`CompClubEmailBlast` is designed to automate the process of sending e-mail blast for NUS Computing Club. It uses the e-mail blast template for the Club.

There is no error checking, the inputs are assumed to be correct.

## Assumptions ##

The programme takes many assumptions. Some of them are:

1. The inputs are well formed as expected by the programme, there is no error checking done by the programme
2. For every e-mail item, there's at least a title; the image, text, and link are optional
3. The information on the no. of items, titles, and links must be available in `main.txt` in the folder that contains all the contents. Each item is listed in `main.txt` chronologically. A title is needed for each item while the link&link-text pair is optional 
4. Text files are named as `txti.html` where `i` is a number greater than or equal to `1`
5. Image files are named as `imgi.html` where `i` is a number greater than or equal to `1`

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

## Argument inputs for CCmailer ##

`CCmailer` takes in 5 arguments:
  * name of the file containing private info (either relative path or static path)
  * input folder containing the inputs (ending with "/")
  * output folder to write the output to (ending with "/")
  * name of the output html file (This html file will be placed in the output folder)
  * type of e-mail (either emailBlast or acadAdvisory)

## Sample Instructions ##

These steps will demonstrate the basic functionality of the programme using the sample files

1. update `samplePrivateInfo.txt` file with the required information
2. remove `acadliaison-logo.png` from the sample folder unless you are test sending Academic Advisory
3. Run `CCmailer` using the 5 arguments: `samplePrivateInfo.txt sample/ sample/ sampleTest.html emailBlast` using Eclipse IDE <b>OR</b> for manual compilation, follow these steps:
   1. run `compile.sh` which will compile the files in the folder `src`. If you can't run it, ensure that you have the execution rights for the file
   2. Change to the `src` folder
   3. run CCmailer with the classpath and the 5 arguments: For example, `java -cp ".:../lib/javax.mail.jar" CCmailer ../../samplePrivateinfo.txt ../sample/ ../sample/ sampleTest.html emailBlast`
4. Check that you receive the sample e-mail
5. The ouput html, `sampleTest.html`, is in the `sample/` folder
