#!c:\perl\bin\perl
use DBI;

my $os = lc $^O;

my $islinux;
if($os =~ m/mswin/){
    $islinux=0;
}
else{
    $islinux=1;
}

my $dbname;

if($islinux){
    $dbname = 'DEVINT.NYC.FRESHDIRECT.COM';
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
}
else {
    $dbname = 'DEVINT_10G';
}

my $dbuser = 'fdstore_prda';
my $pw = 'fdstore_prda';
print "debug: dbuser= ", $dbuser, "\n";
print "debug: pw=" ,  $pw, "\n"; 

print "debug: trying DBI->connect('dbi:Oracle:$dbname', $dbuser, $pw)...\n";
     
my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
      or die "Couldn't connect to database: " . DBI->errstr;

print "\n ---- Connection OK ------ \n";


$dbh->disconnect;

print "\n";