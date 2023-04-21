const express = require('express');
const { Pool } = require('pg');
const dotenv = require('dotenv').config({ path: './database.env' });

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

async function connect() {
    try {
      await pool.connect();
      console.log('Connected to database');
    } catch (error) {
      console.error('Error connecting to database', error);
    }
}
connect();

//Add process hook to shutdown pool
process.on('SIGINT', function() {
    pool.end();
    console.log('\nApplication Successfully Shutdown');
    process.exit(0);
});

app.set("view engine", "ejs");
app.use(express.static(__dirname + '/views'));

app.get('/', (req, res) => {
    res.render('index');
});

app.get('/order', (req, res) => {
    res.render('order');
});
    
app.listen(port, () => {
console.log(`Example app listening at http://localhost:${port}`);
});