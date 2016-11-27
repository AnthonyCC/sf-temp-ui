use DBI;

my $os = lc $^O;

my $islinux;
if($os =~ m/mswin/){
    $islinux=0;
}
else{
    $islinux=1;
}

my $INSTANCE;

if($islinux){
    $INSTANCE = 'DBSTO.NYC.FRESHDIRECT.COM';
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
}
else {
    $INSTANCE = 'DBSTO';
}

my $user = 'fd_promo_rw';
my $pass = 'fd_promo_rw';
print "debug: user= ", $user, "\n";
print "debug: pass=" ,  $pass, "\n"; 

print "debug: trying DBI->connect('dbi:Oracle:$INSTANCE', $user, $pass)...\n";
     
my $dbh  = DBI->connect("dbi:Oracle:$INSTANCE", $user, $pass)
      or die "Couldn't connect to database: " . DBI->errstr;

print "\n ---- Connection OK ------ \n";


$dbh->disconnect;

print "\n";