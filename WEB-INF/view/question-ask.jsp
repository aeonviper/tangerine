<%@include file="header.jsp"%>
<body ng-controller="controller" ng-cloak>
<%@include file="menu.jsp"%>

<%
tangerine.controller.QuestionController model = (tangerine.controller.QuestionController) request.getAttribute(orion.core.Constant.model);
Question question = model.getQuestion();
%>

<div class="content">

<div class="three">{{::language.photo}}</div>
Upload foto yang berisi hal hal yang ingin kamu tanyakan<br/>
(contoh: soal PR yang kamu kesulitan menjawab, rumus yang membingungkan, atau cuplikan buku yang ingin kamu diskusikan)<br/>
Untuk memperjelas pertanyaan, kamu bisa upload sampai 10 foto, gunakan untuk foto pertanyaannya sendiri, foto rumus rumus yang berkaitan, dan info lainnya yang berkaitan dengan pertanyaan<br/>

<div style="margin:15px 0 20px 0;">
	<div id="uploadList"></div>
	<div id="uploadNotification"></div>
	<br/>

<%
if (mobile) {
%>
	<div id="container">
		<div id="drop" class="hidden"></div>
		<button id="browse">Select file(s)</button>			    
	</div>
<%	
} else {
%>
	<div id="container">
		<div id="drop" style="border:4px dashed #b4b9be;padding:30px;margin-bottom:10px;text-align:center;">			
			<div style="font-family:'Open Sans';font-size:20px;">Drop files here to upload</div>
			<div style="font-family:'Open Sans';font-size:12px;color:#444;">or</div>
			<button id="browse">Select file(s)</button>			
		</div>	    
	</div>
<%
}
%>

	<div>
		Uploader not working ? Use the <a href="<%= contextPath %>/question/inquire">standard uploader</a>
	</div>
</div>

<form id="questionForm" method="post" action="<%= contextPath %>/question/ask/do" enctype="multipart/form-data">

<input type="hidden" name="question.id" value="<%= question.getId() %>">

<div class="three">{{::language.question}}</div>
<textarea name="question.text" rows="5" style="width:100%;" placeholder="{{::language.yourQuestion}}"></textarea>

<div class="three">{{::language.explanation}}</div>
<input type="radio" name="question.explanationType" value="ExplanationOnly" checked> Ingin penjelasan dulu, jawaban hanya kalau diminta<br/>
<input type="radio" name="question.explanationType" value="ExplanationAndAnswer"> Ingin penjelasan dan jawaban sekaligus<br/>

<div class="three">{{::language.subject}}</div>
<select name="question.category">
	<option name="" value=""/>
<%
	for (Category category : Category.enumerationList) {
		if (category.equals(principal.getLastUsedCategory())) {
			out.println("<option value='" + category.name() + "' selected>" + category.getDisplay(language) + "</option>");
		} else {
			out.println("<option value='" + category.name() + "'>" + category.getDisplay(language) + "</option>");
		}
	}
%>
</select>

<div class="three">{{::language.level}}</div>
<select name="question.level">
	<option name="" value=""/>
<%
	for (Level level : Level.enumerationList) {
		if (level.equals(principal.getLastUsedLevel())) {
			out.println("<option value='" + level.name() + "' selected>" + level.getDisplay() + "</option>");
		} else {
			out.println("<option value='" + level.name() + "'>" + level.getDisplay() + "</option>");
		}
	}
%>
</select>

<br/>
<br/>

<button id="upload">{{::language.next}}</button>
</form>

</div>

<script>
var tangerineApplication = angular.module('tangerineApplication', []);
tangerineApplication.controller('controller', ['$scope', function($scope) {
	$scope.language = language;
}]);
</script>

<script>
var uploader = new plupload.Uploader({
    runtimes: 'html5,flash,silverlight,html4',
    browse_button: 'browse',
    container: 'container',
    drop_element: 'drop',
    file_data_name: 'file[0]',
    url: "<%= contextPath %>/question/upload/do?id=<%= question.getId() %>",
    flash_swf_url: '<%= contextPath %>/resource/plupload/Moxie.swf',
    silverlight_xap_url: '<%= contextPath %>/resource/plupload/Moxie.xap',

    resize: {
    	width: 1280,
    	height: 1024
    },
    
    filters: {
        max_file_size: '10mb',
        mime_types: [
            {title: "Image files", extensions: "jpg,gif,png"},
            {title: "Zip files", extensions: "zip"}
        ]
    },

    init: {
        PostInit: function() {
            document.getElementById('upload').onclick = function() {
                uploader.start();
                return false;
            };
        },
 
        FilesAdded: function(up, files) {
            plupload.each(files, function(file) {
                document.getElementById('uploadList').innerHTML += '<div class="file" id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b> <a href="' + "javascript:removeFile('" + file.id + "');" + '">delete</a></div><br/>';
            });
        },
 
        UploadProgress: function(up, file) {
            document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span> <meter value=" + file.percent + "></meter>";
        },
        
        UploadComplete: function(upload, files) {
        	document.getElementById('questionForm').submit();
        },
 
        Error: function(up, err) {
            document.getElementById('uploadNotification').innerHTML += "\nError #" + err.code + ": " + err.message;
        }
    }
});
uploader.init();

function removeFile(id) {	
	uploader.removeFile(uploader.getFile(id));
	jQuery('#' + id).next().remove();
	jQuery('#' + id).remove();
}
</script>

<%@include file="language.jsp"%>
</body>
<%@include file="footer.jsp"%>
