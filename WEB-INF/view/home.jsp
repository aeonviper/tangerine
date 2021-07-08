<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="banner.jsp"%>

<br/>

<center>

<div>
<img src="<%= contextPath %>/resource/image/application/logo-one.jpg" class="gallery">
<br/>
<span class="title">Bingung bikin PR? Perlu bimbingan belajar?</span>
<br/>
<br/>
</div>

<div>
<img src="<%= contextPath %>/resource/image/application/logo-two.jpg" class="gallery">
<br/>
<span class="title">Tanya aja lewat gadgetmu</span>
<br/>
<br/>
</div>

<div>
<img src="<%= contextPath %>/resource/image/application/logo-three.jpg" class="gallery">
<br/>
<span class="title">Dapat penjelasan dan jawaban, kebingungan pun hilang</span>
<br/>
<br/>
</div>

<a class="title" href="<%= contextPath %>/register">Daftar sekarang</a><span class="title">, gratis 1 bulan pertama !</span>

</center>

<br/>
<br/>
<br/>

<script>
var tangerineApplication = angular.module('tangerineApplication', []);
tangerineApplication.controller('controller', ['$scope', function($scope) {
	$scope.language = language;
}]);
</script>

<%@include file="language.jsp"%>
</body>
<%@include file="footer.jsp"%>
