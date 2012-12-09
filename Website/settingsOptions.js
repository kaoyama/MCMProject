/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function noAdsDisplay() {
    document.getElementById("adSection").innerHTML = "<h3>Advertisement \n\
Options</h3>\n\
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
             if (listOfMerchants[i] != "" && listOfMerchants[i] != null)
                {
            htmlString += "<label for='" + listOfMerchants[i] + "'>" + 
                listOfMerchants[i] + "</label>";
            htmlString += "<input type='checkbox' name='" + listOfMerchants[i] 
                + "' />";
                }
        }
           htmlString += "</div>";

   document.getElementById("adSection").innerHTML = htmlString;
   
}
function allAds() {
    document.getElementById("adSection").innerHTML = "<h3>Advertisement \n\
Options</h3><label> You are receiving ads from the following merchants.\n\
</label>";
}
function noCouponsDisplay() {
    document.getElementById("couponSection").innerHTML = "<h3>Coupon \n\
Options</h3><label> You are not receiving any coupons. </label>";
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
<form name='selectMerchants' method='POST' \n\
<div id='merchantList'>";
    
    for (var i=0;i<listOfMerchants.length;i++)
    {
        if (listOfMerchants[i] != "" && listOfMerchants[i] != null)
        {
            htmlString += "<label for='" + listOfMerchants[i] + "'>" + 
            listOfMerchants[i] + "</label>";
            htmlString += "<input type='checkbox' name='" + listOfMerchants[i] + 
            "' />";
        }
    }
    htmlString += "</div>";
    //add save button to string
    htmlString += "<br/><input type='submit' value='Save' onclick='?run=saveCouponData'>\n\
                    </form>";
    document.getElementById("couponSection").innerHTML = htmlString;
}
function allCoupons() {
    document.getElementById("couponSection").innerHTML = "<h3>Coupon \n\
Options</h3><label> You are receiving coupons from the following merchants. \n\
</label>";
}

function buttonState() {
    var stuff = document.getElementById("localization");
    if(stuff.checked)
    {
        document.getElementById("noAds").disabled = false;
        document.getElementById("someAds").disabled = false;
        document.getElementById("allAds").disabled = false;
        document.getElementById("noCoupons").disabled = false;
        document.getElementById("someCoupons").disabled = false;
        document.getElementById("allCoupons").disabled = false;
    }
    else   
    {
        document.getElementById("noAds").disabled = true;
        document.getElementById("someAds").disabled = true;
        document.getElementById("allAds").disabled = true;
        document.getElementById("noCoupons").disabled = true;
        document.getElementById("someCoupons").disabled = true;
        document.getElementById("allCoupons").disabled = true;
    }
    
}