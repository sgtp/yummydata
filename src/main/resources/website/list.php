<?php
require_once("libs/sparqllib.php");

error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);

$server="http://yummydata.org:3030/yummy/query";


$db = sparql_connect($server );
if( !$db ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
$db->ns( "yummy","http://yummydata.org/lang#" );
$db->ns( "rdfs","http://www.w3.org/2000/01/rdf-schema#" );

date_default_timezone_set('Europe/London'); 
$date=date('Y-m-d'); 
$today=date('Y-m-d', strtotime($date .' -1 day'));
#test!!!!
#$today="2013-06-28" ;
 
$sparql = "select distinct *
where { graph ?g {
?res <http://yummydata.org/lang#hasDayDate> \"".$today."\" .
?res  <http://yummydata.org/lang#hasResultType> <http://yummydata.org/lang#analysisResults> .
?res  <http://yummydata.org/lang#sparkles> ?score .
?res  <http://yummydata.org/lang#stars> ?stars .
?res   <http://yummydata.org/lang#testing> ?endpoint .

}
graph ?g2 {
	?endpoint <http://www.w3.org/2000/01/rdf-schema#label> ?label
	}
}
order by desc(?score) desc(?stars)";


$result = $db->query( $sparql ); 
if( !$result ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
 
$fields = $result->field_array( $result );
/* 
print "<p>Number of rows: ".$result->num_rows( $result )." results.</p>";
print "<table class='example_table'>";
print "<tr>";
foreach( $fields as $field )
{
	print "<th>$field</th>";
}
print "</tr>";
while( $row = $result->fetch_array() )
{
	print "<tr>";
	foreach( $fields as $field )
	{
		print "<td>$row[$field]</td>";
	}
	print "</tr>";
}
print "</table>";
*/

# Just a bit of PHP ?
?>
<!DOCTYPE html>
<html>
  <head>
    <title>YummyData</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

  </head>
  <body>
 
    <div class="row">
    	<div class="span12">
        	<h1>Yummy Data is Here!</h1>
        </div>
   	</div>
   	<div class="row">
   		<div class="span2">
   			<h2>Menu</h2>
   			<ul>
   			<li>About</li>
   			<li>Register a new site</li>
   			</ul>
   		</div>
   		<div class="span10">
   			<table class="table table-striped">
   				<caption>Todays's ranking</caption>
   				<thead>
    				<tr>
      					<th>Endpoint</th>
      					<th>URL</th>
      					<th>Score</th>
      					<th>Stars</th>
      					<th>Detail</th>
    				</tr>
  				</thead>
   				<tbody>
   				<?php
   					while( $row = $result->fetch_array()){
						print "<tr>";
   						print "<td>".$row["label"]."</td>";
						print "<td>".$row["endpoint"]."</td>";
						print "<td>".$row["score"]."</td>";
						print "<td>";
						for($i=0; $i<intval($row["stars"]);$i++) {	//TODO this should ne a numeric to begin with
							print "<i class=\"icon-star\"></i>";
						}
						print "</td>";
						//print "<td> <a href=eDtail.php?endpoint=\"".$row[endpoint]."\"><i class=\"icon-info-sign\"></a> </td>";
						print "<td> <a class=\"icon-info-sign\" href=eDetail.php?endpoint=\"".$row['endpoint']."\"></a> </td>";

						print "</tr>\n";
					}

   				
   				
   				?>
   				</tbody>
   			</table>
   			
   			
   		</div>
   	</div>
           
    
    
    <!-- Scripts at the end for faster loading -->
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-42296188-1', 'yummydata.org');
  ga('send', 'pageview');

</script>
  </body>
</html>