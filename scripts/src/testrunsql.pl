use DBI;

use PerlConfig;

my $os = lc $^O;
my $linux;

my $configfile = 'perlscripts.linux.cfg';
if($os =~ m/mswin/){
    $configfile = 'perlscripts.windows.cfg';
}
print "debug: configfile= ", $configfile, "\n"; 

my $pgm = lc $0;        #lc->convert to lowercase
$pgm =~ s/\..*//;       #remove file extension

my $cfg = new PerlConfig;

$cfg->read($configfile) or die "Couldn't read Config file: $!";

use Getopt::Std;

getopts('hf:d:u:p:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

if($opts{h} ne ""){
print "usage: ", $pgm, " -f <infile> -d <database> -u <user> -p <password> \n  <infile> is sql script file name.\n";
exit 0;
}
 

my $infile = $opts{f};
my $dbname = $opts{d};   
my $dbuser = $opts{u};
my $pw = $opts{p};  

if($dbname eq "") {
    $dbname = $cfg->get("$pgm.dbname");
}
    
if($dbuser eq "") {
    $dbuser = $cfg->get("$pgm.dbuser");
    $pw = $dbuser;
}
 
 if ($infile eq "" || $dbname eq "" || $dbuser eq "" || $pw eq "" ){
    print "missing param(s). usage: ", $pgm, " -f <infile> -d <database> -u <user> -p <password> \n";
    exit 0;
 }
 
print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: infile= ", $infile, "\n";
#print "debug: password=" ,  $pw, "\n";  #avoid displaying password

my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw, {AutoCommit => 1})
        or die "Couldn't connect to database: " . DBI->errstr;
        
my @data;
 
my $sqlrec;

my $cnt=0; 

open(FHIN, "<$infile") or die "Cannot open $file: $!";

my @content = <FHIN>;

foreach $sqlrec(@content) {
    
   while($sqlrec =~ m/[\n;]$/) {      #remove trailing new line char(s) and semicolon
      chop($sqlrec);
   }
    
   if ($sqlrec eq "" || $sqlrec =~ m/^-/){     #ignore starting with '-'
       next;
    } 
 
    print "debug: sqlrec= ", $sqlrec, "\n";    
      
    $dbh->do($sqlrec) or die "Couldn't execute statement: " . $dbh->errstr; 
    
    $cnt++; 
}

close FHIN;

print "debug: #sql stmts executed=", $cnt, "\n";

print "\n";

$dbh->disconnect;