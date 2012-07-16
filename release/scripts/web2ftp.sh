#!/bin/bash
echo ">>>>>>> DOWNLOADING FILE FROM WEB"
if [ $# -eq 0 ] ; then
    echo 'Script to download a file from web url and to submit it to an ftp account'
    echo '    arg 1: website url' 
    echo '    arg 2: filename to save web url as'
    echo '    arg 3: ftp site url'
    echo '    arg 4: ftp user name'
    echo '    arg 5: ftp password'
    echo '    arg 6: error email receipent'
    echo '    arg 7: error description'
    exit 0
fi

function sendErrorMail() {
    echo "web2ftp failure: $2" | mail -s "$1" "$3"
    return 0
}


wget $1 -O $2 -t 5  --retry-connrefused
if [ "$?" -ne 0 ]; then 
    echo ">>>>>>> ERROR: wget failed" 
    sendErrorMail "$7" "wget failed" "$6"
    exit 1
fi

echo ">>>>>>> UPLOADING FILE TO FTP"

FTPLOG="web2ftp.log"
ftp -v -u -n $3 <<EOT | tee $FTPLOG
user $4 $5
put $2
bye
EOT

FTP_SUCCESS_MSG="226 Transfer complete"
if fgrep "$FTP_SUCCESS_MSG" $FTPLOG ;then
   echo ">>>>>>> SUCCESS: Transfer complete"
else
   echo ">>>>>>> ERROR: ftp failed"
   sendErrorMail "$7" "ftp failed" "$6"
   exit 1
fi

