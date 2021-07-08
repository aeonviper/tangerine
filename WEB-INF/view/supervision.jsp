<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.AdministrationController model = (tangerine.controller.AdministrationController) request.getAttribute(orion.core.Constant.model);
%>

<div class="content">

<center><div style="min-height:20px;" ng-bind="notification"></div></center>

<div ng-show="boxAuthentication">
<table>
	<tr><td>Username:</td><td><input type="text" ng-model="username"></td></tr>
	<tr><td>Password:</td><td><input type="password" ng-model="password"></td></tr>
	<tr><td></td><td><button ng-click="login();">Login</button></td></tr>
</table>
</div>

<div ng-show="boxContent">

<button ng-click="listUser();">List User</button>
<button ng-click="listVoucher();">List Voucher</button>
<button ng-click="listVerification();">List Verification</button>
<button ng-click="clear();">Clear</button>
<button ng-click="logout();">Logout</button>
<br/>
<button ng-click="now();">Now</button>
<button ng-click="midnight();">Midnight</button>
<input type="text" ng-model="conversionDateTime" placeholder="2016-01-01T00:00:00">
<button ng-click="dateTimeConvertNumber();">Convert Number</button>
<button ng-click="dateTimeConvertText();">Convert Text</button>
<br/>
<br/>

<table ng-show="userList.length > 0">
<tr>
	<th>Id</th>
	<th>Text Id</th>
	<th>Email</th>
	<th>Name</th>
	<th>Membership</th>
	<th>Active</th>
	<th>Credit</th>
	<th><button ng-click="userList = null">Close</button></th>
</tr>
<tr ng-repeat="user in userList">
	<td>{{::user.id}}</td>
	<td>{{::user.textId}}</td>
	<td><a ng-click="viewUser(user.id);" class="">{{::user.email}}</a></td>
	<td>{{::user.name}}</td>
	<td>{{::user.membershipType}}</td>
	<td>{{::user.active}}</td>
	<td>{{::user.subscriptionCredit | number}}</td>
</tr>
</table>

<div ng-show="user != null">

<table>
<tr>
	<td><h3>User Detail</h3></td>
	<td>
		<button ng-click="viewUser(user.id);">Refresh</button>
		<button ng-click="boxEditUser = !boxEditUser">Edit</button>
		<button ng-click="deleteUser(user.id);">Delete</button>
		<button ng-click="user = null">Close</button>
	</td>
</tr>
</table>

<table ng-show="user" class="standard">
<tr>
	<td>Id</td>
	<td>{{user.id}}</td>
</tr>
<tr>
	<td>Text Id</td>
	<td>{{user.textId}}</td>
</tr>
<tr>
	<td>Name</td>
	<td>{{user.name}} <input type="text" ng-model="transitUser.name" ng-show="boxEditUser"></td>	
</tr>
<tr>
	<td>Email</td>
	<td>{{user.email}} <input type="text" ng-model="transitUser.email" ng-show="boxEditUser"></td>
</tr>
<tr>
	<td>Membership</td>
	<td>{{user.membershipType}}</td>
</tr>
<tr>
	<td>Credit</td>
	<td>{{user.subscriptionCredit | number}} <span ng-show="boxEditUser">{{transitUser.subscriptionCredit | number}}</span> <input type="text" ng-model="transitUser.subscriptionCredit" ng-show="boxEditUser"></td>
</tr>
<tr>
	<td>School</td>
	<td>{{user.school}}<br/><textarea ng-model="transitUser.school" ng-show="boxEditUser"></textarea></td>
</tr>
<tr>
	<td>Phone</td>
	<td>{{user.phone}} <input type="text" ng-model="transitUser.phone" ng-show="boxEditUser"></td>
</tr>
<tr>
	<td>Active</td>
	<td>{{user.active}} <input type="checkbox" ng-model="transitUser.active" ng-show="boxEditUser"></td>
</tr>
<tr>
	<td>Information</td>
	<td>{{user.information}}<br/><textarea ng-model="transitUser.information" ng-show="boxEditUser"></textarea></td>
</tr>
<tr>
	<td>State</td>
	<td>{{user.state}}<br/><textarea ng-model="transitUser.state" ng-show="boxEditUser"></textarea></td>
</tr>
<tr>
	<td>Created</td>
	<td>{{user.created}}</td>
</tr>
<tr>
	<td></td>
	<td><button ng-click="updateUser();" ng-show="boxEditUser">Edit</button></td>
</tr>
<tr>
	<td><strong>Subscription</strong></td>
	<td>

	<table>
		<tr>
			<th>Id</th>
			<th>From</th>
			<th>To</th>
			<th>Package</th>
			<th>Used</th>
			<th>Total</th>
			<th>Renewal</th>
			<th>Information</th>
			<th>Created</th>
			<th>
				<button ng-click="boxUserSubscription = !boxUserSubscription">Add</button>
			</th>
		</tr>
		<tr ng-repeat="userSubscription in user.subscriptionList">
			<td>{{::userSubscription.id}}</td>
			<td>{{::userSubscription.fromDate}}</td>
			<td>{{::userSubscription.toDate}}</td>
			<td>{{::userSubscription.userSubscriptionPackage}}</td>
			<td>{{::userSubscription.usedQuestion}}</td>
			<td>{{::userSubscription.userSubscriptionQuestion}}</td>
			<td>{{::userSubscription.renewal}}</td>
			<td>{{::userSubscription.information}}</td>
			<td>{{::userSubscription.created}}</td>
			<td>
				<a><img src="resource/image/application/bin.png" ng-click="deleteUserSubscription(userSubscription.id);"></a>
			</td>
		</tr>
	</table>

	<table class="standard" ng-show="user != null && boxUserSubscription">
	<tr>
		<td>Id</td>
		<td>{{userSubscription.id}}</td>
	</tr>
	<tr>
		<td>From</td>
		<td><input type="text" ng-model="userSubscription.fromDate"></td>
	</tr>
	<tr>
		<td>To</td>
		<td><input type="text" ng-model="userSubscription.toDate"></td>
	</tr>
	<tr>
		<td>Package</td>
		<td><input type="text" ng-model="userSubscription.subscriptionPackage"></td>
	</tr>
	<tr>
		<td>Used</td>
		<td><input type="text" ng-model="userSubscription.usedQuestion"></td>
	</tr>
	<tr>
		<td>Total</td>
		<td><input type="text" ng-model="userSubscription.subscriptionQuestion"></td>
	</tr>
	<tr>
		<td>Renewal</td>
		<td><input type="text" ng-model="userSubscription.renewal"></td>
	</tr>
	<tr>
		<td>Information</td>
		<td><textarea ng-model="userSubscription.information"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<button ng-click="addUserSubscription(user.id);">Add</button>
			<button ng-click="userSubscription = {}">Clear</button>
		</td>
	</tr>
	</table>

	</td>
</tr>
<tr>
	<td><strong>Payment</strong></td>
	<td>

	<table>
		<tr>
			<th>Id</th>
			<th>Date</th>
			<th>From</th>
			<th>To</th>
			<th>Amount</th>
			<th>Reference</th>
			<th>Code</th>
			<th>Information</th>
			<th>Created</th>
			<th>
				<button ng-click="boxUserPayment = !boxUserPayment">Add</button>
			</th>
		</tr>
		<tr ng-repeat="userPayment in user.paymentList">
			<td>{{::userPayment.id}}</td>
			<td>{{::userPayment.date}}</td>
			<td>{{::userPayment.fromName}}</td>
			<td>{{::userPayment.toName}}</td>
			<td>{{::userPayment.amount | number}}</td>
			<td>{{::userPayment.reference}}</td>
			<td>{{::userPayment.code}}</td>
			<td>{{::userPayment.information}}</td>
			<td>{{::userPayment.created}}</td>
			<td>
				<a><img src="resource/image/application/bin.png" ng-click="deleteUserPayment(userPayment.id);"></a>
			</td>
		</tr>
	</table>

	<table class="standard" ng-show="user != null && boxUserPayment">
	<tr>
		<td>Id</td>
		<td>{{userPayment.id}}</td>
	</tr>
	<tr>
		<td>Date</td>
		<td><input type="text" ng-model="userPayment.date"></td>
	</tr>
	<tr>
		<td>From</td>
		<td><input type="text" ng-model="userPayment.fromName"></td>
	</tr>
	<tr>
		<td>To</td>
		<td><input type="text" ng-model="userPayment.toName"></td>
	</tr>
	<tr>
		<td>Amount</td>
		<td>{{userPayment.amount | number}} <input type="text" ng-model="userPayment.amount"></td>
	</tr>
	<tr>
		<td>Reference</td>
		<td><input type="text" ng-model="userPayment.reference"></td>
	</tr>
	<tr>
		<td>Code</td>
		<td><input type="text" ng-model="userPayment.code"></td>
	</tr>
	<tr>
		<td>Information</td>
		<td><textarea ng-model="userPayment.information"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<button ng-click="addUserPayment(user.id);">Add</button>
			<button ng-click="userPayment = {}">Clear</button>
		</td>
	</tr>
	</table>

	</td>
</tr>
<tr>
	<td><strong>Expense</strong></td>
	<td>

	<table>
		<tr>
			<th>Id</th>
			<th>From</th>
			<th>To</th>
			<th>Package</th>
			<th>Amount</th>
			<th>Question</th>
			<th>Information</th>
			<th>Created</th>
			<th>
				<button ng-click="boxUserExpense = !boxUserExpense">Add</button>
			</th>
		</tr>
		<tr ng-repeat="userExpense in user.expenseList">
			<td>{{::userExpense.id}}</td>
			<td>{{::userExpense.fromDate}}</td>
			<td>{{::userExpense.toDate}}</td>
			<td>{{::userExpense.subscriptionPackage}}</td>
			<td>{{::userExpense.amount | number}}</td>
			<td>{{::userExpense.subscriptionQuestion}}</td>
			<td>{{::userExpense.information}}</td>
			<td>{{::userExpense.created}}</td>
			<td>
				<a><img src="resource/image/application/bin.png" ng-click="deleteUserExpense(userExpense.id);"></a>
			</td>
		</tr>
	</table>

	<table class="standard" ng-show="user != null && boxUserExpense">
	<tr>
		<td>Id</td>
		<td>{{userExpense.id}}</td>
	</tr>
	<tr>
		<td>From</td>
		<td><input type="text" ng-model="userExpense.fromDate"></td>
	</tr>
	<tr>
		<td>To</td>
		<td><input type="text" ng-model="userExpense.toDate"></td>
	</tr>
	<tr>
		<td>Package</td>
		<td><input type="text" ng-model="userExpense.subscriptionPackage"></td>
	</tr>
	<tr>
		<td>Amount</td>
		<td>{{userExpense.amount | number}}<input type="text" ng-model="userExpense.amount"></td>
	</tr>
	<tr>
		<td>Question</td>
		<td><input type="text" ng-model="userExpense.subscriptionQuestion"></td>
	</tr>
	<tr>
		<td>Information</td>
		<td><textarea ng-model="userExpense.information"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<button ng-click="addUserExpense(user.id);">Add</button>
			<button ng-click="userExpense = {}">Clear</button>
		</td>
	</tr>
	</table>

	</td>
</tr>
<tr>
	<td><strong>Voucher</strong></td>
	<td>

	<table>
		<tr>
			<th>Id</th>
			<th>Voucher Id</th>
			<th>Name</th>
			<th>Type</th>
			<th>Date</th>
			<th>Information</th>
			<th>Created</th>
			<th>
				<button ng-click="boxUserVoucher = !boxUserVoucher">Add</button>
			</th>
		</tr>
		<tr ng-repeat="userVoucher in user.voucherList">
			<td>{{::userVoucher.id}}</td>
			<td>{{::userVoucher.voucherId}}</td>
			<td>{{::userVoucher.name}}</td>
			<td>{{::userVoucher.voucherType}}</td>
			<td>{{::userVoucher.date}}</td>
			<td>{{::userVoucher.information}}</td>
			<td>{{::userVoucher.created}}</td>
			<td>
				<a><img src="resource/image/application/bin.png" ng-click="deleteUserVoucher(userVoucher.id);"></a>
			</td>
		</tr>
	</table>

	<table class="standard" ng-show="user != null && boxUserVoucher">
	<tr>
		<td>Id</td>
		<td>{{userVoucher.id}}</td>
	</tr>	
	<tr>
		<td>Voucher Id</td>
		<td><input type="text" ng-model="userVoucher.voucherId"></td>
	</tr>	
	<tr>
		<td>Date</td>
		<td><input type="text" ng-model="userVoucher.date"></td>
	</tr>
	<tr>
		<td>Information</td>
		<td><textarea ng-model="userVoucher.information"></textarea></td>
	</tr>
	<tr>
		<td></td>
		<td>
			<button ng-click="addUserVoucher(user.id);">Add</button>
			<button ng-click="userVoucher = {}">Clear</button>
		</td>
	</tr>
	</table>

	</td>
</tr>
</table>
</div>

<table ng-show="voucherList.length > 0">
<tr>
	<th>Id</th>
	<th>Name</th>
	<th>Type</th>
	<th>Start</th>
	<th>Finish</th>
	<th>Active</th>
	<th>Data</th>
	<th>Multiple</th>
	<th>Information</th>
	<th>Created</th>
	<th>
		<button ng-click="boxVoucher = !boxVoucher">Add/Edit</button>
		<button ng-click="voucherList = null">Close</button>
	</th>
</tr>
<tr ng-repeat="voucher in voucherList">
	<td>{{::voucher.id}}</td>
	<td>{{::voucher.name}}</td>
	<td>{{::voucher.voucherType}}</td>
	<td>{{::voucher.start}}</td>
	<td>{{::voucher.finish}}</td>
	<td>{{::voucher.active}}</td>
	<td>{{::voucher.data}}</td>
	<td>{{::voucher.multiple}}</td>
	<td>{{::voucher.information}}</td>
	<td>{{::voucher.created}}</td>
	<td>
		<a><img src="resource/image/application/page_edit.png" ng-click="editVoucher(voucher.id);"></a>
		<a><img src="resource/image/application/bin.png" ng-click="deleteVoucher(voucher.id);"></a>
	</td>
</tr>
</table>

<table class="standard" ng-show="voucherList.length > 0 && boxVoucher">
<tr>
	<td>Id</td>
	<td>{{voucher.id}}</td>
</tr>
<tr>
	<td>Name</td>
	<td><input type="text" ng-model="voucher.name"></td>
</tr>
<tr>
	<td>Type</td>
	<td><input type="text" ng-model="voucher.voucherType"></td>
</tr>
<tr>
	<td>Start</td>
	<td><input type="text" ng-model="voucher.start"></td>
</tr>
<tr>
	<td>Finish</td>
	<td><input type="text" ng-model="voucher.finish"></td>
</tr>
<tr>
	<td>Active</td>
	<td><input type="checkbox" ng-model="voucher.active"></td>
</tr>
<tr>
	<td>Data</td>
	<td><textarea ng-model="voucher.data"></textarea></td>
</tr>
<tr>
	<td>Multiple</td>
	<td><input type="checkbox" ng-model="voucher.multiple"></td>
</tr>
<tr>
	<td>Information</td>
	<td><textarea ng-model="voucher.information"></textarea></td>
</tr>
<tr>
	<td></td>
	<td>
		<button ng-click="addVoucher();">Add</button>
		<button ng-click="updateVoucher();">Update</button>
		<button ng-click="voucher = {}">Clear</button>
	</td>
</tr>
</table>

<table ng-show="verificationList.length > 0">
<tr>
	<th>Id</th>
	<th>Text Id</th>
	<th>User Id</th>
	<th>Type</th>
	<th>Value</th>
	<th>Code</th>
	<th><button ng-click="verificationList = null">Close</button></th>
</tr>
<tr ng-repeat="verification in verificationList">
	<td>{{::verification.id}}</td>
	<td>{{::verification.textId}}</td>
	<td>{{::verification.userId}}</td>
	<td>{{::verification.type}}</td>
	<td>{{::verification.value}}</td>
	<td>{{::verification.code}}</td>
	<td>
		<a><img src="resource/image/application/bin.png" ng-click="deleteVerification(verification.id);"></a>
	</td>
</tr>
</table>

</div>

</div>

<script>
var token = '';
var server = 'http://localhost:8881/';

var nullify = function(bean, propertyList) {
	for (var i=0;i<propertyList.length;i++) {
		var property = propertyList[i];
		if (bean[property] === '') {
			bean[property] = null;
		}
	}
};

var beanCopy = function(source, destination) {
	for (property in source) {		
		if (typeof source[property] !== 'object') {
			destination[property] = source[property];	
		}				
	}
};

var tangerineApplication = angular.module('tangerineApplication', []);

tangerineApplication.controller('controller', ['$scope', '$http', function($scope, $http) {

	$scope.clear = function() {
		$scope.notification = '';
	};

	$scope.login = function() {
		$scope.clear();
		$socket.send({type: 'login', username: $scope.username, password: $scope.password});
		$scope.password = '';
	};

	$scope.logout = function() {
		$scope.clear();
		$socket.send({type: 'logout', token: token});
		token = '';
	};

	$scope.listUser = function() {
		$scope.clear();
		$socket.send({type: 'listUser', token: token});
	};

	$scope.viewUser = function(id) {
		$scope.clear();
		$socket.send({type: 'viewUser', token: token, id: id});
	};

	$scope.listVerification = function() {
		$scope.clear();
		$socket.send({type: 'listVerification', token: token});
	};

	$scope.listVoucher = function() {
		$scope.clear();
		$socket.send({type: 'listVoucher', token: token});
	};

	$scope.addVoucher = function() {
		$scope.clear();
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.voucher.created = data;
			$socket.send({type: 'addVoucher', token: token, voucher: $scope.voucher});
		});
	};

	$scope.editVoucher = function(id) {
		$scope.clear();
		for (var i=0;i<$scope.voucherList.length;i++) {
			if ($scope.voucherList[i].id === id) {
				$scope.voucher = $scope.voucherList[i];
				$scope.boxVoucher = true;
				break;
			}
		}
	};

	$scope.updateVoucher = function() {
		$scope.clear();
		nullify($scope.voucher, ['start', 'finish']);
		$socket.send({type: 'editVoucher', token: token, voucher: $scope.voucher});
	};

	$scope.deleteVoucher = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteVoucher', token: token, id: id});
		}
	};

	$scope.addUserSubscription = function(userId) {
		$scope.clear();
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.userSubscription.userId = userId;
			$scope.userSubscription.created = data;
			$socket.send({type: 'addUserSubscription', token: token, userSubscription: $scope.userSubscription});
		});
	};

	$scope.deleteUserSubscription = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteUserSubscription', token: token, id: id, userId: $scope.user.id});
		}
	};
	
	$scope.addUserPayment = function(userId) {
		$scope.clear();
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.userPayment.userId = userId;
			$scope.userPayment.created = data;
			$socket.send({type: 'addUserPayment', token: token, userPayment: $scope.userPayment});
		});
	};

	$scope.deleteUserPayment = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteUserPayment', token: token, id: id, userId: $scope.user.id});
		}
	};
	
	$scope.addUserExpense = function(userId) {
		$scope.clear();
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.userExpense.userId = userId;
			$scope.userExpense.created = data;
			$socket.send({type: 'addUserExpense', token: token, userExpense: $scope.userExpense});
		});
	};

	$scope.deleteUserExpense = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteUserExpense', token: token, id: id, userId: $scope.user.id});
		}
	};
	
	$scope.addUserVoucher = function(userId) {
		$scope.clear();
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.userVoucher.userId = userId;
			$scope.userVoucher.created = data;
			$socket.send({type: 'addUserVoucher', token: token, userVoucher: $scope.userVoucher});
		});
	};

	$scope.deleteUserVoucher = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteUserVoucher', token: token, id: id, userId: $scope.user.id});
		}
	};

	$scope.deleteVerification = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteVerification', token: token, id: id});
		}
	};
	
	$scope.updateUser = function() {
		$scope.clear();		
		$socket.send({type: 'editUser', token: token, user: $scope.transitUser});
	};
	
	$scope.deleteUser = function(id) {
		$scope.clear();
		if (confirm('Are you sure you want to delete this?')) {
			$socket.send({type: 'deleteUser', token: token, id: id});
			$scope.user = null
		}
	};
	
	$scope.now = function() {
		$http.get('system/utility/now')
		.success(function(data, status, headers, config) {
			$scope.notification = data;
		});
	};

	$scope.midnight = function() {
		$http.get('system/utility/midnight')
		.success(function(data, status, headers, config) {
			$scope.notification = data;
		});
	};

	$scope.dateTimeConvertNumber = function() {
		$http.post('system/utility/datetime/convert', {number: $scope.conversionDateTime})
		.success(function(data, status, headers, config) {
			$scope.notification = data;
		});
	};

	$scope.dateTimeConvertText = function() {
		$http.post('system/utility/datetime/convert', {text: $scope.conversionDateTime})
		.success(function(data, status, headers, config) {
			$scope.notification = data;
		});
	};

	var $socket = io(server);

	$socket.on('connect', function() {
		console.log('connected');
	});

	$socket.on('disconnect', function() {
		console.log('disconnected');
	});

	$socket.on('event', function(data) {
		console.log('event')
	});

	$socket.on('message', function(packet) {
		// console.log(angular.toJson(packet));

		if (packet.type === 'loginResult') {
			if (packet.value) {
				token = packet.token;
				$scope.notification = 'Login successful';
				$scope.boxAuthentication = false;
				$scope.boxContent = true;
			} else {
				$scope.notification = 'Login failed';
			}
		} else if (packet.type === 'logoutResult') {
			if (packet.value) {
				$scope.notification = 'Logout successful';
				$scope.boxAuthentication = true;
				$scope.boxContent = false;
			} else {
				$scope.notification = 'Logout failed';
			}
		} else if (packet.type === 'action') {
			packet.value.token = token;
			$socket.send(packet.value);
		} else if (packet.type === 'userList') {
			$scope.userList = packet.value;
		} else if (packet.type === 'voucherList') {
			$scope.voucherList = packet.value;
		} else if (packet.type === 'user') {
			$scope.user = packet.value;
			$scope.transitUser = {};
			beanCopy($scope.user, $scope.transitUser);
		} else if (packet.type === 'verificationList') {
			$scope.verificationList = packet.value;
		} else if (packet.type === 'error') {
			if (typeof packet.bean === 'object') {
			}
			$scope.notification = 'Error: ' + packet.text + ' ' + angular.toJson(packet.bean);
		}

		$scope.$apply();
	});

	$scope.language = language;
	$scope.boxAuthentication = true;

	$scope.voucher = {};

}]);
</script>

<script src="<%= contextPath %>/resource/angular/i18n/angular-locale_id-id.js"></script>
<script type="text/javascript" src="<%= contextPath %>/resource/script/socket.io.js"></script>

<%@include file="language.jsp"%>
</body>
<%@include file="footer.jsp"%>
