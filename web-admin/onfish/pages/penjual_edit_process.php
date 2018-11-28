<?php
include("koneksi.php");

if(isset($_POST)){
	$id_penjual = $_POST['id_penjual'];
	$nama_penjual = $_POST['nama_penjual'];
	$kontak = $_POST['kontak'];
	
	$sql = "update `penjual` set `nama_penjual` = '$nama_penjual', `kontak` = '$kontak' where `id_penjual` = '$id_penjual'";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "penjual.php";
		</script>
		<?php
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