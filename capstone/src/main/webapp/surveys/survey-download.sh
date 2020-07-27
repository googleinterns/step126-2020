#!/bin/bash

#Json file name where client secret is stored
CLIENT_SECRET_FILE=''

if [ -z "$CLIENT_SECRET_FILE" ]
then
  echo Store the name of the json file where the client secret is stored within the CLIENT_SECRET_FILE variable 
  exit 1
fi

#download oauth21 tool
wget https://storage.googleapis.com/oauth2l/latest/linux_amd64.tgz
tar -xvf linux_amd64.tgz

#id for each survey that is currently deployed
SURVEY_IDS=('iilszvt24alys4gp7yyhypsuuq' 'edxr3yho5rkjsbcrepg7x4373e' 'wfj4mvsfw3mlcjvkoushioiomm' 'el6cjg2mb6rjbo5sel7lm3kkj4' 'rvhtbkditxingqxn5qoixau2eu'
'cm3hdousx3jvt4uaf4o6ukkjrq' 'lla7pgdxkjkrnxdjxnzzt24y7y' 'xsz7qvuip42ibjtsltglv7cxsy' 'lhujzdwrs6ukp4ci5rz734wy7y' 'h77xuhlpouhhlnc52lh7r6bflm' 
'qqegqs3pzo7aa7unu6f3wskeiq' '7m777v2tsvldrcjapn5fqiz3xy' '2qe3qoubki66ixvvr2brglahwi' 'oef7cqircmjizw6uk4r4xaukdi' 'rb2v6nh6vk4keyfblobuxfve2m' 
'mywyxzaekbuh43kc76fe47lxke' 'mg7qdviq7idbi6wia6t7svqgfm' 'dglzpokdznwwb7mbv27idkewbm' 'jdq4devbyy2kuup2minggrs5ty' 'mzulceapcv5nm3q3jcewfv3l2y' 
'z4gkxlmetp3cgahkskuuwno2tu' 'hjgt4ozk7uztw2eylsjrslls5u' 'hydgtwhoo63meppzhsicdtimhq' 'qqbzaiu2ptb5xrpaoipbz4eosi' 'opjq7l3y267o2rdljpvcs37qqa' 
'pcbt55lli3ypmo4p26veqvh5sm' 'u5s5nbi7fwdzycztqqjmpndseq' '7waxhohyj4aty2kxiu6lwq6zj4' 'ufgsknnuyr2sbilluju6hd4ycu' 'oc4ehskpfnbpde5hhgp3gtcabq' 
'v6zee7g66cg74klpr6nzo4vvjq' 'jspnt7lj5c4i2kv4fcudbml23q' 'btsrnwdc62mcavmfefql7cvml4' 'vsxcry3luael7zz7y6saf5e7ta' '65zghd6jjc2ffnyszgsgnbuadm' 
'2wes62yvhtknqkwbmkd5dot47a' 'qhbxjs7nd5a4zxnyfyudsogmva')

ZIP_CODES=('94103-r' '94103-r2' '94107-r' '94107-r2' '94109-r' '94109-r2' '94110-r' '94110-r2' '94112-r' '94112-r2' '94114-r' '94114-r2' '94116-r' '94116-r2' '94117-r' '94117-r2' '94118-r' 
'94118-r2' '94121-r' '94121-r2' '94122-r' '94122-r2' '94123-r' '94123-r2' '94124-r' '94124-r2' '94127-r' '94127-r2' '94129-r' '94129-r2' '94131-r' '94131-r2' '94132-r' '94132-r2' '94132-r3' 
'94134-r' '94134-r2')

#If prompted, follow instructions and find verification code in url param code
linux_amd64/oauth2l fetch --credentials $CLIENT_SECRET_FILE --scope https://www.googleapis.com/auth/surveys

for survey in "${SURVEY_IDS[@]}"; do 
  TOKEN=$(linux_amd64/oauth2l fetch --credentials client_secret_71285827048-mh3huvq5tkcfv846jvigmjb5b5bm51vm.apps.googleusercontent.com.json --scope https://www.googleapis.com/auth/surveys)
  curl "https://surveys.google.com/reporting/export_oauth?format=xls&survey=$survey&access_token=$TOKEN" > $survey.xls
done
echo Finished downloading ${#SURVEY_IDS[@]} surveys

#Download xls to csv module
echo Downloading CSV converter
sudo easy_install xlsx2csv

for i in "${!SURVEY_IDS[@]}"; do 
  echo Converting ${SURVEY_IDS[i]}.xls sheet three to ${ZIP_CODES[i]}.csv file
  xlsx2csv ${SURVEY_IDS[i]}.xls -s 3 > ${ZIP_CODES[i]}.csv
done
echo Finished converting ${#SURVEY_IDS[@]} excel files to csvs

rm *.xls
echo Cleared up ${#SURVEY_IDS[@]} excel files

find -empty -type f -delete
echo Cleared up empty files


