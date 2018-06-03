<?php
include "ConectDataBase.php";
$LiveImageID=$_POST["LiveImageID"];
$SQLquery= "UPDATE liveimage SET numberOfDowload = numberOfDowload+1 WHERE liveImageID=$LiveImageID";
$conn->query($SQLquery);
?>