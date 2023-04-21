//Global variables
let order = [];
let order_sec = [];
let price = [];
let items = [];


/**
 * Resize Functions
 * These functions are used to change the size of all elements on the website.
 * This helps with accessibility.
 *
 */

function resize_def(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name. 
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "order_style.css";
    if (fileName != "order_style.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }


    //store new name
    localStorage.setItem("styleSheet", newFileName);
}

function resize_large(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name. 
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "style_big.css";
    if (fileName != "style_big.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }


    //store new name
    localStorage.setItem("styleSheet", newFileName);
}

function resize_huge(){
    //Get style element by ID (hint: getElementById)
    var style_el = document.getElementById("css");
    //Check the current stylesheet file name. 
    var fileName = style_el.getAttribute("href");
    //Determine new stylesheet file name
    var newFileName = "style_huge.css";
    if (fileName != "style_huge.css"){
        // replace stylesheet with new stylesheet
        style_el.setAttribute("href", newFileName);
    }


    //store new name
    localStorage.setItem("styleSheet", newFileName);
}


window.onload = function(){
        // get stylesheet name from local storage hint: localStorage.getItem(name)
        var style_element = localStorage.getItem("styleSheet");
        
        //Avoid null css file when first booting website
        if ((style_element != "order_style.css") && (style_element != "style_big.css") && (style_element != "style_huge.css")){
            style_element = "order_style.css";
        }
        // get html style element by ID
        var element = document.getElementById("css");
        // replace href attribute of html element.
        element.setAttribute("href", style_element);
}

//Other Functions
/*function create_item(item){
    if (item == 'Bowl'){
        order_sec = ['entree1', 'side1', 'side2'];
    }
    else if (item == 'Plate'){
        order_sec = ['entree1', 'entree2', 'side1', 'side2'];
    }
    else if (item == 'Bigger Plate'){
        order_sec = ['entree1', 'entree2', 'entree3', 'side1', 'side2'];
    }
    else {
        
    }
}*/
function print_order(){
    let OLen = order.length;
        
    let text = "Order Items:<ul>";
    for (let i = 0; i < OLen; i++) {
      text += "<li>" + order[i] + "</li>";
    }
    text += "</ul>";
    
    document.getElementById("order").innerHTML = text;
}

function add_entree(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub_Meal" || order[0].includes("A_La_Carte_Entree_")) {
        order[1] = item;
    }
    else if (order[0] == "Plate") {
        order[1] = item;
        order[2] = item;
    }
    else if (order[0] == "Bigger_Plate") {
        order[1] = item;
        order[2] = item;
        order[3] = item;
    }
    else if (order[0] == "Family_Meal") {
        order[1] = item;
        order[2] = item;
        order[3] = item;
    }
    else if (order[0].includes("A_La_Carte_Side_")) {
        order[1] = "Please choose from: Sides";
    }
    else {
        order[0] = "First select from:"
        order[1] = "Regular Meals"
        order[2] = "or"
        order[3] = "A-La-Carte"
    }
    print_order();
}

function add_side(order, item){
    if (order[0] == "Bowl" || order[0] == "Cub_Meal") {
        order[2] = item;
    }
    else if (order[0] == "Plate") {
        order[3] = item;
        order[4] = item;
    }
    else if (order[0] == "Bigger_Plate") {
        order[4] = item;
        order[5] = item;
    }
    else if (order[0] == "Family_Meal") {
        order[4] = item;
        order[5] = item;
    }
    else if (order[0].includes("A_La_Carte_Side_")) {
        order[1] = item;
    }
    else if (order[0].includes("A_La_Carte_Entree_")) {
        order[1] = "Please choose from: Entrees";
    }
    else {
        order[0] = "First select from:"
        order[1] = "Regular Meals"
        order[2] = "or"
        order[3] = "A-La-Carte"
    }
    print_order();
}

function add_appetizer(order, item) {
    order[4] = item;
    print_order();
}

function add_drink(order, item) {
    order[5] = item;
    print_order();
}

function set_meal(order, item) {
    clear_order();
    order[0] = item;
    print_order();
}

function clear_order(){
    if (order[0] == '') order = [];
    for (let i = 0; i < order.length; i++) {
        order[i] = [''];
    } 
}

function add_to_price(order, item){
    price.push(item);
}

function print_price(){
    let PLen = price.length;
        
    let text = "Price:<ul>";
    for (let i = 0; i < PLen; i++) {
      text += "<li>" + price[i] + "</li>";
    }
    text += "</ul>";
    
    document.getElementById("price").innerHTML = text;
}

function clear_price(){
    price = [];
}

function calc_total(price){
    let total = 0;

    for (let i = 0; i < price.length; i++){
        total += price[i];
    }

    return total;
}