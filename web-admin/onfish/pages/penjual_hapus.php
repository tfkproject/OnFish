<?php
include("koneksi.php");

if(isset($_GET)){
	$id = $_GET['id_penjual'];
	
	$sql = "DELETE FROM `penjual` WHERE `id_penjual` = $id";
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
			alert('Data gagal dihapus');
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