const express = require('express');
const { Pool } = require('pg');
const dotenv = require('dotenv').config;

//Create express app
const app = express();
const port = 3000;

//Create Pool
const pool = new Pool({
    user: process.env.PSQL_USER,
    host: process.env.PSQL_HOST,
    database: process.env.PSQL_DATABASE,
    password: process.env.PSQL_PASSWORD,
    port: process.env.PSQL_PORT,
    ssl: {rejectUnauthorized: false}
});

//Add process hook to shutdown pool
process.on('SIGINT', function() {
    pool.end();
    console.log('Application Successfully Shutdown');
    process.exit(0);
});

app.set("view engine", "ejs");
app.use(express.static(__dirname + '/views'));

app.get('/', (req, res) => {
    const data = {name: 'Nick'};
    res.render('index',data);
});

app.get('/index', (req, res) => {
    
});

app.get('/', (req, res) => {
    res.send('Hi User!');
});


    
app.listen(port, () => {
console.log(`Example app listening at http://localhost:${port}`);
});