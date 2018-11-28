<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

$q_pencarian = $_POST['q_pencarian'];

if($result = $db->query("SELECT * FROM dagangan inner join petani on dagangan.id_petani = petani.id_petani inner join kategori_ikan on dagangan.id_kategori_ikan = kategori_ikan.id_kategori_ikan WHERE kategori_ikan.nama_ikan like '%$q_pencarian%'")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["id_dagangan"] = $row->id_dagangan;
			$data["id_petani"] = $row->id_petani;
			$data["nama_petani"] = $row->nama_petani;
			$data["id_kategori_ikan"] = $row->id_kategori_ikan;
			$data["nama_ikan"] = $row->nama_ikan;
			$data["berat_tersedia"] = $row->berat_tersedia;
			$data["harga_per_kg"] = $row->harga_per_kg;
			$data["foto"] = $row->foto_dagangan;
					
			
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