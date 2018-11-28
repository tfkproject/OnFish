<?php
include("koneksi.php");

if(isset($_GET)){
	$id = $_GET['id_transaksi'];
	
	$sql = "UPDATE `transaksi` SET `status` = 'Y' WHERE `id_transaksi` = $id";
	$eksekusi = mysqli_query($koneksi, $sql);
	if($eksekusi){
		?>
		<script>
			window.location = "transaksi.php";
		</script>
		<?php
	}
	else{
		?>
		<script>
			alert('Status berhasil di update');
			history.back(-1);
		</script>
		<?php
	}
}
else{
	?>
	<script>
		alert('Maaf, terjadi error.');
		history.back(-1);
	</script>
	<?php
}
?>