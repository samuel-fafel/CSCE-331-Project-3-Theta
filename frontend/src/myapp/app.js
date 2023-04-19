const express = require('express');
const { Pool } = require('pg');
const dotenv = require('dotenv').config;

//Create express app
const app = express();
const port = 3000;

//Create Pool
const pool = new Pool({
    user: 'csce315331_theta_master', //process.env.PSQL_USER,
    host: 'csce-315-db.engr.tamu.edu', //process.env.PSQL_HOST,
    database: 'csce315331_theta', //process.env.PSQL_DATABASE,
    password: '3NHS', //process.env.PSQL_PASSWORD,
    port: 5432, //process.env.PSQL_PORT,
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

async function getPrice(itemId) {
    try {
      const result = await pool.query(`SELECT price FROM products WHERE id = `, [itemId]);
      console.log(`Price for item ${itemId}: ${result.rows[0].price}`);
    } catch (error) {
      console.error('Error executing query', error);
    }
}

//Add process hook to shutdown pool
process.on('SIGINT', function() {
    pool.end();
    console.log('Application Successfully Shutdown');
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