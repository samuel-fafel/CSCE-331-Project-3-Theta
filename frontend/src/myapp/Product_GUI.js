const express = require('express');
const app = express();
const port = 3500;

app.set("view engine", "ejs");
app.use(express.static(__dirname + '/views'));

app.get('/', (req, res) => {
    const data = {name: 'Nathan'};
    res.render('productGUI',data);
});

app.get('/', (req, res) => {
    res.send('Hi User!');
});
    
app.listen(port, () => {
console.log(`Example app listening at http://localhost:${port}`);
});