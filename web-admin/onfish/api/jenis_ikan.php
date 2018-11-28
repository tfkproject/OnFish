<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

if($result = $db->query("SELECT * FROM jenis_ikan")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["id_jenis_ikan"] = $row->id_jenis_ikan;
			$data["nama_ikan"] = $row->nama_ikan;
			$data["foto"] = "http://103.111.86.246/app/onfish/img/ikan_jenis/".$row->foto;
					
			
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
