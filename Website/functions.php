<?php

function displayCustomerMenu() {
echo "<ul>
            <li><a href='customerHomePage.php'>Home</a></li>
            <li><a href='customerMerchantMap.php'>Merchant Map</a></li>
            <li><a href='customerAccountSettings.php'>Modify Account Settings</a></li>
            <li><a href='customerMerchantSettings.php'>Modify Merchant Settings</a></li>
            <li><a href='customerRecentActivities.php'>Recent Activities</a></li>
            <li><a href='customerCurrentLocation.php'>Input Current Location</a></li>
            <li><a href='MCMlogOUt.php'>Log out</a></li>
        </ul>";
}

function displayMerchantMenu() {
echo "<ul>
            <li><a href='merchantHomePage.php'>Home</a></li>
            <li><a href='merchantViewCharges.php'>View Charges</a></li>
            <li><a href='merchantAccountSettings.php'>Modify Account Settings</a></li>
            <li><a href='merchantCreateAd.php'>Create an Ad</a></li>
            <li><a href='merchantCreateCoupon.php'>Create a Coupon</a></li>
            <li><a href='merchantRecentTransactions.php'>Recent Activities</a></li>
            <li><a href='merchantViewNearbyCustomers.php'>View Nearby Customers</a></li>
            <li><a href='MCMlogOut.php'>Log out</a></li>
        </ul>";
}

function checkUserSatus($user, $userType) {
    if($user === "guestOfKimi" ||
            !$user ||
            $userType === "merchant")
        header("Location: index.php");
}
?>
