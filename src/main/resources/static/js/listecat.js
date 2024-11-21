window.onload=init;

function init(){
	document.getElementById("listeCat").addEventListener("change", function(){
	var value = document.getElementById("listeCat").value;
	if (value != 0 ){
		window.location.href="/categorie?id="+value;
	}
})
}