const dir = require(`${process.env['SRC']}/directory_paths`);

const template = require(`${dir.SRC}/template`);


template.generateHtml('edm', {
    items: [
        {
            title: 'Cats of SoC',
            image: 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Kitten_in_Rizal_Park%2C_Manila.jpg/300px-Kitten_in_Rizal_Park%2C_Manila.jpg',
            text: 'We interview some of the various feline inhabitants of our school, to find out their editor preference. It turns out that most cats prefer Vim over Emacs.'
        }
    ],
    logo: 'http://newsletters.nuscomputing.com/compclub-logo.png',
    date: '01 January 2017',
    header: 'Computing Club Newsletter'
});
