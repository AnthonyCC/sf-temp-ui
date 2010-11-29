var IE=(document.all);
var pi = Math.PI;
var ypos2 = 50;
var xpos2 = 60;
var xpos3 = 200;
//var clockHand = new Array(0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44);
var clockHand = new Array(0,4,8,12,16,20,24,28,32,36,40,44);
var yHour = new Array;
var xHour = new Array;
var hourMinute = 0;
var hourMinuteX = 0;
var hourMinuteY = 0;
var homi;
var homiX;
var count;
var hSpan;
var dspS;
var cIS;
var spec;
function hand(val,what){
spec=1;
if (what=='minute'){
homi='2';
homiX=140;
count=clockHand.length;
if (showMin==0){
spec=0;
}
}
else{
homi='';
homiX=0;
count=clockHand.length-2;
}
hourMinute = - (pi / 180 * 90) + (pi / 6 * val) + (pi / 360);
for(i=0;i<count;i++){
yHour[i] = Math.round(clockHand[i] * Math.sin(hourMinute));
xHour[i] = Math.round(clockHand[i] * Math.cos(hourMinute));
hourMinuteY = yHour[i] + ypos2+4;
hourMinuteX = xHour[i] + xpos2+5+homiX;
if(spec==1){
eval('document.getElementById("idhand'+homi+i+'").style.top='+hourMinuteY);
eval('document.getElementById("idhand'+homi+i+'").style.left='+hourMinuteX);
}
}
}
var num=0;
var x;
var y;
var radius;
var angle;
function pickClockhand(what1,what2){
hSpan=document.getElementById('helpSpan');
dspS=document.getElementById("display").style;
cIS=document.getElementById("closeImg").style;
x = 0;
y = 0;
radius = 55;
angle = 150;
if(showMin>5){
hSpan.innerHTML=hSpan.innerHTML+'<br><br>Only the black numbers for the minutes are selectable (steps of '+showMin+' minutes)...<br><br>';
}
if(showMin==1){
document.getElementById('mm').style.display="inline";
document.getElementById('mp').style.display="inline";
hSpan.innerHTML=hSpan.innerHTML+'<br><br>To select a value for the minutes other than in steps of 5, you can use the "min -" or the "min +" button to select the exact minute...<br><br>';
}
if(showMin>0){
dspS.top=ypos2+85;
dspS.left=0;
if(IE){
cIS.left=xpos2+197;
}
else{
cIS.left=xpos2+199;
}
}
if(showMin==0){
xpos2=83;
document.getElementById("pick").style.width=178;
dspS.top=ypos2+85;
dspS.left=-50;
if(IE){
cIS.left=xpos2+71;
}
else{
cIS.left=xpos2+73;
}
}
for(i=1;i<=12;i++){
x = radius * Math.sin(angle * pi / 180);
x += xpos2 - 2;
y = radius * Math.cos(angle * pi / 180);
y += ypos2 + 10;
angle -= 30;
eval('document.getElementById("'+what1+i+'").style.top='+y);
eval('document.getElementById("'+what1+i+'").style.left='+x);
}
hand(12,'hour');
if(showMin!=0){
x = 0;
y = 0;
radius = 55;
angle = 150;
for(i=1;i<=12;i++){
num=(i*5);
if ((i*5)==60){
num=0;
}
x = radius * Math.sin(angle * pi / 180);
x += xpos3 - 2; 
y = radius * Math.cos(angle * pi / 180);
y += ypos2 + 10;
angle -= 30;
eval('document.getElementById("'+what2+num+'").style.top='+y);
eval('document.getElementById("'+what2+num+'").style.left='+x);
}
hand(0,'minute');
}
}
var insertD;
function writeDivs(){
insertD='';
for (var i=0;i<howMany;i++){
}
insertD +='<input type="hidden" name="pickedH" value="12">';
insertD +='<input type="hidden" name="pickedM" value="00">';
insertD +='<input type="hidden" name="pickedHH" value="0">';
insertD +='<input type="hidden" name="pickedMM" value="0">';
insertD +='<input type="hidden" name="pickedAm" value=" am">';
insertD +='<input type="hidden" name="pickWhat" value=0>';
for (var i=0;i<howMany;i++){
insertD +='<input type="text" name="picked'+i+'" readonly size=8>&nbsp;';
insertD +='<input class="btn2" type="button" value="'+pickerName[i]+'" onClick="showPicker('+i+');document.timePick.pickWhat.value='+i+';">&nbsp;&nbsp;';
}
insertD +='<div id="pick">';
insertD +='<img class="close" src="/ccassets/javascript/timepicker/Help.gif" id="helpImg" alt="&nbsp;Help me...&nbsp;" title="&nbsp;Help me...&nbsp;" border=0 onClick="showHelp()"><img class="close" src="/ccassets/javascript/timepicker/Close.gif" id="closeImg" alt="&nbsp;Close time picker...&nbsp;" title="&nbsp;Close time picker...&nbsp;" border=0 onClick="showPicker(document.timePick.pickWhat.value)">';
for (var j=0;j<clockHand.length-2;j++){
insertD +='<div class="handClock" id="idhand'+j+'" style="color:'+hCol+'">.</div>';
}
if (showMin!=0){
for (var j=0;j<clockHand.length;j++){
insertD +='<div class="handClock" id="idhand2'+j+'" style="color:'+mCol+'">.</div>';
}
}
return insertD;
}
var hPic;
var mPic;
var hhPic;
var mmPic;
var amPic;
var tPic;
var insert;
function writePicker(what){
hPic=document.timePick.pickedH;
mPic=document.timePick.pickedM;
hhPic=document.timePick.pickedHH;
mmPic=document.timePick.pickedMM;
amPic=document.timePick.pickedAm;
insert='';
for (var j=1;j<=12;j++){
if (what=='pickHour'){
insert +='<div class="pickClock" id="'+what+j+'" onClick="pickAm(this.innerHTML,\'hour\',null);" onmouseover="dispAm('+j+',this,\'hour\');" onmouseout="hideAm(this);">'+j+'</div>';
}
if (what=='pickMinute'&&showMin!=0){
num=(j*5);
if ((j*5)==60){
num=0;
}
if (num%showMin==0){
insert +='<div class="pickClock" id="'+what+num+'" onClick="pickAm('+j+',\'minute\','+num+');" onmouseover="dispAm('+j+',this,\'minute\');" onmouseout="hideAm(this);">'+num+'</div>';
}
else{
insert +='<div class="pickClock" id="'+what+num+'"><font color="red">'+num+'</font></div>';
}
}
}
if (what=='pickMinute'){
insert +='<div class="disp" id="display"><input type="radio" value=" am" name="ampm" checked onClick="amOrPm(this,0)">&nbsp;am&nbsp;-&nbsp;pm&nbsp;<input type="radio" value=" pm" name="ampm" onClick="amOrPm(this,0)">&nbsp;&nbsp;<input type="checkbox" name="clockType" onClick="amOrPm(null,1);">&nbsp;24 h clock&nbsp;&nbsp;<input id="mm" class="btn" type="button" value="min -" onClick="minusOne()" title="subtract one minute...">&nbsp;&nbsp;<input id="mp" class="btn" type="button" value="min +" onClick="plusOne()" title="add one minute..."></div>';
insert +='</div><div id="helpSpan" class="help" onClick="this.style.display=\'none\';">Click the numbers to insert hours and minutes.<br><br>Use the radio buttons for toggling from am to pm; when doing so in 24 hours mode, this will toggle between 0 to 12 and 13 to 24 o\'clock.<br><br>Click somewhere in this area to hide help...</div>';
}
return insert;
}
function rewritePicker(what,val){
for (var j=1;j<=12;j++){
var newHour=j+val;
if(show24!=1&&j==12&&val==12){
newHour=0;
}
document.getElementById(what+j).innerHTML=newHour;
}
}
function minusOne(){
mns=mPic.value;
mns--;
if (mns==-1){
mns=59;
}
pickAm(mns/5,'minute',mns)
}
function plusOne(){
mns=mPic.value;
mns++;
if (mns==60){
mns=0;
}
pickAm(mns/5,'minute',mns)
}
var hrs;
var mns;
function pickAm(val,what,what2){
tPic=document.timePick['picked'+document.timePick.pickWhat.value];

hrs=Number(hPic.value);
mns=mPic.value;
hand(val,what);
if (what=='minute'){
val2=val;
if (val2==12){
val2=0;
}
hand(Number(hrs)+Number(val2)*5/60,'hour');
}
if (what=='hour'){
hand(parseFloat(Number(val)+Number(Number(mns)/60)),'hour');
}
if (what2!=null){
if (what2<10){
what2='0'+what2;
}
mPic.value=what2;
mmPic.value="1";
if (what2!='00'&&hrs==24){
hPic.value=0;
}
if (what2=='00'&&hrs==0&&show24==1){
hPic.value=24;
}
}
if (what2==null){
hPic.value=val;
if (val==24&&mns!='00'){
hPic.value=0;
}
hhPic.value="1";
}
if (hPic.value!=""&&mPic.value!=""){
if(hPic.value.length<2){
hPic.value='0'+hPic.value;
}
tPic.value=hPic.value+':'+mPic.value;
if (document.timePick.clockType.checked==false){
tPic.value=tPic.value+amPic.value;
}
}
}
var temp;
function amOrPm(what,who){
tPic=document.timePick['picked'+document.timePick.pickWhat.value];
hrs=Number(hPic.value);
mns=mPic.value;
var chBox=document.timePick.clockType.checked;
for (var i = 0; i<2;i++){
if (document.timePick.ampm[i].checked==true){
temp=document.timePick.ampm[i].value;
}
}
if (chBox==true){
amPic.value="";
}
if (chBox==false){
amPic.value=temp;
}
if (temp==" pm"&&chBox==true){
rewritePicker('pickHour',12);
}
if (temp==" am"||chBox==false){
rewritePicker('pickHour',0);
}
if (who==1||who==0){
if (temp==" pm"){
if(hrs<=12&&hrs!=0){
hrs=hrs+12;
if(hrs==24&&show24!=1){
hrs=0;
}
}
if(hrs==12&&Number(mns)==0&&chBox==true&&show24==1){
hrs=24;
}
if(hrs==24&&Number(mns)>0&&chBox==true){
hrs=0;
}
if((hrs==24||hrs==0)&&chBox==false){
hrs=12;
}
if(hrs>12&&chBox==false){
hrs=hrs-12;
}
}
if (temp==" am"){
if(hrs==24||hrs==0){
hrs=12;
}
if(hrs>12){
hrs=hrs-12;
}
}
document.timePick.clockType.blur();
}
if (who==0&&chBox==false){
amPic.value=temp;
what.blur();
}
hPic.value=hrs;
if(hPic.value.length<2){
hPic.value='0'+hPic.value;
}

mPic.value=mns;
tPic.value=hPic.value+':'+mPic.value;
if (chBox==false){
tPic.value=tPic.value+amPic.value;
}
}
function dispAm(val,highlight,what){
highlight.className="high";
if (what=="minute"&&mmPic.value!="1"||(what=="hour"&&hhPic.value!="1")){
hand(val,what);
}
}
function hideAm(lowlight){
lowlight.className="pickClock";
}
function resetValues(){
document.timePick.pickedH.value="12";
document.timePick.pickedM.value="00";
document.timePick.pickedHH.value="0";
document.timePick.pickedMM.value="0";
document.timePick.pickedAm.value=" am";
document.timePick.clockType.checked=false;
document.timePick.ampm[0].checked=true;
hand(12,'hour');
hand(0,'minute');
}
var oldH;
var oldM;
var oldHN;
var oldMN;
var oldAmPm;
function restoreValues(val){
oldH=val.substring(0,2);
oldM=val.substring(3,5);
oldHN=Number(oldH);
oldMN=Number(oldM/5);
if (val.length>5){
oldAmPm=val.substring(6,8)
document.timePick.clockType.checked=false;
}
if (val.length<=5){
oldAmPm='';
document.timePick.clockType.checked=true;
}
if(oldAmPm=='am'||(oldHN>0&&oldHN<13)){
document.timePick.ampm[0].checked=true;
}
if(oldAmPm=='pm'||oldHN==0||oldHN>12){
document.timePick.ampm[1].checked=true;
}
oldAmPm=' '+oldAmPm;
document.timePick.pickedH.value=oldH;
document.timePick.pickedM.value=oldM;
document.timePick.pickedHH.value="1";
document.timePick.pickedMM.value="1";
document.timePick.pickedAm.value=oldAmPm;
hand(parseFloat(Number(oldHN)+Number(Number(oldM)/60)),'hour');
hand(oldMN,'minute');
if (document.timePick.clockType.checked==true&&(oldHN==0||oldHN>12)){
rewritePicker('pickHour',12);
}
else{
rewritePicker('pickHour',0);
}
}
function showPicker(val){
tPic=document.timePick['picked'+val];
var pDiv=document.getElementById('pick').style
pDiv.background=bgCol;
if(pDiv.display=="block"&&val==document.timePick.pickWhat.value){
resetValues();
document.getElementById('helpSpan').style.display="none";
pDiv.display="none";
}
else{
if (tPic.value!=""){
restoreValues(tPic.value);
pDiv.display="block";
}
if (tPic.value==""){
resetValues();
rewritePicker('pickHour',0);
pDiv.background=bgCol;
pDiv.display="block";
}
}
}
var hSp2;
function showHelp(){
hSp2=document.getElementById('helpSpan').style;
if(IE){
hSp2.display='block';
hSp2.width=281;
hSp2.height=167;
}
else{
document.getElementById('helpSpan').style.top=-169;
document.getElementById('helpSpan').style.display='block';
}
}
function writeAll(){
document.write(writeDivs());
document.write(writePicker('pickHour'));
document.write(writePicker('pickMinute'));
pickClockhand('pickHour','pickMinute');
return '';
}
