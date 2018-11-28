<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

$id_pelanggan = $_POST['id_pelanggan'];

if($result = $db->query("SELECT sum(harga_total) as total_bayar FROM item_beli where id_pelanggan = '$id_pelanggan'")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["total_bayar"] = $row->total_bayar;
					
			
			array_push($response["daftar"], $data);
		}
		
		$response["success"] = 1;
		
		// echoing JSON response
		echo json_encode($response);
	}
		
		$result->free();
} else {
    // no datas found
    $response["success"] = 0;
    $response["message"] = "Tidak ada data ditemukan";
    // echo no users JSON
    echo json_encode($response);
}
?>