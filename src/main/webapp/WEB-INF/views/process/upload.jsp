<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link href="${ctx}/static/jQuery-File-Upload-9.11.2/css/jquery.fileupload.css" rel="stylesheet">
<script src="${ctx}/static/jQuery-File-Upload-9.11.2/js/vendor/jquery.ui.widget.js"></script>
<script src="${ctx}/static/jQuery-File-Upload-9.11.2/js/jquery.iframe-transport.js"></script>
<script src="${ctx}/static/jQuery-File-Upload-9.11.2/js/jquery.fileupload.js"></script>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"	aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel"><spring:message code='public.fileupload'/></h4>
			</div>
			<div class="modal-body">
				<div class="progress">
				  <div id="progress-bar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="min-width: 2em;width: 0%;">
				    0%
				  </div>
				</div>
				<div class="file-box">
					
			        <span class="btn btn-success fileinput-button">
				        <span><spring:message code='public.choose.file'/></span>
				        <input id="fileupload" type="file" name="files" data-url="${ctx}/file/paytranUpload">
				    </span>
				    <span class="text-primary"><spring:message code='public.file.valid.paytran'/></span>
				    
				    <br/>
					<ul id='messange'></ul>
					<div id='errorMessange'></div>
			   
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code='public.close'/></button>
			</div>
		</div>
	</div>
</div>






<script type="text/javascript">

$(function () {
    $('#fileupload').fileupload({
    	add: function(e, data) {
            var uploadErrors = [];
           
            var fileType = data.files[0].name.split('.').pop().toLowerCase(), allowdtypes = 'jpg,jpeg,png,gif,pdf';
            if (allowdtypes.indexOf(fileType) < 0) {
                uploadErrors.push(data.files[0].name+' : '+'<spring:message code='public.fileTypeError'/>');
            }
            if(uploadErrors.length == 0 && data.originalFiles[0]['size'] && data.originalFiles[0]['size'] > 1048576*4) {
                uploadErrors.push(data.files[0].name+' : '+'<spring:message code='public.maxfilesize'/>');
            }
            if(uploadErrors.length > 0) {
            	$.each(uploadErrors, function (index, error) {
                    $("#errorMessange").html('<p style="color: red;">' + error + '</p>');
                });
            	return false;
            } else {
                data.submit();
            }
    	},
    	autoUpload: false,
        dataType: 'json',
        done: function (e, data) {
        	//alert(data.result.success)
        	//alert(data.result.message)
        	if(data.result.success == 'true'){
        		$.each(data.files, function (index, file) {
                    $("#messange").append('<li><code style="color:#5CB85C">'+file.name+'</code><spring:message code='public.success'/></li>');
               
                  //放到form
    				$("#filesDIV").append("<input type='hidden' name='attachments' id='attachment"+data.result.fileId+"' value='"+data.result.fileId+"'>")
    				
    				$("#filesLabel").append("<li>"+file.name+"<a href='javascript:void(0)' onclick='delAttach("+data.result.fileId+",this)'>&nbsp;&nbsp;<i class='fa fa-minus-circle'></i></a></li>");
    				
    				var tmpExt = file.name.split('.').pop()
    				if(tmpExt == "pdf"){
    					$("#dumpImageDiv").append('<img id="uploadImg'+data.result.fileId+'" src="${ctx}/static/images/pdf.png" style="height:120px;width:95px;margin:0 10px 10px 0;" />');
    				}else{
    					$("#dumpImageDiv").append('<img id="uploadImg'+data.result.fileId+'" src="${ctx}/file/dumpImage?path='+data.result.filePath+'" style="height:120px;width:100px;margin:0 10px 10px 0;" />');
    				}
    				
        		});
				
				
				$("#errorMessange").empty();
			}else{
				$('#errorMessange').html("<span style='color:red;'>"+data.result.message+"</span>");
			}
        	
        	
        	
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $("#progress-bar").html(progress + "%");
			$("#progress-bar").attr("aria-valuenow",progress);
			$("#progress-bar").css("width", progress + "%");
        }
    });
});
	
	

	
	
</script>