use DBI;

use Getopt::Std;  
getopts('f:hUp:', \ my %opts);    # -ptake arg.  Sets opt_* as a side effect.

if($opts{h} ne ""){
    print "usage: ", $pgm, " -f <infile> [-U] [-p <password>]\n where -U->update mode (build AND run sql script),\n", "<password> is database password. Other params are read from perl.cfg file\n";
    exit 1;
 }


my $os = lc $^O;

my $configfile;
my $linux;
if($os =~ m/mswin/){
    $configfile = '..\perlscripts.windows.cfg';
    $islinux=0;
}
else{
     $configfile = '../perlscripts.linux.cfg';
    $islinux=1;
}

print "debug: configfile= ", $configfile, "\n"; 

print "debug: islinux= $islinux\n";

if($islinux){
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
    BEGIN {
            push @INC,"../lib";
           }
    }
else{
        BEGIN {
            push @INC,"..\lib";
        }
}

use PerlConfig;
use Removeattrs;
 
my $pgm = lc $0;        #lc->convert to lowercase
$pgm =~ s/\..*//;       #remove file extension

my $cfg = new PerlConfig;

$cfg->read($configfile) or die "Couldn't read Config file: $!";
 
my $dbname = $cfg->get("$pgm.dbname");
my $dbuser = $cfg->get("$pgm.dbuser");
my $infile=$cfg->get("$pgm.infile");

print "debug: dollaroptsf= $opts{f}\n";

if ($opts{f} ne ""){
    $infile = $opts{f};
}

my $pw   = $dbuser;

if ($opts{p} ne ""){
    $pw = $opts{p};
}

print "debug: dbname= ", $dbname, "\n";
print "debug: dbuser= ", $dbuser, "\n";
print "debug: infile= ", $infile, "\n";
print "debug: password=" ,  $pw, "\n"; #avoid displaying password

print "debug: trying DBI->connect('dbi:Oracle:$dbname', $dbuser, $pw)...\n";

my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw, {AutoCommit => 1})
        or die "Couldn't connect to database: " . DBI->errstr;
        
my @data;
 
my $sqlrec;

my $cnt=0; 

open(FHIN, "<$infile") or die "Cannot open $file: $!";

do{
    do{
        $line = <FHIN>;
        $sqlrec .= $line;
    } until eof or $line =~ m/[;]/ ;
     while($sqlrec =~ m/[\r\n;]$/) {      #remove trailing new line char(s) and semicolon
          chop($sqlrec);
       }
         
     print "debug: sqlrec= ", $sqlrec, "\n";
     if ($sqlrec eq "" || $sqlrec =~ m/--/){     #ignore starting with '-'
           $sqlrec = "";
     }
     else{     
        $dbh->do($sqlrec) or die "Couldn't execute statement: " . $dbh->errstr;
        $sqlrec = "";
        $cnt++;   
     }        
}until eof ;

#

close FHIN;

print "debug: #sql stmts executed=", $cnt, "\n";

print "\n";

$dbh->disconnect;