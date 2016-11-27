package Removeattrs;
use DBI;
my $os = lc $^O;
my $linux;
if($os =~ m/mswin/){
    $islinux=0;
}
else{
    $islinux=1;
}

if($islinux){
    $ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";
}

use DBD::Oracle ;

sub build_query
{
    my ($aref) = @_;
    $result = 'SELECT count(*) from erps.attributes where ROOT_ID in ("0"';                 #o stub to handle last comma correctly
    foreach (@$aref) {
        $result .= ', "';
        $result .= $_;
        $result .= '"';
     }
    $result .= ');';
    $result =~ tr/\"/\'/;
    $result .= "\n\n";

    return $result;    
}

sub build_delete
{
    my ($aref) = @_;
    $result = 'DELETE from erps.attributes where ROOT_ID in ("0"';                 #o stub to handle last comma correctly
    foreach (@$aref) {
        $result .= ', "';
        $result .= $_;
        $result .= '"';
    }
    $result .= ');';
    $result =~ tr/\"/\'/;
    $result .= "\n\n";

    return $result;    

}

sub new {
        my ($class_name) = @_;

        my ($self) = {};
        #warn "We just created our new variable...\n ";

        bless ($self, $class_name);
        #warn "and now it's a $class_name object!\n";

        $self->{'_created'} = 1;
        return $self;
    }

sub run{
    my $self = shift;
    my ($infile, $sqloutfile, $dbname, $dbuser, $pw) = @_;

    print "debug: dbname= ", $dbname, "\n";
    print "debug: dbuser= ", $dbuser, "\n";
    print "debug: infile= ", $infile, "\n";
    print "debug: sqloutfile= ", $sqloutfile, "\n";

    #print "debug: password=" ,  $pw, "\n";  #avoid displaying password

    my $dbh  = DBI->connect("dbi:Oracle:$dbname", $dbuser, $pw)
            or die "Couldn't connect to database: " . DBI->errstr;
            
   my @data;
 
   my $inrec, $outrec, $sqlrec;
   my @holder, @sqlholder;

   my $mat_id;

   open(FHIN, "<$infile") or die "Cannot open $file: $!";

    my @content = <FHIN>;

  foreach $inrec(@content) {
    
    chomp($inrec);
    
    if ($inrec eq "" || $inrec =~ m/^#/){     #record starting with '#' is ignored
       next;
    }
    
    (@warray) = split(",", $inrec);

    $mat_id = '000000000';
    $mat_id .= $warray[0];
    
    push @holder, $mat_id;
    $mat_id = '';    
}

$sqlrec = build_query(\@holder);
push @sqlholder, $sqlrec;  #decomment only if you want to see the select stmt

  while($sqlrec =~ m/[\r\n;]$/) {      #remove trailing new line char(s) and semicolon
          chop($sqlrec);
  }
   

my $sth = $dbh->prepare($sqlrec) or die "Couldn't execute statement: " . $dbh->errstr;
$sth->execute();
my @data = $sth->fetchrow_array();
if($sth->rows == 0){
    $cnt = 0; 
}
else {
    $cnt = @data[0];
}
 
$sth->finish();


$sqlrec = '-- ' ; $sqlrec .= $cnt; $sqlrec .=  ' records will be removed.'; $sqlrec .= "\n\n";
push @sqlholder, $sqlrec;

$sqlrec = build_delete(\@holder);

push @sqlholder, $sqlrec;

close FHIN;

open (FHSQL, ">$sqloutfile") or die "Cannot open $file: $!";
print FHSQL @sqlholder;
close FHSQL;

print "\n";

$dbh->disconnect;

}

warn "Removeattrs is successfully loaded!\n";
1;