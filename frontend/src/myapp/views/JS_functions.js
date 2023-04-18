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
    var newFileName = "style.css";
    if (fileName != "style.css"){
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
        if (style_element != "style.css"){
            style_element = "style.css";
        }
        // get html style element by ID
        var element = document.getElementById("css");
        // replace href attribute of html element.
        element.setAttribute("href", style_element);
}

//Other Functions