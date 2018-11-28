<?php
session_start();
if (empty($_SESSION)) {
	header("location:index.php"); // jika belum login, maka dikembalikan ke file form_login.php
}
else{
	include("koneksi.php");
?>
<!DOCTYPE html>
<html lang="en">

<?php
include("head.php");

$id_penjual = $_GET['id_penjual'];
?>

<body>

    <div id="wrapper">

	<?php include("nav.php"); ?>
	
	
	<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Edit Data Nelayan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Form Edit Nelayan
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <form role="form" action="penjual_edit_process.php" method="POST">
                                        <?php
        									$query = "select * from `penjual` where `id_penjual` = '$id_penjual'";
		        							$eksekusi = mysqli_query($koneksi, $query);
				        					while($row = mysqli_fetch_array($eksekusi)){
						           		?>
						           		<input name="id_penjual" class="form-control" type="hidden" value="<?php echo $row['id_penjual']; ?>" >
                                        <div class="form-group">
                                            <label>Nama Nelayan</label>
                                            <input name="nama_penjual" class="form-control" value="<?php echo $row['nama_penjual']; ?>" placeholder="contoh: Joko">
                                        </div>
                                        <div class="form-group">
                                        <label>Kontak / No HP</label>
                                            <input name="kontak" type="number" class="form-control" value="<?php echo $row['kontak']; ?>" placeholder="Wajib gunakan 62 (kode id indonesia) guna untuk WhatsApp. contoh: 6281234567890">
                                        </div>
										<?php
				        				}
										?>
                                        <button name="submit" value="submit" type="submit" class="btn btn-default">Submit</button>
                                        <button type="reset" class="btn btn-default">Reset</button>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <?php
	include("script.php");
	?>

</body>

</html>
<?php
}
?>