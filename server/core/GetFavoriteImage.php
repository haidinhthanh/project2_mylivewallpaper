<?php
include "ConectDataBase.php";
$liveImageID=$_POST["liveImageID"];
$SQLquery = "SELECT * from liveimage where liveImageID=$liveImageID ";
$data= $conn->query($SQLquery);
$Array_Catagory= array();
if($data->num_rows>0){
	while($row = $data->fetch_assoc() ) {
			array_push($Array_Catagory,new LiveImage($row['liveImageID'],$row['CatagoryID'],$row['nameLiveImage'],$row['urlLinkImage'],$row['numberOfSee'],$row['numberOfDowload'],$row['UrlGIFImage'],$row['DayUpdate']));
	 	}
	}
$conn->close();
echo json_encode($Array_Catagory);
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