<?php
error_reporting(0);
include("db_config.php");
// array for JSON response
$response = array();
if (isset($_GET['id']) && isset($_GET['name']) && isset($_GET['address'])) {
    $id = $_GET['id'];
    $name = $_GET['name'];
    $address = $_GET['address'];

    $result = mysqli_query($db, "DELETE FROM info WHERE id='$id' AND name='$name' AND address='$address'");

    $row_count = mysqli_affected_rows($db);
    if ($row_count > 0) {
        $response["success"] = 1;
        $response["message"] = "Deleted Successfully.";
    } else {
        $response["success"] = 0;
        $response["message"] = "Failed To Delete";
    }
    // echoing JSON response
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "Missing Parameters";
    // echoing JSON response
    echo json_encode($response);
}
?>
