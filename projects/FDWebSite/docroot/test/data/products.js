var varPicker1 = 
        'Pick at most <input name="varmax" type="text" size="6"/> at random.' +
	'(other options <input type="radio" id="varperm" name="varperm" value="number" onclick="toggleVarpicker()"/>)';

var varPicker2 =
	'Pick ... <ul><li>one at random <input type="radio" id="varperm" name="varperm" checked value="one"/> ' +
	'or <li>pick randomly upto a limit <input type="radio" size="5" id="varperm" name="varperm" value="number" onclick="toggleVarpicker()"/> or' +
	'<li>all permutations <input type="radio" id="varperm" name="varperm" value="all" onclick="alertPerms()"/></ul>';
        
var skuSel = "";

function toggleVarpicker() {
   var varperm = document.getElementById('varperm');
   if (varperm.value != "number") {
      document.getElementById('varpicker').innerHTML = varPicker1;
   } else {
      document.getElementById('varpicker').innerHTML = varPicker2;
   }
}

function alertPerms() {
   alert("Selecting all permutations could result in humongous result sets!");
}

function setgroc(arg) {
   if (arg == false) {
      if (document.getElementById('groc_false').checked == false)  document.getElementById('groc_false').checked = false; 
      else {
         document.getElementById('groc_false').checked = true;
         document.getElementById('groc_true').checked = false;
      }
   } else if (arg == true) {
      if (document.getElementById('groc_true').checked == false)  document.getElementById('groc_true').checked = false;
      else {
         document.getElementById('groc_false').checked = false;
         document.getElementById('groc_true').checked = true;
      }
   }
}

function selectAvailableSkus() {
   if (skuSel != "avail_skus") {
      skuSel = "avail_skus";
      document.getElementById('skus').checked = false;
      document.getElementById('avail_skus').checked = true;
      document.getElementById('perm_skus').checked = false;
      document.getElementById('perm_options').checked = false;
      document.getElementById('perm_salesunits').checked = false;
      document.getElementById('bpath').checked = false;

      remove_column('skus');
      add_column('avail_skus');
      remove_column('perm_skus');
      remove_column('perm_options');
      remove_column('perm_salesunits');
      remove_column('bpath');

   } else {

      skuSel = "";
      document.getElementById('avail_skus').checked = false;
      remove_column('avail_skus');
   }
   display_columns();
}

function selectSkus() {
   if (skuSel != "skusel") {
      skuSel = "skusel";
      document.getElementById('skus').checked = true;
      document.getElementById('avail_skus').checked = false;
      document.getElementById('perm_skus').checked = false;
      document.getElementById('perm_options').checked = false;
      document.getElementById('perm_salesunits').checked = false;
      document.getElementById('bpath').checked = false;

      add_column('skus');
      remove_column('avail_skus');
      remove_column('perm_skus');
      remove_column('perm_options');
      remove_column('perm_salesunits');
      remove_column('bpath');

   } else {
      skuSel = "";
      document.getElementById('skus').checked = false;
      remove_column('skus');
   }
   display_columns();
}

function selectVariations() {
   if (skuSel != "varsel") {
      skuSel = "varsel";
      document.getElementById('skus').checked = false;
      document.getElementById('avail_skus').checked = false;
      document.getElementById('perm_skus').checked = true;
      document.getElementById('perm_options').checked = true;
      document.getElementById('perm_salesunits').checked = true;
      document.getElementById('bpath').checked = false;

      remove_column('skus');
      remove_column('avail_skus');
      remove_column('bpath');
      add_column('perm_skus');
      add_column('perm_options');
      add_column('perm_salesunits');

   } else {
      skuSel = "";
      document.getElementById('perm_skus').checked = false;
      document.getElementById('perm_options').checked = false;
      document.getElementById('perm_salesunits').checked = false;

      remove_column('perm_skus');
      remove_column('perm_options');
      remove_column('perm_salesunits');
   }
   display_columns();
}

function selectBrowsePath() {
   if (skuSel != "bpath") {
      skuSel = "bpath";
      document.getElementById('skus').checked = false;
      document.getElementById('avail_skus').checked = false;
      document.getElementById('perm_skus').checked = false;
      document.getElementById('perm_options').checked = false;
      document.getElementById('perm_salesunits').checked = false;
      document.getElementById('bpath').checked = true;

      remove_column('skus');
      remove_column('avail_skus');
      remove_column('perm_skus');
      remove_column('perm_options');
      remove_column('perm_salesunits');
      add_column('bpath');

   } else {
      skuSel = "";
      document.getElementById('bpath').checked = false;

      remove_column('bpath');
   }
   display_columns();
}

function my_init() {
   document.getElementById('varpicker').innerHTML = varPicker2; 
}
