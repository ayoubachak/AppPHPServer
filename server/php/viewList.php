<?php
$mysqli = new mysqli("localhost","root","","phpserverapp");
$response = array();

// Check connection
if ($mysqli -> connect_errno) {
  echo "Failed to connect to MySQL: " . $mysqli -> connect_error;
  exit();
}

// Perform query
if ($result = $mysqli -> query("SELECT * FROM info")) {


$response["orders"] = array();
if ($result -> num_rows>0) {
while ($row = $result -> fetch_row()) {
    $item = array();
            $item["id"] = $row[0];
            $item["name"] = $row[1];
            $item["address"] = $row[2];

            // push ordered items into response array
            array_push($response["orders"], $item);

  }
  // success
     $response["success"] = 1;
}
else {
    // order is empty
      $response["success"] = 0;
      $response["message"] = "No Items Found";
}
// echoing JSON response
echo json_encode($response);

  $result -> free_result();
}
    $mysqli -> close();
?>
