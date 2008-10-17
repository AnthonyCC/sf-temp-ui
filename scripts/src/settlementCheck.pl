#!/usr/bin/perl
#
# Make sure settlements went okay
# 
# Charith Perera
# 20061212 - Altered to use OpenNMS send-event.pl if an error occurs
#
#
# Rob Francis
# rfrancis@freshdirect.com
# 20050318
########################

use DBI;


$debug = 1;
#$user  = "appdev";
#$pass  = "readn0wrt";


#$user  = "appdev";
#$pass  = "ReadN0Wrt";

#$INSTANCE ="DBSTO.NYC.FRESHDIRECT.COM";

#$user  = "fd_promo_rw";
#$pass  = "fd_promo_rw";
#$INSTANCE ="DBSTO.NYC.FRESHDIRECT.COM";

$user  = "fdstore_prda";
$pass  = "fdstore_prda";
$INSTANCE ="DEVINT.NYC.FRESHDIRECT.COM";
#$ENV{'ORACLE_HOME'} = "/opt/oracle/product/10.2.0";
$ENV{'ORACLE_HOME'}="/opt/fdbin/oracle/instantclient_11_1";


# Establish connections
my $dbh1 = DBI->connect( "dbi:Oracle:$INSTANCE", $user, $pass ) or dieWithError($DBI);

# Get the bad batch status count
$badBatchStatus = getBadBatchStatus($dbh1, $thread);

# Close the connection
$dbh1->disconnect;


# If debugging is turned on
if ($debug) {
    print "batch_status <> 0: $badBatchStatus\n";
}

# Create an alert if needed
if ($badBatchStatus != 0) {
    $output .= "\nThere are $badBatchStatus transactions with a batch status not equal to zero.\n";
}

# Send an alert if $output is non-empty
#sendAlert($output) if $output;


#################################################
# Subroutines
#################################################

sub getBadBatchStatus {
    my ($dbh) = @_;
    my($sth);
    my $sql = "select count(1) from paylinx.cc_settlement where batch_status <> 00";

    $sth = $dbh->prepare($sql);
    $sth->execute();
    my $count;
    $sth->bind_columns(undef, \$count);

    while ($sth->fetch() ) {
	   return $count;
    }
}

sub sendAlert {
    `/opt/OpenNMS/bin/send-event.pl uei.opennms.org/fd/store/paylinx -p "ErrorMsg $output"`;
}

sub dieWithError {
    `/opt/OpenNMS/bin/send-event.pl uei.opennms.org/fd/store/paylinx -p "ErrorMsg $DBI::errstr"`;
    exit;
}

# When all is done, exit nicely
exit(0);
