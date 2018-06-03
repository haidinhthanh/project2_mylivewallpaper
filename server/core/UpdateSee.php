<?php
include "ConectDataBase.php";
$LiveImageID=$_POST["LiveImageID"];
$SQLquery= "UPDATE liveimage SET numberOfSee = numberOfSee+1 WHERE liveImageID=$LiveImageID";
$conn->query($SQLquery);
?>