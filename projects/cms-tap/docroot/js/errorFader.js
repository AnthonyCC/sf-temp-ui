var fadeIndex = 1;
var fadeDirection = 1;
var fadeColors = ["#990000", "#A40000", "#AF0000", "#BB0000", "#C60000", "#D10000", "#DD0000", "#E80000", "#F30000", "#FF0000"];
function stepFader(){
	fadeIndex += fadeDirection;
	e = document.getElementById('fadeError');
	e.style.color = fadeColors[fadeIndex];
	if ((fadeIndex >= fadeColors.length-1)||(fadeIndex < 1)){
		fadeDirection = 0-fadeDirection;
	}
	setTimeout("stepFader()", 100);
}
stepFader();
