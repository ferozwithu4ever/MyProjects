<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Email Service</title>
<style>
.loader {
	border: 16px solid #f3f3f3;
	border-radius: 50%;
	border-top: 16px solid #3498db;
	width: 120px;
	height: 120px;
	-webkit-animation: spin 2s linear infinite; /* Safari */
	animation: spin 2s linear infinite;
}

/* Safari */
@
-webkit-keyframes spin { 0% {
	-webkit-transform: rotate(0deg);
}

100%
{
-webkit-transform
:
 
rotate
(360deg);
 
}
}
@
keyframes spin { 0% {
	transform: rotate(0deg);
}
100%
{
transform
:
 
rotate
(360deg);
 
}
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.7/angular.min.js"></script>

<script>
	var app = angular.module('myApp', []);
	app.controller('myCtrl', function($scope, $http) {
		$scope.sendEmails = function() {
			
			if(document.getElementById("myFile").files[0]){
				var file = document.getElementById("myFile").files[0];
				var fd = new FormData();
				fd.append('file',file);
				var fileType = file.name.split(".");
				 var fileExt = fileType[fileType.length - 1];
				if(fileExt == "xls" || fileExt == "XLS"){
					
				} else{
					alert("Please provide a valid file type. eg: xls/xlxs");
				}	
			}else if(!$scope.recipient) {
				alert("Please enter valid email or choose a file");
				return;
			}
			document.getElementById('pageLoader').style.display = 'block';
			
			
			$http.post("EmailServelt",fd,{
			headers:{'Content-Type':undefined},
			params:{
				recipient : $scope.recipient,
				subject:$scope.subject,
				message:$scope.message
			}
			}).then(function(response){
				document.getElementById('pageLoader').style.display = 'none';
				alert("Requested Operation completed");
			});
		};

	});
</script>
</head>
<body>
	<div ng-app="myApp">
		<div ng-controller="myCtrl">
			<div class="loader" id="pageLoader" style="display: none"></div>
			<h1>Email Service</h1>
			<table>
				<!--<tr>
					<td align="center"><select>
							<option value="">Select</option>
							<option value="gmail">gmail</option>
							<option value="hotmail">hotmail</option>
							<option value="yahoo">yahoo</option>
							<option value="AOL">AOL</option>
					</select></td>
				</tr>-->

				<tr>
					<td><input type="file" file-model="myFile" id="myFile" />
				</tr>
				<tr>

				</tr>

				<tr>
					<td>To: <textarea cols="50" rows="5" name="recipient"
							ng-model="recipient" style="width: 370px; float: right;"></textarea></td>

				</tr>



				<tr>
					<td>Subject: <input type="text" name="subject" size="50"
						ng-model="subject" style="width: 370px; float: right;"></td>

				</tr>
				<tr>
					<td>Message: <textarea cols="50" rows="10" id="message"
							ng-model="message"></textarea></td>

				</tr>

				<tr>
					<td colspan="2" align="center">
						<button ng-click="sendEmails();">Send Email</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>