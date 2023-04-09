<?php
error_reporting(E_ERROR | E_PARSE); // To hide any notice/warning messages
include("db_config.php");
$response = array();

if (isset($_GET['id'])) {
    $id = $_GET['id'];
    $stmt = $db->prepare("SELECT * FROM info WHERE id=?");
    $stmt->bind_param("i", $id);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $item = array();
        $item["id"] = $row["id"];
        $item["name"] = $row["name"];
        $item["address"] = $row["address"];
        $response["order"] = $item;
        $response["success"] = 1;
    } else {
        $response["success"] = 0;
        $response["message"] = "No Items Found";
    }
    echo json_encode($response);
}

$db->close();
?>
