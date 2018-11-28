<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

$id_penjual = $_POST['id_penjual'];

if($result = $db->query("SELECT * FROM `dagangan` INNER JOIN jenis_ikan on dagangan.id_jenis_ikan = jenis_ikan.id_jenis_ikan INNER JOIN penjual on dagangan.id_penjual = penjual.id_penjual WHERE penjual.id_penjual = '$id_penjual'")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["id_dagangan"] = $row->id_dagangan;
			$data["id_penjual"] = $row->id_penjual;
			$data["nama_penjual"] = $row->nama_penjual;
			$data["id_jenis_ikan"] = $row->id_jenis_ikan;
			$data["nama_ikan"] = $row->nama_ikan;
			$data["berat_tersedia"] = $row->berat_tersedia;
			$data["harga_per_kg"] = $row->harga_per_kg;
			$data["foto"] = "http://203.153.21.11/app/onfish/img/ikan_dagangan/".$row->foto_dagangan;
			$data["deskripsi"] = $row->deskripsi;
			$data["no_hp"] = $row->no_hp;
					
			
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