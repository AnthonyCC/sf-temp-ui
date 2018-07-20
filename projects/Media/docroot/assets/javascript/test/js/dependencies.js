//populate filters
var selectOptions = ['<option value=""></option>'];
FreshDirect.test.js.dependencies.globalFileList.forEach(function(inData){
	selectOptions.push('<option value="'+inData+'">'+inData+'</option>');
});
$jq('#filter_dependencies').append(selectOptions.join());
selectOptions = ['<option value=""></option>'];
Object.keys(FreshDirect.test.js.dependencies.globalRegisters).forEach(function(inData){
	selectOptions.push('<option value="'+inData+'">'+inData+'</option>');
});
$jq('#filter_registers').append(selectOptions.join());

//button events
$jq('#filter-apply').on('click', function(e) {
	var valDep = $jq('#filter_dependencies').val();
	var valReg = $jq('#filter_registers').val();
	if (valDep !== '' || valReg !== '') {
		$jq('.file-cont').hide().stop().filter(function(i,e) {
			return (
				(valDep !== '' && $jq(e).text().indexOf(valDep) !== -1) ||
				(valReg !== '' && $jq(e).text().indexOf(valReg) !== -1)
			);
		}).show();
	} else {
		$jq('.file-cont').show();
	}
});
$jq('#filter-clear').on('click', function(e) {
	$jq('#filter_dependencies').val('');
	$jq('#filter_registers').val('');
	$jq('.file-cont').show();
});