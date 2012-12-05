/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function noAdsDisplay() {
    document.getElementById("adSection").innerHTML = "<h3>Advertisement Options</h3>\n\
<label> You are not receiving any ads. </label>";
}

function selectAds(merchantList) {
    var listOfMerchants = new Array();
    listOfMerchants = merchantList.split("#");
    var htmlString = "<h3>Advertisement Options</h3>\n\
<label for='adSortType'>Sort ads by:</label>\n\
<select name=sortType id=sortType>\n\
<option value='productType'>Product Type</option>\n\
<option value='alphaNum'>Alphabetical</option>\n\
<option value='distance'>Distance</option>\n\
</select>\n\
<br/>\n\
<div id='merchantList'>";
    
    for (var i=0;i<listOfMerchants.length;i++)
        {
            htmlString += "<label for='" + listOfMerchants[i] + "'>" + listOfMerchants[i] + "</label>";
            htmlString += "<input type='checkbox' name='" + listOfMerchants[i] + "' />";
        }
           htmlString += "</div>";

   document.getElementById("adSection").innerHTML = htmlString;
   
}
function allAds() {
    document.getElementById("adSection").innerHTML = "<h3>Advertisement Options</h3>\n\
<label> You are receiving ads from the following merchants.</label>";
}
function noCouponsDisplay() {
    document.getElementById("couponSection").innerHTML = "<h3>Coupon Options</h3>\n\
<label> You are not receiving any coupons. </label>";
}
function selectCoupons(merchantList) {
    var listOfMerchants = new Array();
    listOfMerchants = merchantList.split("#");
    var htmlString = "<h3>Advertisement Options</h3>\n\
<label for='couponSortType'>Sort Coupons by:</label>\n\
<select name=sortType id=sortType>\n\
<option value='productType'>Product Type</option>\n\
<option value='alphaNum'>Alphabetical</option>\n\
<option value='distance'>Distance</option>\n\
</select>\n\
<br/>\n\
<div id='merchantList'>";
    
    for (var i=0;i<listOfMerchants.length;i++)
        {
            if (listOfMerchants[i] != "" && listOfMerchants[i] != null)
                {
            htmlString += "<label for='" + listOfMerchants[i] + "'>" + listOfMerchants[i] + "</label>";
            htmlString += "<input type='checkbox' name='" + listOfMerchants[i] + "' />";
                }
        }
           htmlString += "</div>";

   document.getElementById("couponSection").innerHTML = htmlString;
}
function allCoupons() {
    document.getElementById("couponSection").innerHTML = "<h3>Coupon Options</h3>\n\
<label> You are receiving coupons from the following merchants. </label>";
}
