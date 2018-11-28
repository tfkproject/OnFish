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
?>

<body>

    <div id="wrapper">

	<?php include("nav.php"); ?>
	
	
	<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Input Data Kategori Ikan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Form Input Data
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <form role="form" action="dagangan_add_process.php" method="POST" enctype="multipart/form-data">
                                        <div class="form-group">
                                            <label>Pilih Petani</label>
                                            <select name="id_petani" class="form-control">
											<?php
											$query = "select * from `petani`";
											$eksekusi = mysqli_query($koneksi, $query);
											while($row = mysqli_fetch_array($eksekusi)){
											?>
												<option value="<?php echo $row['id_petani'];?>"><?php echo $row['nama_petani'];?></option>
											<?php
											}
											?>
											</select>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Kategori Ikan</label>
                                            <select name="id_kategori_ikan" class="form-control">
											<?php
											$query = "select * from `kategori_ikan`";
											$eksekusi = mysqli_query($koneksi, $query);
											while($row = mysqli_fetch_array($eksekusi)){
											?>
												<option value="<?php echo $row['id_kategori_ikan'];?>"><?php echo $row['nama_ikan'];?></option>
											<?php
											}
											?>
											</select>
                                        </div>
                                        
                                        <div class="form-group">
											<label>Harga Per Kg</label>
											<div class="form-group input-group">
												<span class="input-group-addon">Rp</span>														
												<input type="number" name="harga_per_kg" class="form-control">
											</div>
										</div>
                                        
                                        <div class="form-group">
                                            <label>Foto</label>
											<small>Required for better view <strong style="color: red;">200px X 200px</strong>.</small>
											<input type="file" name="foto" onchange="readURL(this);" required>
                                        </div>
										<script type="text/javascript">
											function readURL(input) {
												if (input.files && input.files[0]) {
													var reader = new FileReader();

													reader.onload = function (e) {
														$('#blah')
														.attr('src', e.target.result)
														.width(200)
														.height(200);
													}

													reader.readAsDataURL(input.files[0]);
												}
											}
										</script>
										<div class="form-group">
                                            <img id="blah" src="#" alt="Preview disini" />
                                        </div>

                                        <div class="form-group">
											<label>Deskripsi</label>
											<div class="form-group">														
												<textarea name="deskripsi" class="form-control" placeholder="Deskripsi dagangan"></textarea>
											</div>
										</div>
										
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