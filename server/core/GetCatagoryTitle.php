<?php
include "ConectDataBase.php";

$SQLquery = "SELECT * from catagory";
$data= $conn->query($SQLquery);
$Array_Catagory= array();
if($data->num_rows >0){
	while($row = $data->fetch_assoc() ) {
			array_push($Array_Catagory, new catagory(
			$row['CatagoryID'], $row['CatagoryName'], $row['CatagoryImageUrl']));
	 	}
	}
$conn->close();
echo json_encode($Array_Catagory);
class catagory{
	function catagory($catagoryID,$catagoryName,$catagoryImageURL){
		$this->catagoryID=$catagoryID;
		$this->catagoryName=$catagoryName;
		$this->catagoryImageURL=$catagoryImageURL;
		}
	}
?>

