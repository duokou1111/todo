var BASE_URL ="http://127.0.0.1:8080";
function ajax(url,data,callback,type){
	$.ajax({
	            type : type,
				contentType:"application/json",
	            url : BASE_URL+url,
				dataType:"json",
	            data : data,
	            success : function(result) {
	                console.log(result);
	                callback(result);
	            },
	            error : function(e){
	            	alert("系统繁忙，请稍后再试！");
	                console.log(e.status);
	                console.log(e.responseText);
	            }
	        });
}