<?php
include "ConectDataBase.php";
$Catagory=$_POST["Catagory"];
$SQLquery= "SELECT * from liveimage where CatagoryID=$Catagory";
$data= $conn->query($SQLquery);
$Array_LiveImage=array();
if($data->num_rows >0){
	while ($row= $data->fetch_assoc()) {
		array_push($Array_LiveImage, 
			new LiveImage($row['liveImageID'],$row['CatagoryID'],$row['nameLiveImage'],$row['urlLinkImage'],$row['numberOfSee'],$row['numberOfDowload'],$row['UrlGIFImage'],$row['DayUpdate']));
	}
}
echo json_encode($Array_LiveImage);
class LiveImage{
	function LiveImage($liveImageID,$CatagoryID,$nameLiveImage,$urlLinkImage,$numberOfSee,$numberOfDowload,$UrlGIFImage,$DayUpdate){
		$this->liveImageID=$liveImageID;
		$this->CatagoryID=$CatagoryID;
		$this->nameLiveImage=$nameLiveImage;
		$this->numberOfDowload=$numberOfDowload;
		$this->numberOfSee=$numberOfSee;
		$this->urlLinkImage=$urlLinkImage;
		$this->UrlGIFImage=$UrlGIFImage;
		$this->DayUpdate=$DayUpdate;
	}
}
?>