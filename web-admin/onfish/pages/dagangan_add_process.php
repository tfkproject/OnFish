<?php
include("koneksi.php");

if(isset($_POST)){
	$id_petani = $_POST['id_petani'];
	$id_kategori_ikan = $_POST['id_kategori_ikan'];
	$harga_per_kg = $_POST['harga_per_kg'];
	$file_foto = $_FILES['foto']['name'];
	$file_foto_temp = $_FILES['foto']['tmp_name'];
	$deskripsi = $_POST['deskripsi'];
	
	$foto_dagangan = "http://203.153.21.11/app/onfish/img/ikan_dagangan/".$file_foto."";
	
	$sql = "INSERT INTO `dagangan` (`id_dagangan`, `id_petani`, `id_kategori_ikan`, `harga_per_kg`, `foto_dagangan`, `deskripsi`) VALUES (NULL, '$id_petani', '$id_kategori_ikan', '$harga_per_kg', '$foto_dagangan', '$deskripsi')";
	move_uploaded_file($file_foto_temp, "../img/ikan_dagangan/".$file_foto);
	
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		sendMessage();
		header("location:dagangan.php");
	}
	else{
		?>
		<script>
			alert('Data gagal diinput, mohon koreksi ulang.');
			history.back(-1);
		</script>
		<?php
	}
}
else{
	?>
	<script>
		alert('Mohon isi semua field');
		history.back(-1);
	</script>
	<?php
}

?>
<?PHP
function sendMessage() {
    $content      = array(
        "en" => 'Ada ikan baru di posting, yuk buka OnFish! Siapa tau pengen order!'
    );
    $fields = array(
        'app_id' => "38bd68b2-fdbd-4291-9bcf-6e5f9739039d",
        'included_segments' => array(
            'All'
        ),
        'contents' => $content
    );
    
    $fields = json_encode($fields);
    
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, "https://onesignal.com/api/v1/notifications");
    curl_setopt($ch, CURLOPT_HTTPHEADER, array(
        'Content-Type: application/json; charset=utf-8',
        'Authorization: Basic NWYzNjk1YTEtZGQ2ZC00ZTAyLWFkNzUtYjg1MjAwOGRlODE2'
    ));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
    curl_setopt($ch, CURLOPT_HEADER, FALSE);
    curl_setopt($ch, CURLOPT_POST, TRUE);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $fields);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    
    $response = curl_exec($ch);
    curl_close($ch);
    
    return $response;
}
?>