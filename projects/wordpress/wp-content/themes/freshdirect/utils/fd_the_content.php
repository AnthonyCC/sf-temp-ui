<?php

function fd_the_content() {
	$content = get_the_content('(More...)');
	$content = apply_filters('the_content', $content);
	$content = str_replace(']]>', ']]&gt;', $content);
	$content = '<div>' . $content . '</div>';

	$document = new DOMDocument();
	$document->loadXML($content);

	$imageNode = null;	
	$divs = $document->getElementsByTagName("div");

	$content_array = array();

	if ($divs->length > 0) {
		for ($i = 0; $i < $divs->length; ++$i) {
			$element = $divs->item($i);
			$isCaption = strpos($element->getAttribute('class'), 'wp-caption');
			if ($isCaption !== false) {
				$imageNode = $element;				
			}
		}
	}

	if ($imageNode != null) {
//		processImageNode((Element) imageNode);

		$content_array["media"] = $document->saveXML($imageNode);
		$imageNode->parentNode->removeChild($imageNode);
		$content_array["content"] = $document->saveXML($document->firstChild);
		return $content_array;
	}

	$objects = $document->getElementsByTagName("object");

	if ($objects->length > 0) {
		$obj = $objects->item(0);
		$content_array["media"] = $document->saveXML($obj);
		$obj->parentNode->removeChild($obj);
		$content_array["content"] = $document->saveXML($document->firstChild);
		return $content_array;
	}

	$content_array["content"] = $content;
	return $content_array;
}

function fd_the_day_link() {
	$post_time = get_post_time('Y-m-d H:i:s', true);
	$year = mysql2date('Y', $post_time, false);
	$month = mysql2date('m', $post_time, false);
	$day = mysql2date('d', $post_time, false);
	echo '<a class="myfd-day-link" href="' . get_day_link($year, $month, $day) . '">'."$month/$day/$year".'</a>';
}
