<?php

$regId = "APA91bFzZYTrcNsEhIM-7nj9gKRTkwQiKPLtoxwGCFNBbNtscgyA_YabUb_szUaKOmsdJlASlntW9N7nc8657Hnh_aK2V0ljKpDMpmPdEa2-r-C2-91_7bZ5tYCq7rjenWD3M99y0SgX";
$message = "New charge!";

include_once './GCM.php';

$gcm = new GCM();

$registatoin_ids = array($regId);
$message = array("price" => $message);

$result = $gcm->send_notification($registatoin_ids, $message);

echo $result;

?>
