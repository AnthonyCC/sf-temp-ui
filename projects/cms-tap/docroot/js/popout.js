
function popout(src) {
	var target = findNearestElementByClass(src, "popout");
	if (target) {
		target.style.display = "block";
	}
	return false;
}

function findNearestElementByClass(src, className) {
	// check immediate children
    var i;
    for (i=0; i<src.childNodes.length; i++) {
        var n = src.childNodes[i];
        if (n.className==className) {
            return n;
        }
    }
	// check siblings
	var target = src;
	while (target && target.className != className) {
		target = target.nextSibling ? target.nextSibling : target.parentNode;
	}
	return target;
}

function popin(src) {
	var target = findNearestElementByClass(src, "popout");
	if (target) {
		target.style.display = "none";
	}
	return false;
}