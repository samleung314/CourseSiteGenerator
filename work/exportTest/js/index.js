$.ajaxSetup({
    headers: { "cache-control": "no-cache" }
});

function initProjects() {
	var dataFile = "./js/Course.json";
	loadCourseData(dataFile);
}
function loadCourseData(jsonFile) {
	$.getJSON(jsonFile, function (json) {
		loadJSONCourseData(json);
	});
}
function loadJSONCourseData(data) {
	var banner = data.subject + " " + data.number + " - " + data.semester + " " + data.year +
					"<br>" + data.title;

	document.getElementById("title").innerHTML = data.subject + " " + data.number;
	document.getElementById("banner").innerHTML = banner;

	document.getElementById("navBar").innerHTML = data.navBar;

	try{document.getElementById("inlined_course").innerHTML = data.subject + data.number;}catch(err){}
	
	document.getElementById("instructor_link").innerHTML = data.instructor_name;
	document.getElementById("instructor_link").href = data.instructor_home;

	var banner = data.banner;
	var leftFoot = data.left_footer;
	var rightFoot = data.right_footer;

	document.getElementById("bannerImg").src = getImageName(banner);
	document.getElementById("leftFoot").src = getImageName(leftFoot);
	document.getElementById("rightFoot").src = getImageName(rightFoot);
}
function getImageName(path){
	var strings = path.split("\\");
	return "./images/" + strings[strings.length - 1];
}