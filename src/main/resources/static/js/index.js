$(".add-page-close").click(function(){
	$(".mask").fadeOut();
})
function add(){
	$(".mask").fadeIn();
}
$(".message-commit").click(function(){
	/*$(".mask2").fadeOut();*/
	window.location.reload();
})
function finish(id){
	var obj = new Object();
	obj.id = id;
	obj.status = '已完成';
	ajax("/task",JSON.stringify(obj),afterSubmit,"PUT");
}
function del(id){
	var obj = new Object();
	obj.id = id;
	ajax("/task",JSON.stringify(obj),afterSubmit,"DELETE");
}
function taskSubmit(){
	var obj = new Object();
	obj.title = $("#input-title").val();
	/*obj.date = $("#input-date").val().replace(/T/g," ");*/
	obj.date = $("#input-date").val();
	obj.priority = $('input:radio[name="priority"]:checked').val();
	obj.content = $("#input-body").val();
	console.log(obj.title);
	console.log(obj.date);
	console.log(obj.priority);
	console.log(obj.content);
	console.log(JSON.stringify(obj))
	ajax("/task",JSON.stringify(obj),afterSubmit,"post");
}
function afterSubmit(result){
	if(result.success == false){
		alert(result.message);
	}else{
		window.location.reload();
	}
}
$(".sort-commit").click(function(){
	var url = BASE_URL;
	var sort = $("input[name='sortType']:checked").val();
	var flag = false;
	$("input[name='priority']:checked").each(function(){
		if(flag == false) {
			url = url + '/priority';
			flag = true;
		}
		url = url + '/' + $(this).val();
	})
	var startDate = $("#start-date").val();
	var endDate = $("#end-date").val();
	if(startDate.length < 1){
		startDate = "0000-00-00-00-00";
		endDate = "9999-12-00-00-00-00";
	}else{
		alert("?")
		startDate = startDate.replace(/T/g,"-").replace(/:/g,"-");
		endDate = endDate.replace(/T/g,"-").replace(/:/g,"-");
	}
	url = url+"/date/"+startDate+"/"+endDate
	if (sort == null)
		sort = "id";
	url = url + "/sort/" + sort;
	window.location.href = url;
})