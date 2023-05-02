const express = require('express');
const { Pool } = require('pg');
const bodyParser = require('body-parser');
const dotenv = require('dotenv').config({ path: './database.env' });
const my_path = require('path');
//const { Loader } = require('@googlemaps/js-api-loader');

//Create express app
const app = express();
const session = require('express-session');
const port =  process.env.PORT || 3000;

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
    console.log('\nApplication Successfully Shutdown');
    process.exit(0);
});

app.set("view engine", "ejs");
app.use(express.static(__dirname + '/views'));
app.use(session({
  resave: false,
  saveUninitialized: true,
  secret: 'SECRET' 
}));

app.get('/', (req, res) => {
  res.render('index', { username: req.session.user });
});

app.get('/order', (req, res) => {
  res.render('order', { username: req.session.user });
});

app.get('/get-price', async (req, res) => {
    try {
      const queryString = 'SELECT price FROM menu_meals ORDER BY id';
      const result = await pool.query(queryString);
      res.json(result.rows);
    } catch (error) {
      console.error('Error executing query', error);
      res.status(500).send('Internal server error');
    }
});

app.get('/get-drink-price', async (req, res) => {
  try {
    const queryString = 'SELECT price FROM menu_drinks ORDER BY id';
    const result = await pool.query(queryString);
    res.json(result.rows);
  } catch (error) {
    console.error('Error executing query', error);
    res.status(500).send('Internal server error');
  }
});

app.get('/get-latest-transaction', async (req, res) => {
  try {
    const queryString = 'SELECT id FROM transactions ORDER BY id DESC LIMIT 1';
    const result = await pool.query(queryString);
    res.json(result.rows);
  } catch (error) {
    console.error('Error executing query', error);
    res.status(500).send('Internal server error');
  }
});


// Configure body-parser middleware
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.post('/insert-query', async (req, res) => {
  try {
    console.log(req.body);
    const { my_query } = req.body;
    const queryString = `${my_query}`;
    await pool.query(queryString);
    res.status(200).send('Query inserted successfully');
  } catch (error) {
    console.error('Error executing query', error);
    res.status(500).send('Internal server error');
  }
});

app.listen(port, () => {
console.log(`App listening at http://localhost:${port}`);
});


//Passport for OAuth 
//(see https://www.loginradius.com/blog/engineering/google-authentication-with-nodejs-and-passportjs/)
const passport = require('passport');

app.use(passport.initialize());
app.use(passport.session());

app.set('view engine', 'ejs');

passport.serializeUser(function(user, cb) {
  cb(null, user);
});

passport.deserializeUser(function(obj, cb) {
  cb(null, obj);
});

/*  Google AUTH  */

const GoogleStrategy = require('passport-google-oauth').OAuth2Strategy;
const GOOGLE_CLIENT_ID = '418130719038-tdrrf2gggsmkabeg10p0lgvq0cihv1b9.apps.googleusercontent.com';
const GOOGLE_CLIENT_SECRET = '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';
passport.use(new GoogleStrategy({
    clientID: GOOGLE_CLIENT_ID,
    clientSecret: GOOGLE_CLIENT_SECRET,
    callbackURL: "https://project-3-theta-panda-express.onrender.com/auth/google/callback"
  },
  function(accessToken, refreshToken, profile, done) {
      userProfile=profile;
      return done(null, userProfile);
  }
));
 
app.get('/auth/google', passport.authenticate('google', { scope : ['profile', 'email'] }));
 
app.get('/auth/google/callback', passport.authenticate('google', { failureRedirect: '/error' }), function(req, res) {
  req.session.user = { user: req.user };
  //console.log(req.session.user);
  res.redirect('/');
});